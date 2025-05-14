# In-Memory

With the `-inmemory` flag HeadlessMc can even launch the game inside the same
JVM that is running HeadlessMc itself.
Making it possible to really run Minecraft anywhere, where a JVM can run.

Since HeadlessMc contains some libraries that
Minecraft also uses, but different versions which could clash,
it is important to keep the classloading seperate.
This can be done with the Launcher-Wrapper.

Classloading for In-Memory Launching with LauncherWrapper:
``` mermaid
graph TD
  A[SystemClassloader] --> B[LauncherWrapper];
  B --> C[PluginClassloader];
  C --> D[HeadlessMc];
  A[JVM Classloader] --> E[URLClassloader];
  E --> F[Modloader];
  F --> G[Minecraft];
```

Classloading for Forge:
``` mermaid
graph TD
  A[SystemClassloader] --> B[LauncherWrapper];
  B --> C[PluginClassloader];
  C --> D[HeadlessMc];
  A[JVM Classloader] --> E[BootstrapLauncherClassloader];
  E --> F[Forge/Neoforge];
  F --> G[Minecraft];
```
