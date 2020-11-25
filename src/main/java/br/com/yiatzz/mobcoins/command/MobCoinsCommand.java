package br.com.yiatzz.mobcoins.command;

import br.com.yiatzz.mobcoins.MobCoinsPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.NumberFormat;

public class MobCoinsCommand implements CommandExecutor {

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        Double mobCoins = MobCoinsPlugin.getCache().get(player.getName());

        if (mobCoins == null) {
            player.sendMessage("§cVocê não possui mobcoins.");
            return false;
        }

        player.sendMessage("§eSeus mobcoins: §f" + NUMBER_FORMAT.format(mobCoins));
        return true;
    }
}
