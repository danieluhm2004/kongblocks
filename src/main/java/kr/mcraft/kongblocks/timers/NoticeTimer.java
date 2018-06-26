package main.java.kr.mcraft.kongblocks.timers;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import main.java.kr.mcraft.kongblocks.KongBlocks;
import main.java.kr.mcraft.kongblocks.utils.MessageSender;

public class NoticeTimer extends BukkitRunnable {
	public static BukkitTask task;
	
	@SuppressWarnings("deprecation")
	public static void start(int time) {
		task = Bukkit.getScheduler().runTaskTimer(KongBlocks.getInstance(), new NoticeTimer(), 0, time * 20);
	}
	
	public static void stop() {
		task.cancel();
	}
	
	@Override
	public void run() {
		MessageSender.playersMessage(KongBlocks.getPrefix() + KongBlocks.noticeMessage.replace("<size>", String.valueOf(KongBlocks.databaseMap.size())));
	}
}
