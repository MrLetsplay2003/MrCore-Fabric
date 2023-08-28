package me.mrletsplay.mrcore.fabric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class MrCoreMod implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("mrcore");

	@Override
	public void onInitialize() {
		LOGGER.info("And MrCore is on board as well! :wave:");

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(new TestCommand().create()));
	}

}
