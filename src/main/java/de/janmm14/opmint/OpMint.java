package de.janmm14.opmint;

import de.janmm14.opmint.commands.DeopCommand;
import de.janmm14.opmint.commands.OpCommand;
import de.janmm14.opmint.commands.OpListCommand;
import de.janmm14.opmint.commands.ReloadCommand;
import de.janmm14.opmint.commands.SetupCommand;
import io.gomint.event.EventHandler;
import io.gomint.event.EventListener;
import io.gomint.event.EventPriority;
import io.gomint.event.player.PlayerJoinEvent;
import io.gomint.plugin.Plugin;
import io.gomint.plugin.PluginName;
import io.gomint.plugin.Startup;
import io.gomint.plugin.StartupPriority;
import io.gomint.plugin.Version;
import lombok.Getter;
import lombok.SneakyThrows;

@Startup(StartupPriority.STARTUP)
@PluginName("OpMint")
@Version(major = 0, minor = 1)
public class OpMint extends Plugin implements EventListener {

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
				getLogger().info("To gain OP enter /opmintsetup %s", controller.getConfig().getConfigSecret());
			}
		}
		registerCommand(new OpCommand(controller));
		registerCommand(new DeopCommand(controller));
		registerCommand(new OpListCommand(controller));
		registerCommand(new ReloadCommand(controller));
		registerListener(this);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent event) {
		controller.checkOp(event.getPlayer());
	}
}
