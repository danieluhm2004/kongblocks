package main.java.kr.mcraft.kongblocks.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import main.java.kr.mcraft.kongblocks.KongBlocks;
import main.java.kr.mcraft.kongblocks.objects.BlockObject;
import main.java.kr.mcraft.kongblocks.utils.MessageSender;

public class BlockPlace implements Listener {
	@EventHandler(priority=EventPriority.LOWEST)
	public void onEvent(BlockPlaceEvent event) {
		BlockObject block = new BlockObject(event.getBlock());
		if(!KongBlocks.databaseMap.containsKey(block)) {
			KongBlocks.addBlock(block, event.getPlayer());
		} else {
			MessageSender.playerMessage(event.getPlayer(), KongBlocks.getPrefix() + KongBlocks.hasOwnerMessage);
			event.setCancelled(true);
		}
	}
}
