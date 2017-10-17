# ProtocolSupportPocketStuff
Does stuff for ProtocolSupport pocket, I guess.

This plugin
* Downloads & caches pc skins and sends them to pe.
* Receives & hacks & caches pe skins and applies them in pc.
* Uses bukkit to fake dimension switch for pe.
* Provides API for serveral pocket-only features.
* Enables you to easily build modals and listen to their response.

All features can be disabled in the config.yml

## *_WORK IN PROGRESS (OBVIOUSLY)_*

#### Compiling
Compiling PSPS is similar to ProtocolSupport.
1. Make sure you have the proper JDK installed.
2. Clone or download the repository.
3. Compile the latest version of mcpenew or mcpeinventory (depending on what you are testing)
4. Copy the freshly compiled ProtocolSupport.jar (mcpenew or mcpeinventory branch) into your `dlibs` folder.
5. Open a terminal or CMD and navigate to the root of the project.
6. Execute `gradlew jar` or `./gradlew jar` depending on your OS.
7. The compiled JAR will be inside the `target` folder.
