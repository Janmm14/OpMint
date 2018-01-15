package de.janmm14.opmint.commands;

import de.janmm14.opmint.Controller;
import io.gomint.command.Command;
import io.gomint.command.CommandOutput;
import io.gomint.command.annotation.Alias;
import io.gomint.command.annotation.Description;
import io.gomint.command.annotation.Name;
import io.gomint.command.annotation.Overload;
import io.gomint.command.annotation.Parameter;
import io.gomint.command.annotation.Permission;
import io.gomint.command.validator.StringValidator;
import io.gomint.command.validator.TargetValidator;
import io.gomint.entity.EntityPlayer;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.UUID;

public class OpCommand extends Command {

	private final Controller controller;

	public OpCommand(Controller controller) {
		super("op");
		alias("opmint");
		description("Promote a player to operator status");
		permission("opmint.op");
		overload().param("targetPlayer", new TargetValidator());

		this.controller = controller;
	}

	@Override
	public CommandOutput execute(EntityPlayer player, String alias, Map<String, Object> args) {
		Object targetPlayerObj = args.get("targetPlayer");
		if (targetPlayerObj != null && targetPlayerObj instanceof EntityPlayer) {
			EntityPlayer target = (EntityPlayer) targetPlayerObj;
			String name = target.getName();
			UUID uuid = target.getUUID();
			if (controller.addOp(target)) {
				return new CommandOutput().success("Successfully promoted %s (%s) to operator status.", name, uuid);
			}
			return new CommandOutput().fail("%s (%s) is already operator.", name, uuid);
		}
		return new CommandOutput().fail("Invalid / no argument provided.");
	}
}
