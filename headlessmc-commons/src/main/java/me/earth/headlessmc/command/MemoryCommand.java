package me.earth.headlessmc.command;

import lombok.val;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.command.CommandException;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MemoryCommand extends AbstractCommand {
    private static final DecimalFormat DF = new DecimalFormat("#.##");

    static {
        DF.setRoundingMode(RoundingMode.CEILING);
    }

    public MemoryCommand(HeadlessMc ctx) {
        super(ctx, "memory", "Displays Memory stats.");
    }

    @Override
    public void execute(String... args) throws CommandException {
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
