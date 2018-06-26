package main.java.kr.mcraft.kongblocks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import main.java.kr.mcraft.kongblocks.commands.ProtectCommand;
import main.java.kr.mcraft.kongblocks.events.BlockBreak;
import main.java.kr.mcraft.kongblocks.events.BlockPlace;
import main.java.kr.mcraft.kongblocks.events.PlayerInteract;
import main.java.kr.mcraft.kongblocks.events.PlayerJoin;
import main.java.kr.mcraft.kongblocks.objects.BlockObject;
import main.java.kr.mcraft.kongblocks.timers.AutoSaveTimer;
import main.java.kr.mcraft.kongblocks.timers.NoticeTimer;
import main.java.kr.mcraft.kongblocks.timers.StatusTimer;
import main.java.kr.mcraft.kongblocks.utils.MessageSender;

import static main.java.kr.mcraft.kongblocks.utils.MessageSender.*;

public class KongBlocks extends JavaPlugin {
	private static Plugin plugin;

	// Settings
	public static String prefix;
	public static String version;
	public static boolean autoSaveEnable = true;
	public static int autoSaveTime = 3000;
	public static boolean autoSaveMessageEnable = false;
	public static boolean reviewEnable = true;
	public static boolean noticeEnable = true;
	public static int noticeTime = 1200;
	public static boolean statusEnable = true;
	public static int statusTime = 1200;
	public static boolean informationEnable = true;
	public static String database = "players.db";
	public static String databaseTemp = "players.tmp.db";

	// Message
	public static String saveMessage;
	public static String enableMessage;
	public static String disableMessage;
	public static String informationMessage;
	public static String informationNoneMessage;
	public static String informationEnableMessage;
	public static String informationDisableMessage;
	public static String informationNotSupportMessage;
	public static String hasOwnerMessage;
	public static String permissionMessage;
	public static String noticeMessage;
	public static String reviewMessage;
	public static String reviewThanksMessage;
	public static String reviewBlankMessage;
	public static String reviewNotSupportMessage;
	public static String statusEnableMessage;
	public static String statusDisableMessage;

	// Other
	public static File config = new File("plugins" + File.separator + "KongBlocks" + File.separator + "config.yml");
	public static File databaseFile = new File("plugins" + File.separator + "KongBlocks" + File.separator + database);
	public static File databaseTempFile = new File(
			"plugins" + File.separator + "KongBlocks" + File.separator + databaseTemp);
	public static HashMap<BlockObject, String> databaseMap = new HashMap<BlockObject, String>();
	public static ArrayList<String> enabled = new ArrayList<String>();
	public static ArrayList<Player> information = new ArrayList<>();

	@Override
	public void onEnable() {
		try {
			plugin = this;
			version = getDescription().getVersion();
			if (!config.exists()) {
				setup();
			}
			setConfig();
			if(!version.equalsIgnoreCase(getDescription().getVersion())) {
				consoleMessage(getPrefix() + "&e설정 파일 &e버전&f이 다릅니다. &c초기화&f합니다.");
				getConfig().set("", null);
				setup();
				setConfig();
			}
			loadDatabase();
			consoleMessage(getPrefix() + "&a플러그인&f이 &a활성화&f중입니다!");
			consoleMessage(getPrefix() + "&9M&fcraft 에서 &e제작&f되었습니다. XD"); // 해당 내용은 수정하실 수 없습니다.
			update();
			if(autoSaveEnable) {
				AutoSaveTimer.start(autoSaveTime);
			}
			if(noticeEnable) {
				NoticeTimer.start(noticeTime);
			}
			if(statusEnable) {
				StatusTimer.start(statusTime);
			}
			registerCommand("보호", new ProtectCommand());
			registerEvent(new BlockBreak());
			registerEvent(new BlockPlace());
			registerEvent(new PlayerInteract());
			registerEvent(new PlayerJoin());
		} catch (Exception e) {
			e.printStackTrace();
			consoleMessage(getPrefix() + "다음 &c오류&f로 인하여 &e플러그인이 &c강제 종료&f되었습니다.");
			getPluginLoader().disablePlugin(this);
		}
	}

	@Override
	public void onDisable() {
		try {
			consoleMessage(getPrefix() + "&a플러그인&f이 &c비활성화&f중입니다!");
			consoleMessage(getPrefix() + "&9M&fcraft 에서 &e제작&f되었습니다. XD"); // 해당 내용은 수정하실 수 없습니다.
			getConfig().set("enabled", enabled);
			saveConfig();
			saveDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setup() throws Exception {
		config.getParentFile().mkdirs();

		// Settings
		getConfig().set("settings.version", version);
		getConfig().set("settings.autoSave", true);
		getConfig().set("settings.autoSaveTime", 1200);
		getConfig().set("settings.autoSaveMessageEnable", false);
		getConfig().set("settings.reviewEnable", true);
		getConfig().set("settings.noticeEnable", true);
		getConfig().set("settings.noticeTime", 3000);
		getConfig().set("settings.statusEnable", true);
		getConfig().set("settings.statusTime", 1200);
		getConfig().set("settings.informationEnable", true);
		getConfig().set("settings.database", "players.db");
		getConfig().set("settings.databaseTemp", "players.temp.db");

		// Message
		getConfig().set("messages.prefix", "&f[ &9KongBlocks &f] &f");
		getConfig().set("messages.saveMessage", "&e블럭 &e데이터&f를 &6저장&f하였습니다!");
		getConfig().set("messages.enableMessage", "&6보호 &e기능&f이 &a활성화&f되었습니다. &c비활성화&f하려면 &6/보호 &f를 &b입력&f하세요!");
		getConfig().set("messages.disableMessage", "&6보호 &e기능&f이 &c비활성화&f되었습니다. &a활성화&f하려면 &6/보호 &f를 &b입력&f하세요!");
		getConfig().set("messages.informationEnableMessage", "&6정보 확인 &e기능&f이 &a활성화&f되었습니다. &6확인&f할 &9블럭&f을 &e클릭&f하세요!");
		getConfig().set("messages.informationMessage", "&f해당 블럭의 &e소유자&f는 &6<player>&f님입니다.");
		getConfig().set("messages.informationNoneMessage", "&f해당 블럭의 &e소유자&f는 &c없습니다.");
		getConfig().set("messages.informationDisableMessage", "&6정보 확인 &e기능&f이 &c비활성화&f되었습니다.");
		getConfig().set("messages.informationNotSupportMessage", "&f해당 서버는 &6정보 확인 &e기능&f를 사용할 수 없습니다.");
		getConfig().set("messages.hasOwnerMessage", "&f해당 블럭의 &e소유자&f는 &6<player>&f님이므로 &c파괴&f할 수 없습니다.");
		getConfig().set("messages.permissionMessage", "&f해당 &6명령어&f를 &e입력할 &b권한&f이 &c없습니다.");
		getConfig().set("messages.noticeMessage", "&f해당 &e서버&f는 &9공블럭&f을 &a사용&f하여 <size>개의 블럭을 보호하고 있습니다!");
		getConfig().set("messages.reviewMessage",
				"&9공블럭&f가 &6문제&f가 생기거나 &6괜찮은 아이디어&f가 있다면 &6/보호 리뷰 <메세지> &f를 입력해주세요! &c(다만 욕설 방지를 위해 아이피와 닉네임만 저장됩니다. 연속적으로 가능합니다.)");
		getConfig().set("messages.reviewThanksMessage", "&f해당 &e내용&f은 &c&l암호화&f되어 &a전송&f되었습니다. &f&l소중한 의견 감사합니다!");
		getConfig().set("messages.reviewBlankMessage", "&6내용&f을 입력해주세요!");
		getConfig().set("messages.reviewNotSupportMessage", "&f해당 서버는 &6리뷰 &e기능&f를 사용할 수 없습니다.");
		getConfig().set("messages.statusEnableMessage", "&f현재 &6보호 &e기능&f이 &a활성화&f된 상태입니다.");
		getConfig().set("messages.statusDisableMessage", "&f현재 &6보호 &e기능&f이 &c비활성화&f된 상태입니다.");

		saveConfig();
	}

	private void setConfig() {
		for (String uuid : getConfig().getStringList("enabled")) {
			enabled.add(uuid);
		}

		// Settings
		version = getConfig().getString("settings.version");
		autoSaveEnable = getConfig().getBoolean("settings.autoSaveEnable");
		autoSaveTime = getConfig().getInt("settings.autoSaveTime");
		autoSaveMessageEnable = getConfig().getBoolean("settings.autoSaveMessageEnable");
		autoSaveMessageEnable = getConfig().getBoolean("settings.autoSaveMessageEnable");
		reviewEnable = getConfig().getBoolean("settings.reviewEnable");
		noticeEnable = getConfig().getBoolean("settings.noticeEnable");
		noticeTime = getConfig().getInt("settings.noticeTime");
		statusEnable = getConfig().getBoolean("settings.statusEnable");
		statusTime = getConfig().getInt("settings.statusTime");
		informationEnable = getConfig().getBoolean("settings.informationEnable");
		database = getConfig().getString("settings.database");
		databaseTemp = getConfig().getString("settings.databaseTemp");
		
		// Message
		prefix = getConfig().getString("messages.prefix");
		saveMessage = getConfig().getString("messages.saveMessage");
		enableMessage = getConfig().getString("messages.enableMessage");
		disableMessage = getConfig().getString("messages.disableMessage");
		informationMessage = getConfig().getString("messages.informationMessage");
		informationNoneMessage = getConfig().getString("messages.informationNoneMessage");
		informationEnableMessage = getConfig().getString("messages.informationEnableMessage");
		informationDisableMessage = getConfig().getString("messages.informationDisableMessage");
		informationNotSupportMessage = getConfig().getString("messages.informationNotSupportMessage");
		hasOwnerMessage = getConfig().getString("messages.hasOwnerMessage");
		permissionMessage = getConfig().getString("messages.permissionMessage");
		noticeMessage = getConfig().getString("messages.noticeMessage");
		reviewMessage = getConfig().getString("messages.reviewMessage");
		reviewThanksMessage = getConfig().getString("messages.reviewThanksMessage");
		reviewBlankMessage = getConfig().getString("messages.reviewBlankMessage");
		reviewNotSupportMessage = getConfig().getString("messages.reviewNotSupportMessage");
		statusEnableMessage = getConfig().getString("messages.statusEnableMessage");
		statusDisableMessage = getConfig().getString("messages.statusDisableMessage");

		databaseFile = new File("plugins" + File.separator + "KongBlocks" + File.separator + database);
		databaseTempFile = new File("plugins" + File.separator + "KongBlocks" + File.separator + databaseTemp);
	}

	private static void update() {

	}
	
	public static void review(Player player, String message) {
		String ip = player.getAddress().getHostName();
		String name = player.getName();
		
	}

	@SuppressWarnings("unchecked")
	public static void loadDatabase() throws Exception {
		if (databaseFile.exists()) {
			FileInputStream input = new FileInputStream(databaseFile);
			ObjectInputStream objectInput = new ObjectInputStream(input);
			databaseMap = (HashMap<BlockObject, String>) objectInput.readObject();
			objectInput.close();
			input.close();
		} else {
			databaseMap = new HashMap<BlockObject, String>();
		}
	}

	public static void saveDatabase() throws Exception {
		FileOutputStream output = new FileOutputStream(databaseTempFile);
		ObjectOutputStream objectOutput = new ObjectOutputStream(output);
		objectOutput.writeObject(databaseMap);
		objectOutput.close();
		output.close();
		databaseFile.delete();
		databaseTempFile.renameTo(databaseFile);
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
		databaseMap.put(block, player.getUniqueId().toString());
	}

	public static void removeBlock(BlockObject block, Player player) {
		databaseMap.remove(block, player.getUniqueId().toString());
	}
	
	public static void randomReview(Player player) {
		Random random = new Random();
		if(random.nextInt(19) == 0) {
			MessageSender.playerMessage(player, getPrefix() + reviewMessage);
		}
	}

	public static Plugin getInstance() {
		return plugin;
	}

	public static String getPrefix() {
		return prefix;
	}
}