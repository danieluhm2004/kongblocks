package main.java.kr.mcraft.kongblocks.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import main.java.kr.mcraft.kongblocks.KongBlocks;
import main.java.kr.mcraft.kongblocks.objects.BlockObject;
import main.java.kr.mcraft.kongblocks.utils.MessageSender;

public class BlockBreak implements Listener {
	@EventHandler(priority=EventPriority.LOWEST)
	public void onEvent(BlockBreakEvent event) {
		BlockObject block = new BlockObject(event.getBlock());
		if(KongBlocks.databaseMap.containsKey(block)) {
			Player player = event.getPlayer();
			String target = KongBlocks.databaseMap.get(block);
			if(KongBlocks.enabled.contains(target)) {
				if(event.getPlayer().isOp()) {
					KongBlocks.removeBlock(block, player);
					return;
				}
				
				if(player.getUniqueId().toString().equalsIgnoreCase(target)) {
					KongBlocks.removeBlock(block, player);
				} else {
					MessageSender.playerMessage(event.getPlayer(), KongBlocks.getPrefix() + KongBlocks.hasOwnerMessage.replace("<player>", Bukkit.getOfflinePlayer(UUID.fromString(target)).getName()));
					event.setCancelled(true);
				}
			} else {
				KongBlocks.removeBlock(block, player);
			}
		}
	}
}
