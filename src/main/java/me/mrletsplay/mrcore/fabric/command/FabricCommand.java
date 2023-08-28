package me.mrletsplay.mrcore.fabric.command;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

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
import me.mrletsplay.mrcore.command.parser.CommandParser;
import me.mrletsplay.mrcore.command.parser.CommandParsingException;
import me.mrletsplay.mrcore.command.provider.CommandProvider;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public abstract class FabricCommand extends AbstractCommand<FabricCommandProperties> implements CommandProvider, Command<ServerCommandSource>, SuggestionProvider<ServerCommandSource> {

	private CommandParser parser;

	public FabricCommand(String name, FabricCommandProperties initialProperties) {
		super(name, initialProperties);
		this.parser = new CommandParser(this);

//		parser.getParsingProperties().setTabCompleteCommandFilter(event -> {
//			return ((FabricCommand) event.getObject()).checkPermission(((FabricCommandSender) event.getSender()).getBukkitSender());
//		});
//
//		parser.getParsingProperties().setTabCompleteOptionFilter(event -> {
//			return ((FabricCommand) event.getCommand()).checkPermission(((FabricCommandSender) event.getSender()).getBukkitSender());
//		});
//
//		parser.getParsingProperties().setTabCompleteArgumentFilter(event -> {
//			return ((FabricCommand) event.getCommand()).checkPermission(((FabricCommandSender) event.getSender()).getBukkitSender());
//		});
//
//		parser.getParsingProperties().setTabCompleteOptionArgumentFilter(event -> {
//			return ((FabricCommand) event.getCommand()).checkPermission(((FabricCommandSender) event.getSender()).getBukkitSender());
//		});
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
	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		try {
			invoke(new FabricCommandSender(context.getSource()), context.getInput());
		}catch(CommandParsingException e) {
			e.printStackTrace(); // TODO
		}
		return 1;
	}

	@Override
	public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
		List<String> suggestions = tabComplete(new FabricCommandSender(context.getSource()), context.getInput().substring(1));

		builder = builder.createOffset(builder.getInput().length());

		for(String s : suggestions) builder.suggest(s);
		return builder.buildFuture();
	}

	@Override
	public void sendCommandInfo(me.mrletsplay.mrcore.command.CommandSender sender) {
		FabricCommandSender s = (FabricCommandSender) sender;

		s.sendMessage(Text.literal("Command: ").formatted(Formatting.GOLD)
			.append(Text.literal(getFullName()).formatted(Formatting.WHITE)));

		if(getDescription() != null) {
			s.sendMessage(Text.literal("Description: ").formatted(Formatting.GOLD)
				.append(Text.literal(getDescription()).formatted(Formatting.WHITE)));
		}

		if(!getOptions().isEmpty()) {
			s.sendMessage(Text.literal("Available options: ").formatted(Formatting.GOLD)
				.append(Text.literal(getOptions().stream().map(o -> "--" + o.getLongName()).collect(Collectors.joining(", "))).formatted(Formatting.WHITE)));
		}

		if(getUsage() != null) {
			s.sendMessage(Text.literal("Usage: ").formatted(Formatting.GOLD)
				.append(Text.literal(getUsage()).formatted(Formatting.WHITE)));
		}

		if(!getSubCommands().isEmpty()) {
			s.sendMessage(Text.literal(""));
			s.sendMessage(Text.literal("Sub commands: ").formatted(Formatting.GOLD));
			for(me.mrletsplay.mrcore.command.Command sub : getSubCommands()) {
				var subT = Text.literal(sub.getUsage() == null ? "/" + sub.getFullName() : sub.getUsage()).formatted(Formatting.GRAY);
				if(sub.getDescription() != null) {
					subT = subT.append(Text.literal(" - ").formatted(Formatting.DARK_GRAY))
						.append(Text.literal(sub.getDescription()).formatted(Formatting.WHITE));
				}

				s.sendMessage(subT);
			}
		}
	}

	public LiteralArgumentBuilder<ServerCommandSource> create() {
		return literal(getName())
			.executes(this)
			.then(argument("args", StringArgumentType.greedyString()).executes(this).suggests(this));
	}

}
