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
        val total = (Runtime.getRuntime().totalMemory() / 1024L / 1024L);
        val free = (Runtime.getRuntime().freeMemory() / 1024L / 1024L);
        val max = (Runtime.getRuntime().maxMemory() / 1024L / 1024L);
        val usedMemory = total - free;
        val percent = DF.format(usedMemory * 100L / (double) max);
        ctx.log("-Used:  " + usedMemory + "mb, (" + percent + "%)");
        ctx.log("-Free:  " + free + "mb");
        ctx.log("-Total: " + total + "mb");
        ctx.log("-Max:   " + max + "mb");
    }

}
