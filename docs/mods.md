# Mods

HeadlessMc can manage mods for both the client and server.
For this the `mod` and the `server mod` command are used.
Currently, HeadlessMc only supports [Modrinth](https://modrinth.com/) for finding mods.

=== "Client"

    ``` title="Searching for mods"
    > mod search FabricApi
    name                      description                                                                                                                                                              authors
    fabric-api                Lightweight and modular API providing common hooks and intercompatibility measures utilized by mods using the Fabric toolchain.                                          modmuss50
    forgified-fabric-api      Fabric API implemented on top of NeoForge                                                                                                                                Su5eD
    fabric-polyfill           Backport of Fabric API events            
    ...
    ```

    The name column contains the identifier that is used for adding a mod.
    To add a mod, use the `mod add` command and specify which version to
    add the mod for, e.g. by using the versions id:  
    `mod add <version> <mod-name>`

    ``` title="Adding a mod"
    > versions
    id   name                           parent 
    0    1.21.5                         
    1    fabric-loader-0.16.14-1.21.5   1.21.5

    > mod add 1 fabric-api
    Downloaded mod fabric-api from Modrinth successfully.
    ```

    ``` title="List mods of a version"
    > mod list 1
    id   name         description                                                            authors
    0    fabric-api   Core API module providing key hooks and intercompatibility features.   FabricMC
    ```

    ``` title="Uninstall mod"
    > mod remove 1 fabric-api
    Mod 'fabric-api' deleted successfully.
    ```

=== "Server"

    For servers, the `server mod` command is used.

    ``` title="Searching for mods"
    > server mod search simple-voice-chat
    name                               description                                                                                                                                                              authors
    simple-voice-chat                  A working voice chat in Minecraft!                                                                                                         henkelmax
    enhanced-groups                    A server side Fabric mod providing useful features to Simple Voice Chat groups.                                                            henkelmax
    simple-voice-chat-discord-bridge   A mod and plugin to make a bridge between Simple Voice Chat and Discord to allow for players without the mod to hear and speak.   
    ...
    ```

    The name column contains the identifier that is used for adding a mod.
    To add a mod, use the `server mod add` command and specify which version to
    add the mod for, e.g. by using the versions id:  
    `server mod add <version> <mod-name>`

    ``` title="Adding a mod"
    > server list
    id   type    version   name
    0    paper   1.21.5    paper-1.21.5-76

    > server mod add 0 simple-voice-chat
    Downloaded mod simple-voice-chat from Modrinth successfully.
    ```
    
    Sometimes the name from the search on Modrinth is different
    from the name of the mod file on your computer.
    Make sure by listing the mods.

    ``` title="List mods of a server"
    > server mod list 0
    id   name         description                                                            authors
    0    voicechat   A working voice chat in Minecraft   Max Henkel, Matthew Wells
    ```

    ``` title="Uninstall mod"
    > server mod remove 0 voicechat
    Mod 'voicechat' deleted successfully.
    ```
