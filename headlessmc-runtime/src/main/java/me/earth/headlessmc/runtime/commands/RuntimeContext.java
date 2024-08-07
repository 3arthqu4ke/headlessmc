package me.earth.headlessmc.runtime.commands;

import me.earth.headlessmc.api.command.*;
import me.earth.headlessmc.api.command.impl.HelpCommand;
import me.earth.headlessmc.api.command.impl.MemoryCommand;
import me.earth.headlessmc.api.command.impl.MultiCommand;
import me.earth.headlessmc.runtime.Runtime;

// TODO: array command?
@SuppressWarnings({"unchecked", "RedundantSuppression"}) // delegate
public class RuntimeContext extends CommandContextImpl {
    public RuntimeContext(Runtime ctx) {
        super(ctx);
        add(new ClassCommand(ctx));
        add(new FieldCommand(ctx));
        add(new MethodCommand(ctx));
        add(new DumpCommand(ctx));
        add(TypeCommand.ofType(ctx, "string", s -> s));
        add(TypeCommand.ofType(ctx, "char", s -> s.charAt(0)));
        add(TypeCommand.ofType(ctx, "boolean", Boolean::parseBoolean));
        add(TypeCommand.ofType(ctx, "byte", ParseUtil::parseB));
        add(TypeCommand.ofType(ctx, "short", ParseUtil::parseS));
        add(TypeCommand.ofType(ctx, "int", ParseUtil::parseI));
        add(TypeCommand.ofType(ctx, "long", ParseUtil::parseL));
        add(TypeCommand.ofType(ctx, "float", ParseUtil::parseF));
        add(TypeCommand.ofType(ctx, "double", ParseUtil::parseD));
        add(new NewCommand(ctx));
        add(new PopCommand(ctx));
        add(new CopyCommand(ctx));
        add(new RunnableCommand(ctx));
        add(new FunctionCommand(ctx));
        add(new SupplierCommand(ctx));
        add(new HelpCommand(ctx));
        add(new RuntimeQuitCommand(ctx));
        add(new MemoryCommand(ctx));
        add(new PasswordCommand(ctx));
        add(new IfCommand(ctx));
        add(new WhileCommand(ctx));
        add(new MultiCommand(ctx));
    }

}
