package de.janmm14.opmint.config;

import com.blackypaw.simpleconfig.SimpleConfig;
import io.gomint.entity.EntityPlayer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode(of = "uuid", callSuper = false)
@RequiredArgsConstructor
public class OpMintConfigEntry extends SimpleConfig {

	private final UUID uuid;
	private final String lastKnownName;

	public static OpMintConfigEntry ofPlayer(EntityPlayer player) {
		return new OpMintConfigEntry(player.getUUID(), player.getName());
	}
}
