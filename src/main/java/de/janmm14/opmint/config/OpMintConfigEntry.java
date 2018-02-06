package de.janmm14.opmint.config;

import com.blackypaw.simpleconfig.SimpleConfig;
import io.gomint.entity.EntityPlayer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = "uuid", callSuper = false)
public class OpMintConfigEntry extends SimpleConfig {

	private String uuid;
	@Getter
	private String lastKnownName;

	public OpMintConfigEntry(UUID uuid, String lastKnownName) {
		this.uuid = uuid.toString();
		this.lastKnownName = lastKnownName;
	}

	public static OpMintConfigEntry ofPlayer(EntityPlayer player) {
		return new OpMintConfigEntry(player.getUUID(), player.getName());
	}

	public UUID getUuid() {
		return UUID.fromString(this.uuid);
	}
}
