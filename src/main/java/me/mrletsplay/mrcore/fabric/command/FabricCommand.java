package me.mrletsplay.mrcore.fabric.command;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import me.mrletsplay.mrcore.command.AbstractCommand;
import me.mrletsplay.mrcore.command.CommandSender;
import me.mrletsplay.mrcore.command.event.CommandInvokedEvent;
import me.mrletsplay.mrcore.command.parser.CommandParser;
import me.mrletsplay.mrcore.command.parser.CommandParsingException;
import me.mrletsplay.mrcore.command.provider.CommandProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public abstract class FabricCommand extends AbstractCommand<FabricCommandProperties> implements CommandProvider, Command<CommandSourceStack>, SuggestionProvider<CommandSourceStack> {

	private CommandParser parser;

	public FabricCommand(String name, FabricCommandProperties initialProperties) {
		super(name, initialProperties);
		this.parser = new CommandParser(this);
	}

	public FabricCommand(String name) {
		this(name, new FabricCommandProperties());
	}

	@Override
	public List<FabricCommand> getCommands() {
		return Collections.singletonList(this);
	}

	@Override
	public CommandParser getCommandParser() {
		return parser;
	}

	@Override
	public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		try {
			invoke(new FabricCommandSender(context.getSource()), context.getInput());
		}catch(CommandParsingException e) {
			context.getSource().sendSystemMessage(Component.literal("§cError: §7" + e.getMessage()));
		}
		return 1;
	}

	@Override
	public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) throws CommandSyntaxException {
		List<String> suggestions = tabComplete(new FabricCommandSender(context.getSource()), context.getInput().substring(1));

		builder = builder.createOffset(builder.getInput().length());

		for(String s : suggestions) builder.suggest(s);
		return builder.buildFuture();
	}

	@Override
	public void sendCommandInfo(CommandSender sender) {
		FabricCommandSender s = (FabricCommandSender) sender;

		s.sendMessage(Component.literal("Command: ").withStyle(ChatFormatting.GOLD)
			.append(Component.literal(getFullName()).withStyle(ChatFormatting.WHITE)));

		if(getDescription() != null) {
			s.sendMessage(Component.literal("Description: ").withStyle(ChatFormatting.GOLD)
				.append(Component.literal(getDescription()).withStyle(ChatFormatting.WHITE)));
		}

		if(!getOptions().isEmpty()) {
			s.sendMessage(Component.literal("Available options: ").withStyle(ChatFormatting.GOLD)
				.append(Component.literal(getOptions().stream().map(o -> "--" + o.getLongName()).collect(Collectors.joining(", "))).withStyle(ChatFormatting.WHITE)));
		}

		if(getUsage() != null) {
			s.sendMessage(Component.literal("Usage: ").withStyle(ChatFormatting.GOLD)
				.append(Component.literal(getUsage()).withStyle(ChatFormatting.WHITE)));
		}

		if(!getSubCommands().isEmpty()) {
			s.sendMessage(Component.literal(""));
			s.sendMessage(Component.literal("Sub commands: ").withStyle(ChatFormatting.GOLD));
			for(me.mrletsplay.mrcore.command.Command sub : getSubCommands()) {
				var subT = Component.literal(sub.getUsage() == null ? "/" + sub.getFullName() : sub.getUsage()).withStyle(ChatFormatting.GRAY);
				if(sub.getDescription() != null) {
					subT = subT.append(Component.literal(" - ").withStyle(ChatFormatting.DARK_GRAY))
						.append(Component.literal(sub.getDescription()).withStyle(ChatFormatting.WHITE));
				}

				s.sendMessage(subT);
			}
		}
	}

	public LiteralArgumentBuilder<CommandSourceStack> create() {
		return Commands.literal(getName())
			.requires(getProperties().getRequires())
			.executes(this)
			.then(Commands.argument("args", StringArgumentType.greedyString())
				.requires(getProperties().getRequires())
				.executes(this)
				.suggests(this));
	}

	protected boolean isSenderPlayer(CommandInvokedEvent event) {
		return event.getSender() instanceof FabricCommandSender
				&& ((FabricCommandSender) event.getSender()).getSource().isPlayer();
	}

	protected ServerPlayer getSenderPlayer(CommandInvokedEvent event) {
		return ((FabricCommandSender) event.getSender()).asPlayer();
	}

	protected boolean isSenderConsole(CommandInvokedEvent event) {
		return event.getSender() instanceof FabricCommandSender
				&& !((FabricCommandSender) event.getSender()).getSource().isPlayer();
	}

}
