package main.java.kr.mcraft.kongblocks.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import main.java.kr.mcraft.kongblocks.KongBlocks;
import main.java.kr.mcraft.kongblocks.utils.MessageSender;

public class PlayerJoin implements Listener {
	@EventHandler
	public void onEvent(PlayerJoinEvent event) {
		if(!event.getPlayer().hasPlayedBefore()) {
			KongBlocks.enabled.add(event.getPlayer().getUniqueId().toString());
		}
		
		if(KongBlocks.enabled.contains(event.getPlayer().getUniqueId().toString())) {
			MessageSender.playerMessage(event.getPlayer(), KongBlocks.getPrefix() + KongBlocks.statusEnableMessage);
		} else {
			MessageSender.playerMessage(event.getPlayer(), KongBlocks.getPrefix() + KongBlocks.statusDisableMessage);
		}
		KongBlocks.randomReview(event.getPlayer());
	}
}