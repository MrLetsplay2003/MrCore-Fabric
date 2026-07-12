package me.mrletsplay.mrcore.fabric.command;

import me.mrletsplay.mrcore.command.CommandSender;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class FabricCommandSender implements CommandSender {

	private CommandSourceStack source;

	public FabricCommandSender(CommandSourceStack source) {
		this.source = source;
	}

	@Override
	public void sendMessage(String message) {
		source.sendSystemMessage(Component.literal(message));
	}

	public void sendMessage(Component message) {
		source.sendSystemMessage(message);
	}

	public CommandSourceStack getSource() {
		return source;
	}

	public ServerPlayer asPlayer() {
		return source.isPlayer() ? source.getPlayer() : null;
	}

}
