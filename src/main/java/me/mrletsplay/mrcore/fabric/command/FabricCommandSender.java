package me.mrletsplay.mrcore.fabric.command;

import me.mrletsplay.mrcore.command.CommandSender;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class FabricCommandSender implements CommandSender {

	private ServerCommandSource source;

	public FabricCommandSender(ServerCommandSource source) {
		this.source = source;
	}

	@Override
	public void sendMessage(String message) {
		source.sendMessage(Text.literal(message));
	}

	public void sendMessage(Text message) {
		source.sendMessage(message);
	}

	public ServerCommandSource getSource() {
		return source;
	}

	public ServerPlayerEntity asPlayer() {
		return source.isExecutedByPlayer() ? source.getPlayer() : null;
	}

}
