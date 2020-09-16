# OpMint [![Build Status](https://s.janmm14.de/opmint-buildstatus)](https://s.janmm14.de/opmint-ci)

A plugin for the of minecraft pocket / bedrock edition server implementation [GoMint](https://github.oom/GoMint/GoMint).

This plugin provides a simple permission system:

- All permissions (op) or
- No permissions (not op)

[Download](https://s.janmm14.de/opmint-ci)

## Get started

Download the plugin. Put it into the `plugins` folder.

On first start, the plugin will generate the file `plugins/OpMint/config.yml`. It will generate a secret code and print it into the console and write it into the config.yml file. That secret code can used once to gain OP with the command `/opmintsetup`. After that, setup mode will be disabled.

## Usage

- `/op <Playername>` (alias: `/opmint`)
  - Grants op status to `<PlayerName>`
  - Example: `/op Janmm14`
  - Permission: `opmint.op`

- `/deop <Playername/UUID>` (alias: `/deopmint`)
  - Revokes op status of `<Playername/UUID>`
  - Example: `/deop Janmm14`
  - Permission: `opmint.deop`

- `/oplist` (alias: `/ops`)
  - Lists players who got operator status
  - Example: `/oplist`
  - Permission: `opmint.oplist`

- `/opmintreload`
  - Reloads the config file.
  - Example: `/oplist`
  - Permission: `opmint.reload`

- `/opmintsetup <config-secret>`
  - For first setup: Gain op yourself through entering the secret key you can find the the config.
  - Example: `/opmintsetup 0123456789012345678901234567890123456789`
  - Permission: `opmint.reload`
