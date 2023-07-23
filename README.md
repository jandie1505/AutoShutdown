# AutoShutdown
Shuts a minecraft server down after a specific time. Made for cloud systems to shut down a minigame server if an error occured in the minigame plugin.

## Installation
1. Download the plugin from the releases page
2. Put the plugin into your plugins folder
3. Restart your server

## Configuration
| Value | Description |
|--|--|
| enabled | Enable/disable auto-shutdown |
| time | Shut down the server after that amount of seconds |

## Commands
| Command | Description |
|--|--|
| `/autoshutdown disable` | Disable auto-shutdown |
| `/autoshutdown enable` | Enable auto-shutdown |
| `/autoshutdown get` | Disable current status and remaining time |
| `/autoshutdown set <time>` | Set time in seconds (like the config value) |

## Manage auto shutdown with other plugins
```java
// get AutoShutdown plugin
AutoShutdown autoShutdown = (AutoShutdown) this.getServer().getPluginManager().getPlugin("AutoShutdown");

// get status
boolean enabled = autoShutdown.isTimerEnabled();

// set status
autoShutdown.setTimerEnabled(false);

// get time
int time = autoShutdown.getTime();

// set time
autoShutdown.setTime(3600);
```
