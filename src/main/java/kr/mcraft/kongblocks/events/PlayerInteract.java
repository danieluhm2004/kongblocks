package main.java.kr.mcraft.kongblocks.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import main.java.kr.mcraft.kongblocks.KongBlocks;
import main.java.kr.mcraft.kongblocks.objects.BlockObject;
import main.java.kr.mcraft.kongblocks.utils.MessageSender;

public class PlayerInteract implements Listener {
	@EventHandler
	public void onEvent(PlayerInteractEvent event) {
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(KongBlocks.informationEnable) {
				if(KongBlocks.information.contains(event.getPlayer())) {
					BlockObject block = new BlockObject(event.getClickedBlock());
					String target = KongBlocks.databaseMap.get(block);
					if(KongBlocks.databaseMap.containsKey(block)) {
						if(KongBlocks.information.contains(event.getPlayer())) {
							MessageSender.playerMessage(event.getPlayer(), KongBlocks.getPrefix() + KongBlocks.informationMessage.replace("<player>", Bukkit.getOfflinePlayer(UUID.fromString(target)).getName()));
						}
					} else {
						MessageSender.playerMessage(event.getPlayer(), KongBlocks.getPrefix() + KongBlocks.informationNoneMessage);
					}
					event.setCancelled(true);
					event.getPlayer().updateInventory();
				}
			}
		}
	}
}
