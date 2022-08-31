## ServerPingPlayerList

[![discord](https://img.shields.io/discord/895546064260718622?logo=discord)](https://discord.0b0t.org)
[![reddit](https://img.shields.io/reddit/subreddit-subscribers/0b0t)](https://old.reddit.com/r/0b0t/)
![last commit](https://img.shields.io/github/last-commit/zeroBzeroT/ServerPingPlayerList)
![repo size](https://img.shields.io/github/languages/code-size/zeroBzeroT/ServerPingPlayerList.svg?label=repo%20size)
[![downloads](https://img.shields.io/github/downloads/zeroBzeroT/ServerPingPlayerList/total)](https://github.com/zeroBzeroT/ServerPingPlayerList/releases)

---

ServerPingPlayerList is a plugin for [BungeeCord](https://github.com/SpigotMC/BungeeCord). The plugin makes sure that
the player list is displayed as so-called samples again. This feature is included in Vanilla Minecraft and Bukkit,
Spigot & Paper by default, but the [BungeeCord Proxy](https://github.com/SpigotMC/BungeeCord) does not support this
feature. Therefore, I have developed this plugin.

### Details

- All players of a server in the server browser (on hover)
- Server name, version and minimum supported protocol
- Message of the day override

### Commands

- **/spplreload {count}**   reloads the config from the file

### Config

| Setting                 | Description                                 |
|-------------------------|---------------------------------------------|
| versionName             | Name and Version of the Server              |
| versionMinProtocol      | Minimum supported protocol version          |
| setHoverInfo            | Show player list on hover in server browser |
| messageOfTheDayOverride | Overrides all MOTDs of the servers          |
| messageOfTheDay         | Message of the day                          |

### Libraries

- BungeeCord API

### Statistics

![Graph](https://bstats.org/signatures/bungeecord/0b0t_ServerListListener.svg)

### Warranty

The Software is provided "as is" and without warranties of any kind, express
or implied, including but not limited to the warranties of merchantability,
fitness for a particular purpose, and non-infringement. In no event shall the
Authors or copyright owners be liable for any claims, damages or other
liability, whether in an action in contract, tort or otherwise, arising from,
out of or in connection with the Software or the use or other dealings in the
Software.
