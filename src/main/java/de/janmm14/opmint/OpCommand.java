package de.janmm14.opmint;

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

import java.util.Map;

@Name("op")
@Alias("opmint")
@Description("Promote a player to operator status")
@Permission("opmint.command")
@Overload(@Parameter(name = "targetPlayer", validator = TargetValidator.class))
@Overload(@Parameter(name = "targetUuid", validator = StringValidator.class, arguments = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}")) //uuid regex
public class OpCommand extends Command{

	@Override
	public CommandOutput execute(EntityPlayer entityPlayer, String s, Map<String, Object> map) {
		return null;
	}
}
