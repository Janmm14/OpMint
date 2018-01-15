package de.janmm14.opmint;

import io.gomint.command.Command;
import io.gomint.command.CommandOutput;
import io.gomint.command.annotation.Alias;
import io.gomint.command.annotation.Description;
import io.gomint.command.annotation.Name;
import io.gomint.command.annotation.Overload;
import io.gomint.command.annotation.Parameter;
import io.gomint.command.annotation.Permission;
import io.gomint.command.validator.TargetValidator;
import io.gomint.entity.EntityPlayer;

import java.util.Map;

@Name("op")
@Alias("opmint")
@Description("Promote a player to operator status")
@Permission("opmint.command")
@Overload(@Parameter(name = "targetPlayer", validator = TargetValidator.class))
public class DeopCommand extends Command{

	@Override
	public CommandOutput execute(EntityPlayer player, String alias, Map<String, Object> args) {
		return null;
	}
}
