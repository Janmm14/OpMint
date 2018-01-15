package de.janmm14.opmint.commands;

import de.janmm14.opmint.OpMint;
import io.gomint.command.Command;
import io.gomint.command.CommandOutput;
import io.gomint.command.validator.StringValidator;
import io.gomint.entity.EntityPlayer;

import java.util.Map;

public class SetupCommand extends Command {

	private final OpMint plugin;

	public SetupCommand(OpMint plugin) {
		super("opmintsetup");
		description("gain op through entering the config secret");
		overload().param("secret", new StringValidator(".+"));
		this.plugin = plugin;
	}

	@Override
	public CommandOutput execute(EntityPlayer player, String alias, Map<String, Object> args) {
		if (!plugin.getController().getConfig().isSetupEnabled()) {
			return new CommandOutput().fail("Setup disabled");
		}
		Object secret = args.get("secret");
		if (secret == null || !(secret instanceof String)) {
			return new CommandOutput().fail("Missing argument: secret");
		}
		if (secret.equals(plugin.getController().getConfig().getConfigSecret())) {
			plugin.getController().getConfig().setSetupEnabled(false);
			if (plugin.getController().addOp(player)) {
				return new CommandOutput().success("You gained op. Disabled setup mode.");
			} else {
				return new CommandOutput().fail("You aleady have op. Disabled setup mode.");
			}
		}
		return new CommandOutput().fail("Wrong secret.");
	}
}