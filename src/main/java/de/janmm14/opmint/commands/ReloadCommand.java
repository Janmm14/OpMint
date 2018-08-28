package de.janmm14.opmint.commands;

import de.janmm14.opmint.Controller;
import io.gomint.command.Command;
import io.gomint.command.CommandOutput;
import io.gomint.command.CommandSender;

import java.util.Map;

public class ReloadCommand extends Command {

	private final Controller controller;

	public ReloadCommand(Controller controller) {
		super("opmintreload");
		description("Reload OpMint config file");
		permission("opmint.reload");

		this.controller = controller;
	}

	@Override
	public CommandOutput execute(CommandSender sender, String alias, Map<String, Object> args) {
		controller.initialize();
		return new CommandOutput().success("OpMint config file reloaded.");
	}
}
