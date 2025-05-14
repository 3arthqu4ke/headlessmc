# Servers

HeadlessMc can also be used to manage paper, fabric, purpur, neoforge, forge and vanilla servers.
You can add a server with the `server add <type> <version> <name>` command.
If `<version>` is not specified,
the server for the latest Minecraft version will be used.

``` title="Adding a server"
> server add paper 1.21.5
Adding paper server for 1.21.5
Added paper server: paper-1.21.5-76.
```

``` title="Listing servers"
> server list
id   type    version   name
0    paper   1.21.5    paper-1.21.5-76
```

``` title="Accepting the EULA of a server"
> server eula paper-1.21.5-76 accept
```

``` title="Launching a server"
> server launch paper-1.21.5-76
```

The argument `nogui`, to start the server without a GUI,
will be added automatically by HeadlessMc.
You can also specify JVM and other game arguments like this:

``` title="Launching a server with arguments"
> server launch paper-1.21.5-76 --jvm "-Xms10G -Xmx10G" --game-args "bonusChest eraseCache"
```

#### Mods and plugins

You can also manage the mods/plugins of your server.
Read about that [here](mods.md).
