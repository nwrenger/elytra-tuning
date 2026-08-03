# Elytra Tuning

[![modrinth](https://img.shields.io/modrinth/v/elytra-tuning.svg)](https://modrinth.com/mod/elytra-tuning)
[![modrinth](https://img.shields.io/badge/dynamic/json?url=https://api.modrinth.com/v2/project/elytra-tuning&label=downloads&query=$.downloads&color=#00AF5C)](https://modrinth.com/mod/elytra-tuning)
[![modrinth](https://img.shields.io/modrinth/game-versions/elytra-tuning.svg)](https://modrinth.com/mod/elytra-tuning)

A lightweight mod that adds configuration options to **limit elytra flight speed and tune firework boosting's strength and duration**.

Works in both **singleplayer** and on a dedicated **server**, with an optional client-side mod that offers additional improvements for players.

> Ideal for multiplayer servers that want to **nerf elytra rushing**, **reduce chunk-loading lag**, or **balance late-game travel**.

## Why Use This Mod?

1. **Configurable Speed Limits**:
   Limit either horizontal flight speed or total three-dimensional velocity.
2. **Firework Tuning**:
   Adjust both the strength and duration of firework boosts.
3. **Performance and Balance**:
   Reduce extreme chunk loading and rebalance late-game travel without disabling elytras.
4. **Flexible**:
   The server enforces its configuration for every player, while the optional client installation synchronizes prediction for smoother flight.
5. **Compatible**:
   Handles normal flight, firework boosting, riptide launches, and modded flight setups such as **Do a Barrel Roll**.

## How It Works

Minecraft calculates elytra movement **server-side**, while the client predicts it **locally**. The server configuration controls:

- **Speed limiting**, calculated from either horizontal (`X/Z`) or absolute (`X/Y/Z`) velocity.
- **Firework boosts**, with configurable strength and duration multipliers relative to vanilla behavior.

When installed on the client, the server synchronizes its complete configuration so both sides calculate flight consistently.

> **Note**:
> Without the mod on the client, players might snap back (lag back) because the server corrects their velocity when exceeding the cap.
> Server owners should encourage players to install the mod for a smoother experience.

## Configuration

A config file is created at:

```sh
./config/elytra-tuning.json
```

Default contents:

```json
{
  "speed": {
    "max": 60.0,
    "calculation": "HORIZONTAL"
  },
  "rocket": {
    "boost_multiplier": 1.0,
    "duration_multiplier": 1.0
  }
}
```

- `speed`
  - `max`: Maximum allowed elytra speed in **blocks per second**.
  - `calculation`: `HORIZONTAL` measures `X/Z` movement; `ABSOLUTE` measures total `X/Y/Z` velocity.
- `rocket`
  - `boost_multiplier`: `0.0` disables acceleration, `1.0` is vanilla strength, and higher values add stronger forward acceleration.
  - `duration_multiplier`: `1.0` is vanilla duration, `0.5` halves it, and `2.0` doubles it.
- Set `speed` or `rocket` to `null` to disable that section.
- After editing, **restart the server/game** to apply changes.

## Showcase

> TODO

The max speed is set to **10 blocks per second**.

> **Rocket Boosting**: Measured in-game speed is 9.90 blocks per second.
>
> ![rocket](showcase/rocket.png)

> **Riptide Trident**: Measured in-game speed is 9.99 blocks per second.
>
> ![trident](showcase/trident.png)

## Contributing & Issues

I warmly welcome:

- Bug reports
- Feature requests
- Pull requests

Please open issues or PRs on [GitHub](https://github.com/nwrenger/elytra-tuning/issues).

## License

This project is licensed under the **LGPLv3 License**. See [LICENSE](https://github.com/nwrenger/elytra-tuning/blob/main/LICENSE) for details.
