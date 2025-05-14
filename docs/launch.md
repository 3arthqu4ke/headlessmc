# Launching

Once started, HeadlessMc will wait for your command input.
You can try this out by typing `help` and pressing Enter,
to get a list of all available commands.
By default, [JLine](https://jline.org/) is enabled,
allowing you to get suggestions for commands and completing them by pressing TAB.

To launch the client, the `launch` command can be used.
```
launch 1.21.5
```
This launches the vanilla version `1.21.5`,
you can also specify a modloader to use like this:
```
launch fabric:1.21.5
```
Currently HeadlessMc supports the modloaders `fabric`, `forge` and `neoforge`.

#### Managing Versions
You can get a list of all currently downloaded client versions with the `versions`
command:
```
> versions
id   name                           parent
0    1.20.6                         
1    1.21.5                         
2    fabric-loader-0.16.14-1.21.5   1.21.5
```
This outputs a table of all client versions downloaded.
HeadlessMc makes use of such tables a lot.
You can use the id and name columns to specify versions.
``` title="Use id 1 to launch 1.21.5"
launch 1
```
``` title="Using the name of the version to launch"
launch fabric-loader-0.16.14-1.21.5
```

The launch command automatically downloads versions
if they are specified in the `<modloader>:<version>` format.
You can also manually manage versions with the `download` command:
```
> download 1.12.2
Downloading 1.12.2...
Download successful!

> versions
id   name                           parent
0    1.12.2                         
...
```
Modloaders for versions can be installed using the respective command:
```
> forge 1.12.2
Installing Forge 1.12.2-14.23.5.2860
...
Forge 1.12.2-14.23.5.2860 installed successfully!

> versions
id   name                           parent
0    1.12.2                         
1    1.12.2-forge-14.23.5.2860      1.12.2
...
```

#### Quitting HeadlessMc

Simply type `quit` to exit HeadlessMc.

#### Interactive Mode
By default, HeadlessMc will run in interactive mode.
It will continuously listen for commands,
until you end the process with quit.
You can also execute singular commands by specifying them at start with `--command`, like this:
```shell
java -jar headlessmc-launcher.jar --command <command>

./headlessmc-launcher-native --command <command>
```
