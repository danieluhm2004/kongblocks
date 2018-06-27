package main.java.kr.mcraft.kongblocks.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.kr.mcraft.kongblocks.KongBlocks;
import main.java.kr.mcraft.kongblocks.utils.MessageSender;

public class ProtectCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("보호")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(args.length == 0) {
					MessageSender.playerMessage(player, KongBlocks.getPrefix() + "&6# 보호 명령어 도움말입니다!");
					MessageSender.playerMessage(player, KongBlocks.getPrefix() + "&9/보호  &f- 명령어 도움말을 확인합니다.");
					MessageSender.playerMessage(player, KongBlocks.getPrefix() + "&9/보호 &6변경 &f- 보호를 활성화하거나 비활성화합니다.");
					MessageSender.playerMessage(player, KongBlocks.getPrefix() + "&9/보호 &6정보 &f- 해당 블럭의 정보를 확인합니다.");
					MessageSender.playerMessage(player, KongBlocks.getPrefix() + "&9/보호 &6리뷰 <메세지> &f- 해당 플러그인을 리뷰합니다.");
					MessageSender.playerMessage(player, KongBlocks.getPrefix() + "&9/보호 &6저장 &f- 블럭 데이터를 저장합니다. &c&l(OP)");
				} else if(args[0].equalsIgnoreCase("변경")) {
					if(!KongBlocks.enabled.contains(player.getUniqueId().toString())) {
						MessageSender.playerMessage(player, KongBlocks.getPrefix() + KongBlocks.enableMessage);
						KongBlocks.enabled.add(player.getUniqueId().toString());
					} else {
						MessageSender.playerMessage(player, KongBlocks.getPrefix() + KongBlocks.disableMessage);
						KongBlocks.enabled.remove(player.getUniqueId().toString());
					}
				} else if(args[0].equalsIgnoreCase("정보")) {
					if(KongBlocks.informationEnable) {
						if(!KongBlocks.information.contains(player)) {
							KongBlocks.information.add(player);
							MessageSender.playerMessage(player, KongBlocks.getPrefix() + KongBlocks.informationEnableMessage);
						} else {
							KongBlocks.information.remove(player);
							MessageSender.playerMessage(player, KongBlocks.getPrefix() + KongBlocks.informationDisableMessage);
						}
					} else {
						MessageSender.playerMessage(player, KongBlocks.getPrefix() + KongBlocks.informationNotSupportMessage);
					}
				} else if(args[0].equalsIgnoreCase("리뷰")) {
					if(KongBlocks.reviewEnable) {
						String message = new String();
						if(args.length >= 2) {
							for(int i = 1; i <= (args.length - 1); i++) {
								message += args[i];
							}
							KongBlocks.review(player, message);
							MessageSender.playerMessage(player, KongBlocks.getPrefix() + KongBlocks.reviewThanksMessage);
						} else {
							MessageSender.playerMessage(player, KongBlocks.getPrefix() + KongBlocks.reviewBlankMessage);
						}
					} else {
						MessageSender.playerMessage(player, KongBlocks.getPrefix() + KongBlocks.reviewNotSupportMessage);
					}
				} else if(args[0].equalsIgnoreCase("저장")) {
					if(player.isOp()) {
						try {
							KongBlocks.saveDatabase();
							if(KongBlocks.autoSaveMessageEnable) {
								MessageSender.playerMessage(player, KongBlocks.getPrefix() + KongBlocks.saveMessage);
							}
						} catch (Exception e) {
							MessageSender.playerMessage(player, KongBlocks.getPrefix() + "다음 &c오류&f로 인하여 &e파일&f을 &6저장&f할 수 없습니다. " + e.getMessage());
						}
					} else {
						MessageSender.playerMessage(player, KongBlocks.getPrefix() + KongBlocks.permissionMessage);
					}
				}
				KongBlocks.randomReview(player);
			} else {
				MessageSender.consoleMessage(KongBlocks.getPrefix() + "&6콘솔&f에서는 &e작동&f하지 &c않습니다!");
			}
		}
		return true;
	}

}
