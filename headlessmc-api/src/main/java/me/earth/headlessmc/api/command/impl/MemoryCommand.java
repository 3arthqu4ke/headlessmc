package me.earth.headlessmc.api.command.impl;

import lombok.val;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.LogsMessages;
import me.earth.headlessmc.api.command.AbstractCommand;
import me.earth.headlessmc.api.command.Command;
import me.earth.headlessmc.api.command.CommandException;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * A {@link Command} implementation that displays the currently used memory, free memory and max memory of the JVM.
 */
public class MemoryCommand extends AbstractCommand {
    private static final DecimalFormat DF = new DecimalFormat("#.##");
    private static final long BYTES_TO_MB = 1024L * 1024L; // Bytes to megabytes conversion factor
    private static final long PERCENTAGE_SCALE = 100L;

    static {
        DF.setRoundingMode(RoundingMode.CEILING);
    }

    /**
     * Constructs a new MemoryCommand instance.
     *
     * @param ctx the {@link LogsMessages} used to log the output.
     */
    public MemoryCommand(HeadlessMc ctx) {
        super(ctx, "memory", "Displays Memory stats.");
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
        val total = Runtime.getRuntime().totalMemory() / BYTES_TO_MB;
        val free = Runtime.getRuntime().freeMemory() / BYTES_TO_MB;
        val max = Runtime.getRuntime().maxMemory() / BYTES_TO_MB;
        val usedMemory = total - free;
        val percent = DF.format(usedMemory * PERCENTAGE_SCALE / (double) max);
        ctx.log("-Used:  " + usedMemory + "mb, (" + percent + "%)");
        ctx.log("-Free:  " + free + "mb");
        ctx.log("-Total: " + total + "mb");
        ctx.log("-Max:   " + max + "mb");
    }

}
