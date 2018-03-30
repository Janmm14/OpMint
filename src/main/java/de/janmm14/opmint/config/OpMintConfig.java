package de.janmm14.opmint.config;

import io.gomint.config.YamlConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OpMintConfig extends YamlConfig {

	private String configSecret;
	private boolean setupEnabled = true;
	private List<OpMintConfigEntry> op = new ArrayList<>();
}
