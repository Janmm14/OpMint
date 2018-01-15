package de.janmm14.opmint;

import io.gomint.plugin.Plugin;
import io.gomint.plugin.PluginName;
import io.gomint.plugin.Version;
import lombok.Getter;
import lombok.SneakyThrows;

@PluginName("OpMint")
@Version(major = 0, minor = 1)
public class OpMint extends Plugin {

	@Getter
	private ConfigAdapter configAdapter;

	@Override
	@SneakyThrows
	public void onInstall() {
		configAdapter = new ConfigAdapter(this);
		boolean newInstall = !configAdapter.getConfigFile().exists();
		configAdapter.initialize();
		if (configAdapter.getConfig().isSetupEnabled()) {
			registerCommand(new SetupCommand(this));
			if (newInstall) {
				getLogger().info("To gain OP enter /opmintsetup %1$s", configAdapter.getConfig().getConfigSecret());
			}
		}
	}

	@Override
	public void onUninstall() {

	}
}
