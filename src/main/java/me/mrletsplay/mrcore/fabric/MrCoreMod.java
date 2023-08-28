package me.mrletsplay.mrcore.fabric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class MrCoreMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

//		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("hello")
//			.then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("args", StringArgumentType.greedyString())
//				.executes(context -> {
//				context.getSource().sendMessage(Text.literal("Hello World: " + context.getInput()));
//				return 1;
//		}))
//			.executes(context -> {
//				context.getSource().sendMessage(Text.literal("Hello World: " + context.getInput()));
//				return 1;
//		})));

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(new TestCommand().create()));
	}

}
