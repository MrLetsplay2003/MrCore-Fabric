package me.mrletsplay.mrcore.fabric.command;

import java.util.function.Predicate;

import me.mrletsplay.mrcore.command.properties.CommandProperties;
import net.minecraft.commands.CommandSourceStack;

public class FabricCommandProperties implements CommandProperties {

	private Predicate<CommandSourceStack> requires;

	public FabricCommandProperties() {
		this.requires = _ -> true;
	}

	public void setRequires(Predicate<CommandSourceStack> requires) {
		this.requires = requires;
	}

	public Predicate<CommandSourceStack> getRequires() {
		return requires;
	}

}
