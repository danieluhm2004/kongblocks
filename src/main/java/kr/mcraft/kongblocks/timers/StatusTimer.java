package main.java.kr.mcraft.kongblocks.timers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import main.java.kr.mcraft.kongblocks.KongBlocks;
import main.java.kr.mcraft.kongblocks.utils.MessageSender;

public class StatusTimer extends BukkitRunnable {
	public static BukkitTask task;
	
	@SuppressWarnings("deprecation")
	public static void start(int time) {
		task = Bukkit.getScheduler().runTaskTimer(KongBlocks.getInstance(), new StatusTimer(), 0, time * 20);
	}
	
	public static void stop() {
		task.cancel();
	}
	
	@Override
	public void run() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(KongBlocks.enabled.contains(player.getUniqueId().toString())) {
				MessageSender.playerMessage(player, KongBlocks.getPrefix() + KongBlocks.statusEnableMessage);
			} else {
				MessageSender.playerMessage(player, KongBlocks.getPrefix() + KongBlocks.statusDisableMessage);
			}
			KongBlocks.randomReview(player);
		}
	}
}
