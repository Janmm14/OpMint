package de.janmm14.opmint;

import io.gomint.entity.EntityPlayer;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileWriter;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class ConfigAdapter {

	private final OpMint plugin;
	@Getter
	private File configFile;
	@Getter
	private OpMintConfig config = new OpMintConfig();

	public ConfigAdapter(OpMint plugin) {
		this.plugin = plugin;
		File dataFolder = plugin.getDataFolder();
		if (!dataFolder.mkdirs()) {
			throw new RuntimeException("Could no create data folder");
		}
		configFile = new File(dataFolder, "config.cfg");
	}

	public boolean addOp(EntityPlayer player) {
		fixLists();
		OpMintConfigEntry entry = OpMintConfigEntry.ofPlayer(player);
		if (config.getOp().contains(entry)) {
			return false;
		}
		config.getOp().add(entry);
		return true;
	}

	public boolean removeOp(EntityPlayer player) {
		fixLists();
		OpMintConfigEntry entry = OpMintConfigEntry.ofPlayer(player);
		boolean removed = config.getOp().remove(entry);
		//noinspection StatementWithEmptyBody
		while (config.getOp().remove(entry)) {
		}
		return removed;
	}

	private void fixLists() {
		config.setOp(new ArrayList<>(new HashSet<>(config.getOp())));
	}

	@SneakyThrows
	public void initialize() {
		config.initialize(configFile);
		config.setConfigSecret(generateRandomSecret());
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
	private static String generateRandomSecret() {
		char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789".toCharArray();
		int length = chars.length;
		StringBuilder sb = new StringBuilder(40);
		Random random = SecureRandom.getInstanceStrong();
		for (int i = 0; i < 40; i++) {
			sb.append(chars[random.nextInt(length)]);
		}
		return sb.toString();
	}
}
