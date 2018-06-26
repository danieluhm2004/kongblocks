package main.java.kr.mcraft.kongblocks;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import main.java.kr.mcraft.kongblocks.commands.ProtectCommand;
import main.java.kr.mcraft.kongblocks.events.BlockBreak;
import main.java.kr.mcraft.kongblocks.events.BlockPlace;
import main.java.kr.mcraft.kongblocks.events.PlayerJoin;
import main.java.kr.mcraft.kongblocks.objects.BlockObject;
import main.java.kr.mcraft.kongblocks.utils.MessageSender;

import static main.java.kr.mcraft.kongblocks.utils.MessageSender.*;

public class KongBlocks extends JavaPlugin {
	public static File config = new File("plugins" + File.separator + "KongBlocks" + File.separator + "config.yml");
	public static File databaseFile = new File(
			"plugins" + File.separator + "KongBlocks" + File.separator + "players.db");
	public static HashMap<BlockObject, String> map = new HashMap<BlockObject, String>();
	public static ArrayList<String> enabled = new ArrayList<String>();

	// Settings
	public static String prefix;
	public static boolean printConfig = true;
	public static boolean reviewEnable = true;
	public static boolean noticeEnable = true;
	public static int noticeTime = 600;
	public static boolean statusEnable = true;
	public static int statusTime = 600;
	public static String database = "players.db";

	// Message
	public static String enableMessage;
	public static String disableMessage;
	public static String permissionMessage;
	public static String noticeMessage;
	public static String reviewMessage;
	public static String statusEnableMessage;
	public static String statusDisableMessage;

	@Override
	public void onEnable() {
		try {
			super.onEnable();
			if (!config.exists()) {
				setup();
			}
			setConfig();
			consoleMessage(getPrefix() + "&a플러그인&f이 &a활성화&f중입니다!");
			consoleMessage(getPrefix() + "&9M&fcraft 에서 &e제작&f되었습니다. XD"); // 해당 내용은 수정하실 수 없습니다.
			update();
			registerCommand("보호", new ProtectCommand());
			registerEvent(new BlockBreak());
			registerEvent(new BlockPlace());
			registerEvent(new PlayerJoin());
			printConfig();
		} catch (Exception e) {
			e.printStackTrace();
			consoleMessage(getPrefix() + "다음 &c오류&f로 인하여 &e플러그인이 &c강제 종료&f되었습니다.");
			getPluginLoader().disablePlugin(this);
		}
	}

	@Override
	public void onDisable() {
		super.onDisable();
		consoleMessage(getPrefix() + "&a플러그인&f이 &c비활성화&f중입니다!");
		consoleMessage(getPrefix() + "&9M&fcraft 에서 &e제작&f되었습니다. XD"); // 해당 내용은 수정하실 수 없습니다.
		getConfig().set("users", enabled);
		saveConfig();
	}

	private void setup() throws Exception {
		config.getParentFile().mkdirs();
		saveResource("config.yml", true);

		// Settings
		getConfig().set("settings.printConfig", true);
		getConfig().set("settings.autoEnable", true);
		getConfig().set("settings.reviewEnable", true);
		getConfig().set("settings.noticeEnable", true);
		getConfig().set("settings.noticeTime", 600);
		getConfig().set("settings.statusEnable", true);
		getConfig().set("settings.statusTime", 600);
		getConfig().set("settings.database", "players.db");

		// Message
		getConfig().set("messages.prefix", "&f[ &9KongBlocks &f] &f");
		getConfig().set("messages.enableMessage", "&6보호 &e기능&f이 &a활성화&f되었습니다. &c비활성화&f하려면 &6/보호 &f를 &b입력&f하세요!");
		getConfig().set("messages.disableMessage", "&6보호 &e기능&f이 &c비활성화&f되었습니다. &a활성화&f하려면 &6/보호 &f를 &b입력&f하세요!");
		getConfig().set("messages.permissionMessage", "&f해당 블럭의 &e소유자&f는 &6<player>&f님이므로 &c파괴&f할 수 없습니다.");
		getConfig().set("messages.noticeMessage", "&f해당 &e서버&f는 &9공블럭&f을 &a사용&f하여 <size>개의 블럭을 보호하고 있습니다!");
		getConfig().set("messages.reviewMessage",
				"&9공블럭&f가 &6문제&f가 생기거나 &6괜찮은 아이디어&f가 있다면 &6/보호 리뷰 <메세지> &f를 입력해주세요! &c(다만 욕설 방지를 위해 아이피만 저장됩니다. 연속적으로 가능합니다.)");
		getConfig().set("messages.statusEnableMessage", "&f현재 &6보호 &e기능&f이 &a활성화&f된 상태입니다.");
		getConfig().set("messages.statusDisableMessage", "&f현재 &6보호 &e기능&f이 &c비활성화&f된 상태입니다.");

		saveConfig();
		databaseFile.createNewFile();
	}

	private void setConfig() {
		for (String uuid : getConfig().getStringList("users")) {
			enabled.add(uuid);
		}

		// Settings
		printConfig = getConfig().getBoolean("settings.printConfig");
		reviewEnable = getConfig().getBoolean("settings.reviewEnable");
		noticeEnable = getConfig().getBoolean("settings.noticeEnable");
		noticeTime = getConfig().getInt("settings.noticeTime");
		statusEnable = getConfig().getBoolean("settings.statusEnable");
		statusTime = getConfig().getInt("settings.statusTime");
		database = getConfig().getString("settings.database");

		// Message
		prefix = getConfig().getString("messages.prefix");
		enableMessage = getConfig().getString("messages.enableMessage");
		disableMessage = getConfig().getString("messages.disableMessage");
		permissionMessage = getConfig().getString("messages.permissionMessage");
		noticeMessage = getConfig().getString("messages.noticeMessage");
		reviewMessage = getConfig().getString("messages.reviewMessage");
		statusEnableMessage = getConfig().getString("messages.statusEnableMessage");
		statusDisableMessage = getConfig().getString("messages.statusDisableMessage");
	}

	private void printConfig() {
		if (printConfig) {
			// Settings
			consoleMessage(getPrefix() + "&e기본적인 &e설정&f을 불러옵니다.");
			consoleMessage(getPrefix() + "&f시작시 설정 내용 출력: " + printConfig);
			consoleMessage(getPrefix() + "&f플러그인 리뷰 허용: " + reviewEnable);
			consoleMessage(getPrefix() + "&f사용 메세지 출력: " + noticeEnable);
			consoleMessage(getPrefix() + "&f사용 메세지 출력 시간(초): " + noticeTime);
			consoleMessage(getPrefix() + "&f상태 메세지 출력: " + statusEnable);
			consoleMessage(getPrefix() + "&f상태 메세지 출력 시간(초): " + statusTime);
			consoleMessage(getPrefix() + "&f데이터 파일 위치: " + database);

			// Message
			consoleMessage(getPrefix() + "&e메세지 &e설정&f을 불러옵니다.");
			consoleMessage(getPrefix() + "&f활성화 메세지: " + enableMessage);
			consoleMessage(getPrefix() + "&f비활성화 메세지: " + disableMessage);
			consoleMessage(getPrefix() + "&f블럭 파괴 메세지: " + permissionMessage);
			consoleMessage(getPrefix() + "&f사용 메세지: " + noticeMessage);
			consoleMessage(getPrefix() + "&f리뷰 요청 메세지: " + reviewMessage);
			consoleMessage(getPrefix() + "&f상태 메세지(활성화): " + statusEnableMessage);
			consoleMessage(getPrefix() + "&f상태 메세지(비활성화): " + statusDisableMessage);
		}
	}

	private static void update() {

	}
	
	private void registerCommand(String command, CommandExecutor executor) {
		Bukkit.getPluginCommand(command).setExecutor(executor);		
	}
	
	private void registerEvent(Listener lisener) {
		Bukkit.getPluginManager().registerEvents(lisener, this);
	}
	
	public static void addPlayer(Player player) {
		MessageSender.playerMessage(player, KongBlocks.enableMessage);
		KongBlocks.enabled.add(player.getUniqueId().toString());
	}

	public static void removePlayer(Player player) {
		MessageSender.playerMessage(player, KongBlocks.disableMessage);
		KongBlocks.enabled.remove(player.getUniqueId().toString());
	}

	public static void addBlock(BlockObject block, Player player) {
		MessageSender.consoleMessage("블럭을 추가함!");
		map.put(block, player.getUniqueId().toString());
	}

	public static void removeBlock(BlockObject block, Player player) {
		MessageSender.consoleMessage("블럭을 제거함!");
		map.remove(block, player.getUniqueId().toString());
	}

	public static String getPrefix() {
		return prefix;
	}
}