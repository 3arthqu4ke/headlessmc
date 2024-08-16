package me.earth.headlessmc.launcher.command;

import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.launch.AssetsDownloader;
import me.earth.headlessmc.launcher.version.Library;
import me.earth.headlessmc.launcher.version.Version;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class IntegrityCommand extends AbstractVersionCommand {
    public IntegrityCommand(Launcher ctx) {
        super(ctx, "integrity", "Checks the integrity of a version.");
    }

    @Override
    public void execute(Version version, String... args) throws CommandException {
        try {
            int failed = 0;
            int successful = 0;
            int notThere = 0;
            // TODO: make this a service instead
            AssetsDownloader assetsDownloader = new AssetsDownloader(ctx.getMcFiles(), ctx, "", "");
            for (Library library : version.getLibraries()) {
                String libPath = library.getPath(ctx.getProcessFactory().getOs());
                Path path = Paths.get(ctx.getProcessFactory().getFiles().getDir("libraries") + File.separator + libPath);
                if (Files.exists(path)) {
                    ctx.log("Checking " + libPath);
                    Long expectedSize = library.getSize();
                    long size;
                    if (expectedSize != null && expectedSize != (size = Files.size(path))) {
                        Files.delete(path);
                        ctx.log("Integrity check failed! Expected size " + expectedSize + " but was " + size + "! Deleting " + libPath);
                        failed++;
                        continue;
                    }

                    String sha1 = library.getSha1();
                    if (sha1 != null) {
                        String actualHash = assetsDownloader.toHashString(createSha1(path));
                        if (!sha1.equalsIgnoreCase(actualHash)) {
                            Files.delete(path);
                            ctx.log("Integrity check failed, expected " + sha1 + ", actual " + actualHash + ", deleting " + libPath);
                            failed++;
                            continue;
                        }
                    }

                    successful++;
                } else {
                    ctx.log("Failed to find " + libPath);
                    notThere++;
                }
            }

            ctx.log("Integrity check finished, " + failed + " failed, " + successful + " successful and " + notThere + " not found.");
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new CommandException("Failed to check integrity: " + e.getMessage());
        }
    }

    public byte[] createSha1(Path file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        try (InputStream is = Files.newInputStream(file)) {
            int n = 0;
            byte[] buffer = new byte[8192];
            while (n != -1) {
                n = is.read(buffer);
                if (n > 0) {
                    digest.update(buffer, 0, n);
                }
            }

            return digest.digest();
        }
    }

}
