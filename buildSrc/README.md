# HeadlessMc-Modules

A gradle plugin allowing us to generate Java 9+ module-info classes. For an example take a look at the
headlessmc-lwjgl [build.gradle](../headlessmc-lwjgl/build.gradle), there we open all packages in order to allow the
transformed lwjgl classes to access the RedirectionApi (to do that we also need to transform lwjgls' module-info to open
the headlessmc.lwjgl module). I tried around with other gradle plugins which allow you to cross compile to Java 8 while
generating module-info classes but all caused problems in one way or the other.
