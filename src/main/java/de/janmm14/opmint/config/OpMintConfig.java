package de.janmm14.opmint.config;

import com.blackypaw.simpleconfig.SimpleConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OpMintConfig extends SimpleConfig {

	private String configSecret;
	private boolean setupEnabled = true;
	private List<OpMintConfigEntry> op = new ArrayList<>();
}
