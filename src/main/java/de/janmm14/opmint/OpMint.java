package de.janmm14.opmint;

import de.janmm14.opmint.commands.DeopCommand;
import de.janmm14.opmint.commands.OpCommand;
import de.janmm14.opmint.commands.OpListCommand;
import de.janmm14.opmint.commands.SetupCommand;
import io.gomint.plugin.Plugin;
import io.gomint.plugin.PluginName;
import io.gomint.plugin.Version;
import lombok.Getter;
import lombok.SneakyThrows;

@PluginName("OpMint")
@Version(major = 0, minor = 1)
public class OpMint extends Plugin {

	@Getter
	private Controller controller;

	@Override
	@SneakyThrows
	public void onInstall() {
		controller = new Controller(this);
		boolean newInstall = !controller.getConfigFile().exists();
		controller.initialize();
		if (controller.getConfig().isSetupEnabled()) {
			registerCommand(new SetupCommand(this));
			if (newInstall) {
				getLogger().info("To gain OP enter /opmintsetup %1$s", controller.getConfig().getConfigSecret());
			}
		}
		registerCommand(new OpCommand(controller));
		registerCommand(new DeopCommand(controller));
		registerCommand(new OpListCommand(controller));
	}

	@Override
	public void onUninstall() {

	}
}
