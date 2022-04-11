package me.earth.headlessmc.api.command;

/**
 * Objects of this type hold an instance of an {@link CommandContext}.
 */
public interface HasCommandContext {
    /**
     * Returns the {@link CommandContext} set via {@link
     * #setCommandContext(CommandContext)}.
     *
     * @return the CommandContext held by this object.
     */
    CommandContext getCommandContext();

    /**
     * Sets the {@link CommandContext} returned by {@link #getCommandContext()}.
     *
     * @param context the command context to set.
     */
    void setCommandContext(CommandContext context);

}
