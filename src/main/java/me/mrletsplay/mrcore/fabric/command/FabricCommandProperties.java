package me.mrletsplay.mrcore.fabric.command;

import java.util.function.Predicate;

import me.mrletsplay.mrcore.command.properties.CommandProperties;
import net.minecraft.server.command.ServerCommandSource;

public class FabricCommandProperties implements CommandProperties {

	private Predicate<ServerCommandSource> requires;

	public FabricCommandProperties() {
		this.requires = s -> true;
	}

	public void setRequires(Predicate<ServerCommandSource> requires) {
		this.requires = requires;
	}

	public Predicate<ServerCommandSource> getRequires() {
		return requires;
	}

}
