## Setup

Just import the [build.gradle](build.gradle) into an IDE of your choice and you should be good to go.

In order to keep compatibility with older Java and Minecraft versions HeadlessMc uses Java language level 8. It can be
built with any JDK &geq; 8, but language features > 8 can't be used. Starting with JDK 9 the `--release` flag will be 
used to cross compile the project to Java 8 bytecode. 

HeadlessMc uses [project lombok](https://github.com/projectlombok/lombok) to eliminate Java boilerplate.

## Structure

HeadlessMc is divided into multiple gradle subprojects.

**headlessmc-api** and **headlessmc-commons**: are used by both the launcher and the runtime and mainly contain the
command system logic.

**headlessmc-launcher**: is, well, the launcher. It contains all launching logic, instrumentation and depends on
headlessmc-lwjgl and some other libraries. The jars created by building headlessmc-lwjgl and the headlessmc-runtime will
be included as resources in the launcher jar. When launching the game with command/lwjgl support, those jars will be
unpacked and added to the classpath.

**headlessmc-runtime**: contains the commands which can be used while the game is running. In order to add command
support to a game without modifying classes it will be started using the runtimes' main-class which will then
call the games main-class.

**headlessmc-lwjgl**: contains the RedirectionApi and a class transformer which can be used by the launchers'
instrumentation, injected as a JavaAgent or as a launchwrapper Tweaker (more implementations for Fabric and the new fml
might follow) and will transform method in every `org.lwjgl` class this way:
```java
public <type> method(<arg>... args) {
    return (<type>) RedirectionApi.invoke(this, "<owner>;method(<arg>)<type>", <type>.class, args);
}
```
The RedirectionApi can return a default value for all datatypes except abstract classes (interfaces will be implemented
using `java.lang.reflect.Proxy`), but we can also redirect a call manually like this:
```java
RedirectionApi.getRedirectionManager().redirect("<owner>;method(<arg.type>)<type>", <Redirection>);
```

**buildSrc**: A gradle plugin allowing us to generate Java 9+ module-info classes. For an example take a look at the 
headlessmc-lwjgl [build.gradle](headlessmc-lwjgl/build.gradle), there we open all packages in order to allow the
transformed lwjgl classes to access the RedirectionApi (to do that we also need to transform lwjgls' module-info to open 
the headlessmc.lwjgl module). I tried around with other gradle plugins which allow you to cross compile to Java 8 while
generating module-info classes but all caused problems in one way or the other.