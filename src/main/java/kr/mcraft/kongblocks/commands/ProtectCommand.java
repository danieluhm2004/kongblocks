package main.java.kr.mcraft.kongblocks.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.kr.mcraft.kongblocks.KongBlocks;
import main.java.kr.mcraft.kongblocks.utils.MessageSender;

public class ProtectCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("보호")) {
			if(args.length == 0) {
				Player player = (Player) sender;
				if(!KongBlocks.enabled.contains(player.getUniqueId().toString())) {
					MessageSender.playerMessage(player, KongBlocks.enableMessage);
					KongBlocks.enabled.add(player.getUniqueId().toString());
				} else {
					MessageSender.playerMessage(player, KongBlocks.disableMessage);
					KongBlocks.enabled.remove(player.getUniqueId().toString());
				}
			} else if(args[0].equalsIgnoreCase("리뷰")) {
				
			}
		}
		return true;
	}

}
