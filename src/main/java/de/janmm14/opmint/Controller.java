package de.janmm14.opmint;

import de.janmm14.opmint.config.OpMintConfig;
import de.janmm14.opmint.config.OpMintConfigEntry;
import de.janmm14.opmint.config.json.JsonOpMintConfig;
import de.janmm14.opmint.config.json.JsonOpMintConfigEntry;
import io.gomint.entity.EntityPlayer;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Controller {

	private static final int SECRET_LENGTH = 40;

	@Getter
	private OpMint plugin;
	@Getter
	private File configFile;
	@Getter
	private OpMintConfig config = new OpMintConfig();

	@SneakyThrows
	public Controller(OpMint plugin) {
		this.plugin = plugin;
		File dataFolder = plugin.getDataFolder();
		if (!dataFolder.exists() && !dataFolder.mkdirs()) {
			throw new RuntimeException("Could no create data folder");
		}
		configFile = new File(dataFolder, "config.yml");
		File oldConfigFile = new File(dataFolder, "config.cfg");
		if (oldConfigFile.exists() && !configFile.exists()) {
			plugin.getLogger().info("Converting config to new system...");
			JsonOpMintConfig oldConfig = new JsonOpMintConfig();
			oldConfig.initialize(oldConfigFile);
			config.setConfigSecret(oldConfig.getConfigSecret());
			config.setSetupEnabled(oldConfig.isSetupEnabled());
			List<JsonOpMintConfigEntry> oldOps = oldConfig.getOp();
			List<OpMintConfigEntry> ops = config.getOp();
			for (JsonOpMintConfigEntry entry : oldOps) {
				ops.add(new OpMintConfigEntry(entry.getUuid(), entry.getLastKnownName()));
			}
			plugin.getLogger().info("Renaming old config file...");
			if (oldConfigFile.renameTo(new File(dataFolder, "config.cfg.old"))) {
				plugin.getLogger().info("Successfully renamed old config file.");
			} else {
				plugin.getLogger().warn("Could not rename old config file.");
			}
		}
	}

	/**
	 * @return {@code true} if the player was no operator before
	 */
	public boolean addOp(EntityPlayer player) {
		addOpPermission(player);
		fixLists();
		OpMintConfigEntry entry = OpMintConfigEntry.ofPlayer(player);
		if (config.getOp().contains(entry)) {
			return false;
		}
		config.getOp().add(entry);
		saveConfig();
		return true;
	}

	private void addOpPermission(EntityPlayer player) {
		player.getPermissionManager().setPermission("*", true);
	}

	/**
	 * @return {@code true} if the player was operator before
	 */
	public boolean removeOp(EntityPlayer player) {
		removeOpPermission(player);
		fixLists();
		OpMintConfigEntry entry = OpMintConfigEntry.ofPlayer(player);
		boolean removed = config.getOp().remove(entry);
		//noinspection StatementWithEmptyBody
		while (config.getOp().remove(entry)) {
		}
		saveConfig();
		return removed;
	}

	private void removeOpPermission(EntityPlayer player) {
		player.getPermissionManager().removePermission("*");
	}

	/**
	 * @return the uuid of the demoted player or {@code null} if there was no operator with the given last known name
	 */
	public UUID removeOp(String playerName) {
		EntityPlayer playerByName = plugin.getServer().findPlayerByName(playerName);
		if (playerByName != null) {
			if (removeOp(playerByName)) {
				return playerByName.getUUID();
			}
			return null;
		}

		fixLists();
		UUID playerUuid = null;
		Iterator<OpMintConfigEntry> iterator = config.getOp().iterator();
		while (iterator.hasNext()) {
			OpMintConfigEntry entry = iterator.next();
			if (entry.getLastKnownName().equals(playerName)) {
				if (playerUuid == null) {
					playerUuid = entry.getUuid();
				}
				iterator.remove();
			}
		}
		saveConfig();
		return playerUuid;
	}

	/**
	 * @return the name of the demoted player or {@code null} if there was no operator with the given uuid
	 */
	public String removeOp(UUID playerUuid) {
		EntityPlayer playerByName = plugin.getServer().findPlayerByUUID(playerUuid);
		if (playerByName != null) {
			if (removeOp(playerByName)) {
				return playerByName.getName();
			}
			return null;
		}

		fixLists();
		String name = null;
		Iterator<OpMintConfigEntry> iterator = config.getOp().iterator();
		while (iterator.hasNext()) {
			OpMintConfigEntry entry = iterator.next();
			if (entry.getUuid().equals(playerUuid)) {
				if (name == null) {
					name = entry.getLastKnownName();
				}
				iterator.remove();
			}
		}
		saveConfig();
		return name;
	}

	public void checkOp(EntityPlayer player) {
		if (getConfigEntry(player.getUUID()) != null) {
			addOpPermission(player);
		}
	}

	private OpMintConfigEntry getConfigEntry(UUID uuid) {
		return config.getOp().stream().filter(entry -> entry.getUuid().equals(uuid)).findFirst().orElse(null);
	}

	private OpMintConfigEntry getConfigEntry(String lastKnownName) {
		return config.getOp().stream().filter(entry -> entry.getLastKnownName().equals(lastKnownName)).findFirst().orElse(null);
	}

	private void fixLists() {
		if (!(config.getOp() instanceof ArrayList)) {
			config.setOp(new ArrayList<>(new HashSet<>(config.getOp())));
		}
	}

	@SneakyThrows
	public void initialize() {
		config.load(configFile);
		config.setConfigSecret(generateRandomSecret(SECRET_LENGTH));
		saveConfig();
		fixLists();
	}

	@SneakyThrows
	public void saveConfig() {
		config.save(configFile);
	}

	@SneakyThrows
	private static String generateRandomSecret(int length) {
		char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789".toCharArray();
		int charArrayLength = chars.length;
		StringBuilder sb = new StringBuilder(length);
		Random random = SecureRandom.getInstanceStrong();
		for (int i = 0; i < length; i++) {
			sb.append(chars[random.nextInt(charArrayLength)]);
		}
		return sb.toString();
	}
}
