package me.earth.headlessmc.auth;

import lombok.CustomLog;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.command.AbstractCommand;
import me.earth.headlessmc.command.CommandUtil;
import net.lenni0451.commons.httpclient.HttpClient;
import net.raphimc.minecraftauth.MinecraftAuth;
import net.raphimc.minecraftauth.step.AbstractStep;
import net.raphimc.minecraftauth.step.java.session.StepFullJavaSession;
import net.raphimc.minecraftauth.step.msa.StepCredentialsMsaCode;
import net.raphimc.minecraftauth.step.msa.StepJfxWebViewMsaCode;
import net.raphimc.minecraftauth.step.msa.StepMsaDeviceCode;
import net.raphimc.minecraftauth.util.MicrosoftConstants;
import net.raphimc.minecraftauth.util.logging.ILogger;
import net.raphimc.minecraftauth.util.logging.JavaConsoleLogger;

import javax.swing.*;
import java.awt.*;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@CustomLog
public abstract class AbstractLoginCommand extends AbstractCommand {
    private final List<Thread> threads = new CopyOnWriteArrayList<>();

    protected final Object webviewLock = new Object();
    protected AbstractStep<?, StepFullJavaSession.FullJavaSession> webview;
    protected volatile Window webviewWindow = null;

    public AbstractLoginCommand(HeadlessMc ctx) {
        this(ctx, "login", "Logs you into an account.");
    }

    public AbstractLoginCommand(HeadlessMc ctx, String name, String description) {
        super(ctx, name, description);
        replaceLoggerOnConstruction();
    }

    protected abstract void onSuccessfulLogin(StepFullJavaSession.FullJavaSession session);

    @Override
    public void execute(String... args) throws CommandException {
        if (CommandUtil.hasFlag("-webview", args)) {
            loginWithWebview(args);
            return;
        }

        if (args.length > 1 && args[1].equalsIgnoreCase("-cancel")) {
            cancelLoginProcess(args);
        } else if (args.length >= 2 && args[1].contains("@")) {
            loginWithCredentials(args);
        } else {
            // TODO: still unclear, cannot specify logger
            loginWithDeviceCode(args);
        }
    }

    protected void loginWithCredentials(String... args) {
        String helpMessage = "Enter your password or type 'abort' to cancel the login process."
            + (ctx.isHidingPasswordsSupported()
                ? ""
                : " (Your password will be visible when you type!)");
        ctx.log(helpMessage);

        String email = args[1];
        boolean passwordsHiddenBefore = ctx.isHidingPasswords();
        ctx.setHidingPasswords(true);
        ctx.setWaitingForInput(true);
        ctx.setCommandContext(
            new LoginContext(ctx, ctx.getCommandContext(), helpMessage) {
                @Override
                protected void onCommand(String password) {
                    try {
                        login(email, password, args);
                    } finally {
                        returnToPreviousContext();
                        if (!passwordsHiddenBefore) {
                            ctx.setHidingPasswords(false);
                        }

                        ctx.setWaitingForInput(false);
                    }
                }
            });
    }

    protected void login(String email, String password, String... args) {
        try {
            HttpClient httpClient = MinecraftAuth.createHttpClient();
            StepFullJavaSession.FullJavaSession session = MinecraftAuth.JAVA_CREDENTIALS_LOGIN.getFromInput(
                getLogger(args), httpClient, new StepCredentialsMsaCode.MsaCredentials(email, password));

            onSuccessfulLogin(session);
        } catch (Exception e) {
            ctx.log("Failed to login: " + e.getMessage());
            log.warn(e);
        }
    }

    protected void loginWithWebview(String... args) throws CommandException {
        if (webview == null) {
            webview = provideWebview();
            if (webview == null) {
                return;
            }
        }

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = MinecraftAuth.createHttpClient();

                    // TODO: look into this?
                    if (webviewWindow != null) {
                        CookieHandler.setDefault(new CookieManager());
                    }

                    StepFullJavaSession.FullJavaSession session = webview.getFromInput(getLogger(args), httpClient, getWebViewCallback());

                    ctx.log("Session from Webview: " + session);
                    onSuccessfulLogin(session);
                } catch (InterruptedException e) {
                    ctx.log("Login process cancelled successfully.");
                } catch (NoClassDefFoundError e) {
                    log.debug(e.getMessage());
                    ctx.log("Your version of Java does not support Webview! It usually comes bundled with JDK 8 or in the headlessmc-launcher-jfx jar.");
                } catch (Throwable t) {
                    ctx.log("Failed to login with webview: " + t.getMessage());
                } finally {
                    synchronized (threads) {
                        threads.remove(this);
                    }
                }
            }
        };

        startLoginThread(thread);
    }

    protected StepJfxWebViewMsaCode.JavaFxWebView getWebViewCallback() {
        Consumer<JFrame> openCallback = frame -> frame.setVisible(true);
        return new StepJfxWebViewMsaCode.JavaFxWebView(
            openCallback,
            frame ->  {
                // dispose old JFrames, but keep one so JavaFX does not end!
                synchronized (webviewLock) {
                    Window before = webviewWindow;
                    if (before != null && frame.isVisible()) {
                        before.dispose();
                    }

                    frame.setVisible(false);
                    webviewWindow = frame;
                }
            });

    }

    protected AbstractStep<?, StepFullJavaSession.FullJavaSession> provideWebview() throws CommandException {
        try {
            return MinecraftAuth.builder()
                                .withClientId(MicrosoftConstants.JAVA_TITLE_ID)
                                .withScope(MicrosoftConstants.SCOPE_TITLE_AUTH)
                                .javaFxWebView()
                                .withDeviceToken("Win32")
                                .sisuTitleAuthentication(MicrosoftConstants.JAVA_XSTS_RELYING_PARTY)
                                .buildMinecraftJavaProfileStep(true);
        } catch (NoClassDefFoundError e) {
            log.debug(e.getMessage());
            ctx.log("Your version of Java does not support Webview! It usually comes bundled with JDK 8 or in the headlessmc-launcher-jfx jar.");
        } catch (Throwable t) {
            throw new CommandException("Failed to login with webview: " + t.getMessage());
        }

        return null;
    }

    protected void loginWithDeviceCode(String... args) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = MinecraftAuth.createHttpClient();
                    StepMsaDeviceCode.MsaDeviceCodeCallback callback = new StepMsaDeviceCode.MsaDeviceCodeCallback(
                        msaDeviceCode -> ctx.log("Go to " + msaDeviceCode.getDirectVerificationUri()));

                    StepFullJavaSession.FullJavaSession session = MinecraftAuth.JAVA_DEVICE_CODE_LOGIN.getFromInput(getLogger(args), httpClient, callback);

                    onSuccessfulLogin(session);
                } catch (InterruptedException e) {
                    ctx.log("Login process cancelled successfully.");
                } catch (Exception e) {
                    ctx.log("Failed to login with device code: " + e.getMessage());
                    log.info(e);
                } finally {
                    synchronized (threads) {
                        threads.remove(this);
                    }
                }
            }
        };

        startLoginThread(thread);
    }

    protected void cancelLoginProcess(String... args) throws CommandException {
        if (args.length <= 2) {
            throw new CommandException("Please specify the login process id!");
        }

        synchronized (threads) {
            for (Thread thread : threads) {
                if (("HMC Login Thread - " + args[2]).equals(thread.getName())) {
                    thread.interrupt();
                    threads.remove(thread);
                    ctx.log("Cancelled login process " + args[2] + ".");
                    return;
                }
            }
        }

        ctx.log("Failed to find login process with id " + args[2] + "!");
    }

    protected void startLoginThread(Thread thread) {
        int threadId = 0;
        synchronized (threads) {
            String name = "HMC Login Thread - " + threadId;
            while (hasThreadWithName(name)) {
                threadId++;
                name = "HMC Login Thread - " + threadId;
            }

            thread.setName("HMC Login Thread - " + threadId);
            thread.setDaemon(true);
            threads.add(thread);
        }

        ctx.log("Starting login process " + threadId + ", enter 'login -cancel " + threadId + "' to cancel the login process.");
        thread.start();
    }

    protected ILogger getLogger(String... args) {
        if (CommandUtil.hasFlag("-verbose", args)) {
            return MinecraftAuth.LOGGER;
        } else {
            return NoLogging.INSTANCE;
        }
    }

    protected boolean hasThreadWithName(String threadName) {
        return threads.stream().anyMatch(t -> threadName.equals(t.getName()));
    }

    protected void replaceLoggerOnConstruction() {
        replaceLogger();
    }

    public static void replaceLogger() {
        MinecraftAuth.LOGGER = new JavaConsoleLogger(java.util.logging.Logger.getLogger("MinecraftAuth"));
    }

}
