package de.janmm14.opmint.commands;

import de.janmm14.opmint.Controller;
import io.gomint.command.Command;
import io.gomint.command.CommandOutput;
import io.gomint.command.validator.StringValidator;
import io.gomint.command.validator.TargetValidator;
import io.gomint.entity.EntityPlayer;

import java.util.Map;
import java.util.UUID;

public class DeopCommand extends Command {

	private final Controller controller;

	public DeopCommand(Controller controller) {
		super("deop");
		alias("deopmint");
		description("Remove operator status of a player");
		permission("opmint.deop");
		overload().param("targetPlayer", new TargetValidator());
		overload().param("targetUuid", new StringValidator("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}" /* uuid regex */));
		overload().param("targetName", new StringValidator(".{1,40}"));

		this.controller = controller;
	}

	@Override
	public CommandOutput execute(EntityPlayer player, String alias, Map<String, Object> args) {
		Object targetPlayerObj = args.get("targetPlayer");
		if (targetPlayerObj != null && targetPlayerObj instanceof EntityPlayer) {
			EntityPlayer target = (EntityPlayer) targetPlayerObj;
			String name = target.getName();
			UUID playerUuid = target.getUUID();
			if (controller.removeOp(target)) {
				return new CommandOutput().success("Successfully removed operator status of %s (%s).", name, playerUuid);
			}
			return new CommandOutput().fail("%s (%s) is no operator.", name, playerUuid);
		}
		Object targetUuidObj = args.get("targetUuid");
		if (targetUuidObj != null && targetUuidObj instanceof String) {
			try {
				UUID playerUuid = UUID.fromString((String) targetUuidObj);
				String name = controller.removeOp(playerUuid);
				if (name != null) {
					return new CommandOutput().success("Successfully removed operator status of %s (%s).", name, playerUuid);
				}
				return new CommandOutput().fail("? (%s) is no operator.", playerUuid);
			} catch (IllegalArgumentException e) {
				controller.getPlugin().getLogger().debug("Command /op executed by " + player.getName() + " failed at uuid parsing, input: " + targetUuidObj, e);
				return new CommandOutput().fail("Invalid uuid: %s", targetUuidObj);
			}
		}
		Object targetNameObj = args.get("targetName");
		if (targetNameObj != null && targetNameObj instanceof String) {
			String name = (String) targetNameObj;
			UUID playerUuid = controller.removeOp(name);
			if (playerUuid != null) {
				return new CommandOutput().success("Successfully removed operator status of %s (%s).", name, playerUuid);
			}
			return new CommandOutput().fail("%s (?) is no operator.", name);
		}
		return new CommandOutput().fail("Invalid / no argument provided.");
	}
}
