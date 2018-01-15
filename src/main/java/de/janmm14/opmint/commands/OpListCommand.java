package de.janmm14.opmint.commands;

import de.janmm14.opmint.Controller;
import de.janmm14.opmint.config.OpMintConfigEntry;
import io.gomint.command.Command;
import io.gomint.command.CommandOutput;
import io.gomint.entity.EntityPlayer;

import java.util.List;
import java.util.Map;

public class OpListCommand extends Command {

	private final Controller controller;

	public OpListCommand(Controller controller) {
		super("oplist");
		alias("ops");
		description("List players with operator status");
		permission("opmint.oplist");

		this.controller = controller;
	}

	@Override
	public CommandOutput execute(EntityPlayer player, String alias, Map<String, Object> args) {
		List<OpMintConfigEntry> op = controller.getConfig().getOp();
		StringBuilder sb = new StringBuilder(op.size() * (16 + 36));
		for (OpMintConfigEntry entry : op) {
			sb.append("§f").append(entry.getLastKnownName()).append(" (").append(entry.getUuid()).append("),\n");
		}
		return new CommandOutput().success("%s", sb.substring(0, sb.length() - 1));
	}
}
