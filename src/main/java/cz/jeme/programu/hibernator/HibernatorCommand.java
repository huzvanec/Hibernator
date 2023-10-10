package cz.jeme.programu.hibernator;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class HibernatorCommand extends Command {
    private final @NotNull Hibernator plugin;

    HibernatorCommand(@NotNull Hibernator plugin) {
        super("hibernator", "Main hibernator command", "false", List.of("hb"));
        Bukkit.getCommandMap().register("hibernator", this);
        setPermission("hibernator.hibernator");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length == 0) {
            Hibernator.config.set(
                    "enabled",
                    !Hibernator.config.getBoolean("enabled")
            );
            plugin.saveConfig();
            if (Hibernator.config.getBoolean("enabled")) {
                sender.sendMessage(Message.prefix("<green>Hibernator enabled</green>"));
                HibernationManager.INSTANCE.enableHibernation();
            } else {
                HibernationManager.INSTANCE.disableHibernation();
                sender.sendMessage(Message.prefix("<green>Hibernator disabled</green>"));
            }
            return true;
        }
        if (args[0].equals("reload")) {
            HibernationManager.INSTANCE.reload();
            plugin.reload();
            sender.sendMessage(Message.prefix("<green>Hibernator reloaded successfully</green>"));
            return true;
        }
        if (args[0].equals("enable")) {
            Hibernator.config.set("enabled", true);
            HibernationManager.INSTANCE.enableHibernation();
            sender.sendMessage(Message.prefix("<green>Hibernator enabled</green>"));
            return true;
        }
        if (args[0].equals("disable")) {
            Hibernator.config.set("enabled", false);
            HibernationManager.INSTANCE.disableHibernation();
            sender.sendMessage(Message.prefix("<green>Hibernator disabled</green>"));
            return true;
        }
        if (args[0].equals("status")) {
            String pluginStatus = "<red>disabled</red>";
            if (Hibernator.config.getBoolean("enabled")) {
                pluginStatus = "<green>enabled</green>";
            }
            sender.sendMessage(Message.prefix("<aqua><b>=== STATUS ===</b></aqua>"));
            sender.sendMessage(Message.prefix("<aqua>Plugin: " + pluginStatus + "</aqua>"));
            String hibernationStatus = "<red>off</red>";
            if (HibernationManager.INSTANCE.isHibernationEnabled()) {
                hibernationStatus = "<green>running</green>";
            }
            sender.sendMessage(Message.prefix("<aqua>Hibernation: " + hibernationStatus + "</aqua>"));
            double tps = Hibernator.config.getDouble("hibernation-tps");
            sender.sendMessage(Message.prefix("<aqua>Hibernation TPS: <transition:#00FF00:#FF0000:" + tps / 20d + ">" + tps + "</transition></aqua>"));
            return true;
        }
        sender.sendMessage(Message.prefix("<red>Unknown command!</red>"));
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return containsFilter(List.of("reload", "enable", "disable", "status"), args[0]);
        }
        return Collections.emptyList();
    }

    private @NotNull List<String> containsFilter(@NotNull Collection<String> collection, @NotNull String mark) {
        return collection.stream()
                .filter(item -> item.contains(mark))
                .toList();
    }
}
