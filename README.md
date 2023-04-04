# Hibernator
## What is it?
Hibernator is a minecraft plugin inspired by [Hibernate](https://www.spigotmc.org/resources/hibernate.4441/) that will hibernate the server when no players are playing on it
## How does it work?
In hibernation, the plugin will lower the server TPS (ticks per second). You can think of it as the plugin creating "fake lag". Hibernation will automatically turn off when a player joins. When all players disconnect, hibernation is enabled again.
## What are the disadvantages?
**• Because the server is frozen during hibernation, plugins that run tasks even when players are offline will not work correctly!** <br>
e.g. [Dynmap](https://github.com/webbukkit/dynmap) won't be able to run its web server during hibernation. <br>
• Your console will be spammed with "Can't keep up! Is the server overloaded?" messages
## What are the benefits?
• In hibernation CPU usage is reduced to 0-1% <br>
• Saves electricity
## Supported platforms
Spigot based minecraft servers 1.18+
## How to build
Requirements: [Git](https://git-scm.com/), [Maven](https://maven.apache.org/) <br> <br>
**Clone this repository** <br>
```git clone https://github.com/Mandlemankiller/Hibernator.git``` <br>
**Move to the folder** <br>
```cd Hibernator``` <br>
**Build** <br>
```mvn package``` <br>
**Done!** <br>
The jar is now located in `target` directory