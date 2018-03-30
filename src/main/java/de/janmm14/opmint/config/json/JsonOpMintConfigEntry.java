package de.janmm14.opmint.config.json;

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
public class JsonOpMintConfigEntry extends SimpleConfig {

	private String uuid;
	@Getter
	private String lastKnownName;

	public JsonOpMintConfigEntry(UUID uuid, String lastKnownName) {
		this.uuid = uuid.toString();
		this.lastKnownName = lastKnownName;
	}

	public static JsonOpMintConfigEntry ofPlayer(EntityPlayer player) {
		return new JsonOpMintConfigEntry(player.getUUID(), player.getName());
	}

	public UUID getUuid() {
		return UUID.fromString(this.uuid);
	}
}
