package main.java.kr.mcraft.kongblocks.events;

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
		if(KongBlocks.map.containsKey(block)) {
			if(KongBlocks.enabled.contains(KongBlocks.map.get(block))) {
				if(event.getPlayer().isOp()) {
					KongBlocks.removeBlock(block, event.getPlayer());
				} else {
					if(KongBlocks.map.get(block).equals(event.getPlayer().getUniqueId().toString())) {
						KongBlocks.removeBlock(block, event.getPlayer());
					} else {
						MessageSender.playerMessage(event.getPlayer(), KongBlocks.getPrefix() + KongBlocks.permissionMessage);
						event.setCancelled(true);
					}
				}
			} else {
				KongBlocks.removeBlock(block, event.getPlayer());
			}
		}
	}
}
