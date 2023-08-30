package me.mrletsplay.mrcore.fabric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;

public class MrCoreMod implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("mrcore");

	@Override
	public void onInitialize() {
		LOGGER.info("And MrCore is on board as well! :wave:");
	}

}
