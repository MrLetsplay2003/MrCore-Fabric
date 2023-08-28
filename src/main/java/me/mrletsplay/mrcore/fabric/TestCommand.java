package me.mrletsplay.mrcore.fabric;

import java.util.Arrays;

import me.mrletsplay.mrcore.command.event.CommandInvokedEvent;
import me.mrletsplay.mrcore.command.option.CommandOption;
import me.mrletsplay.mrcore.fabric.command.FabricCommand;

public class TestCommand extends FabricCommand {

	private static final CommandOption<?> HELP = createCommandOption("h", "help");

	public TestCommand() {
		super("test");

		setDescription("EEE");
		setTabCompleter(event -> {
			return Arrays.asList("Hello", "World");
		});

		addOption(HELP);
	}

	@Override
	public void action(CommandInvokedEvent event) {
		if(event.isOptionPresent(HELP)) {
			sendCommandInfo(event.getSender());
			return;
		}

		event.getSender().sendMessage(Arrays.toString(event.getArguments()));
	}

}
