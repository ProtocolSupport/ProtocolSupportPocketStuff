# ProtocolSupportPocketStuff
Does stuff for ProtocolSupport pocket, I guess.

This plugin provides serveral differing features (or stuff) exclusively for minecraft PE on ProtocolSupport servers.
### Features
* Downloads & caches PC skins and sends them to PE.
* Uploads & caches PE skins and sends them to Mineskin to fake in PC.
* Allow for resource- & behaviourpacks in PE.
* Bukkit-driven dimensionswitch for PE.
* Hacks itemframes & player skulls for PE.

_All features can be disabled in the config.yml_
### API
* Enables you to easily check for PE players and get PE connections.
* Enables you to easily build modals and listen to their response.
* Enables you to easily send custom PE packets.
* Enables you to extend and write custom listeners for PE packets.
* Enables you to send custom skins and custom JSON geometry.
* Enables you to get device information such as the OS or phone model.

## *_WORK IN PROGRESS (OBVIOUSLY)_*

#### Compiling
Compiling PSPS is similar to ProtocolSupport.
1. Make sure you have the proper JDK installed.
2. Clone or download the repository.
3. Compile the latest version of mcpenew or mcpeinventory (depending on what you are testing)
4. Copy the freshly compiled ProtocolSupport.jar (mcpenew or mcpeinventory branch) into your `dllibs` folder.
5. Open a terminal or CMD and navigate to the root of the project.
6. Execute `gradlew jar` or `./gradlew jar` depending on your OS.
7. The compiled JAR will be inside the `target` folder.
