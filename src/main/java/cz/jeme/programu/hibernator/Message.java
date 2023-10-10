package cz.jeme.programu.hibernator;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public final class Message {
    private Message() {
        throw new AssertionError();
    }

    public static final @NotNull MiniMessage MESSAGE = MiniMessage.miniMessage();
    public static final @NotNull String PREFIX = "<dark_gray>[<gradient:#7DC9EF:#506CC0>Hibernator</gradient>]</dark_gray> ";

    public static @NotNull Component from(@NotNull String string) {
        return MESSAGE.deserialize(string);
    }

    public static @NotNull Component prefix(@NotNull String string) {
        return from(PREFIX + string);
    }

    public static @NotNull String strip(@NotNull String text) {
        return MESSAGE.stripTags(text);
    }

    public static @NotNull String strip(@NotNull Component component) {
        return strip(to(component));
    }

    public static @NotNull String to(@NotNull Component component) {
        return MESSAGE.serialize(component);
    }
}