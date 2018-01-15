package de.janmm14.opmint;

import de.janmm14.opmint.config.OpMintConfig;
import de.janmm14.opmint.config.OpMintConfigEntry;
import io.gomint.entity.EntityPlayer;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileWriter;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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

	public Controller(OpMint plugin) {
		this.plugin = plugin;
		File dataFolder = plugin.getDataFolder();
		if (!dataFolder.mkdirs()) {
			throw new RuntimeException("Could no create data folder");
		}
		configFile = new File(dataFolder, "config.cfg");
	}

	/**
	 * @return {@code true} if the player was no operator before
	 */
	public boolean addOp(EntityPlayer player) {
		player.getPermissionManager().setPermission("*", true);
		fixLists();
		OpMintConfigEntry entry = OpMintConfigEntry.ofPlayer(player);
		if (config.getOp().contains(entry)) {
			return false;
		}
		config.getOp().add(entry);
		return true;
	}

	/**
	 * @return {@code true} if the player was operator before
	 */
	public boolean removeOp(EntityPlayer player) {
		player.getPermissionManager().removePermission("*");
		fixLists();
		OpMintConfigEntry entry = OpMintConfigEntry.ofPlayer(player);
		boolean removed = config.getOp().remove(entry);
		//noinspection StatementWithEmptyBody
		while (config.getOp().remove(entry)) {
		}
		return removed;
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
		return name;
	}

	private void fixLists() {
		if (!(config.getOp() instanceof ArrayList)) {
			config.setOp(new ArrayList<>(new HashSet<>(config.getOp())));
		}
	}

	@SneakyThrows
	public void initialize() {
		config.initialize(configFile);
		config.setConfigSecret(generateRandomSecret(SECRET_LENGTH));
		saveConfig();
		fixLists();
	}

	@SneakyThrows
	public void saveConfig() {
		FileWriter fileWriter = new FileWriter(configFile);
		config.write(fileWriter);
		fileWriter.close();
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
