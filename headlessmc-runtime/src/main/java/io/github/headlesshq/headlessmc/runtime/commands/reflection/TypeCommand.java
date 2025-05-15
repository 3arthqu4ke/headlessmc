package io.github.headlesshq.headlessmc.runtime.commands.reflection;

import io.github.headlesshq.headlessmc.api.command.CommandException;
import io.github.headlesshq.headlessmc.api.command.ParseUtil;
import io.github.headlesshq.headlessmc.runtime.reflection.RuntimeReflection;

public class TypeCommand extends AbstractRuntimeCommand {
    private final TypeParser parser;

    public TypeCommand(RuntimeReflection ctx, String name, String desc, TypeParser p) {
        super(ctx, name, desc);
        this.parser = p;
        this.args.put("<obj>", "The string to parse.");
        this.args.put("<addr>", "Where to store the object");
    }

    public static TypeCommand ofType(RuntimeReflection ctx, String type, TypeParser p) {
        return new TypeCommand(ctx, type, "Allocates " + type + "s.", p);
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
        if (args.length < 2) {
            throw new CommandException("Specify an object and an address!");
        } else if (args.length == 2) {
            throw new CommandException("Specify an address!");
        } else {
            int address = ParseUtil.parseI(args[2]);
            Object obj = parser.parse(args[1]);
            ctx.getVm().set(obj, address);
        }
    }

}
