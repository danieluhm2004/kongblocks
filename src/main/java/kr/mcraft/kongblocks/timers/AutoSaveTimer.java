package main.java.kr.mcraft.kongblocks.timers;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import main.java.kr.mcraft.kongblocks.KongBlocks;
import main.java.kr.mcraft.kongblocks.utils.MessageSender;

public class AutoSaveTimer extends BukkitRunnable {
	public static BukkitTask task;
	
	@SuppressWarnings("deprecation")
	public static void start(int time) {
		task = Bukkit.getScheduler().runTaskTimer(KongBlocks.getInstance(), new AutoSaveTimer(), 0, time * 20);
	}
	
	public static void stop() {
		task.cancel();
	}
	
	@Override
	public void run() {
		try {
			KongBlocks.saveDatabase();
			if(KongBlocks.autoSaveMessageEnable) {
				MessageSender.playersMessage(KongBlocks.getPrefix() + KongBlocks.saveMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageSender.consoleMessage(KongBlocks.getPrefix() + "다음 &c오류&f로 인하여 &e파일&f을 &6저장&f할 수 없습니다.");
		}
	}
}
