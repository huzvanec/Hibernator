package cz.jeme.programu.hibernator;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class HibernatorCommand extends Command {
    private final HibernationManager hibernationManager;
    private final Config config;
    protected HibernatorCommand(Config config, HibernationManager hibernationManager) {
        super("hibernator", "Main hibernator command", "false", Collections.emptyList());
        register();
        this.config = config;
        this.hibernationManager = hibernationManager;
        setPermission("hibernator.hibernator");
    }

    private void register() {
        Bukkit.getCommandMap().register("hibernator", this);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length == 0) {
            config.setPluginEnabled(!config.isPluginEnabled());
            if (config.isPluginEnabled()) {
                sender.sendMessage(Messages.prefix("<green>Hibernator enabled</green>"));
                hibernationManager.enableHibernation();
            } else {
                hibernationManager.disableHibernation();
                sender.sendMessage(Messages.prefix("<green>Hibernator disabled</green>"));
            }
            return true;
        }
        if (args[0].equals("reload")) {
            Hibernator.reload(config, hibernationManager);
            sender.sendMessage(Messages.prefix("<green>Hibernator reloaded successfully</green>"));
            return true;
        }
        if (args[0].equals("enable")) {
            config.setPluginEnabled(true);
            hibernationManager.enableHibernation();
            sender.sendMessage(Messages.prefix("<green>Hibernator enabled</green>"));
            return true;
        }
        if (args[0].equals("disable")) {
            config.setPluginEnabled(false);
            hibernationManager.disableHibernation();
            sender.sendMessage(Messages.prefix("<green>Hibernator disabled</green>"));
            return true;
        }
        if (args[0].equals("status")) {
            String pluginStatus = "<red>disabled</red>";
            if (config.isPluginEnabled()) {
                pluginStatus = "<green>enabled</green>";
            }
            sender.sendMessage(Messages.prefix("<aqua><b>=== STATUS ===</b></aqua>"));
            sender.sendMessage(Messages.prefix("<aqua>Plugin: " + pluginStatus + "</aqua>"));
            String hibernationStatus = "<red>off</red>";
            if (hibernationManager.isHibernationEnabled()) {
                hibernationStatus = "<green>running</green>";
            }
            sender.sendMessage(Messages.prefix("<aqua>Hibernation: " + hibernationStatus + "</aqua>"));
            double tps = Config.parseSleep(config.getSleep());
            sender.sendMessage(Messages.prefix("<aqua>Hibernation TPS: <transition:#00FF00:#FF0000:" + tps / 20d + ">" + tps + "</transition></aqua>"));
            return true;
        }
        sender.sendMessage(Messages.prefix("<red>Unknown command!</red>"));
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return containsFilter(List.of("reload", "enable", "disable", "status"), args[0]);
        }
        return Collections.emptyList();
    }

    private List<String> containsFilter(List<String> list, String mark) {
        return list.stream()
                .filter(item -> item.contains(mark))
                .toList();
    }
}
