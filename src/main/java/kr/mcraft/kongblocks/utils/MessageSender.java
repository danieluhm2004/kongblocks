package main.java.kr.mcraft.kongblocks.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageSender {
	private static boolean debug = false;

	public static void playerMessage(Player player, String message) {
		player.sendMessage(getColor(message));
	}

	public static void playersMessage(String message) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			playerMessage(player, message);
		}
	}

	public static void consoleMessage(String message) {
		Bukkit.getConsoleSender().sendMessage(getColor(message));
	}

	public static String getColor(String message) {
		return message.replace("&", "§");
	}

	public static void debugMessage(String message) {
		if (debug) {
			consoleMessage(message);
		}
	}

	public static void setDebug(boolean debug) {
		MessageSender.debug = debug;
	}
}
