# Getting started

HeadlessMc can also be used as a library.

#### Installation

Depending on your build tool, add the following settings.
Currently, HeadlessMc is not deployed on maven central.
You can either use Jitpack or my maven to download it.

=== "Maven"

    ``` xml
    <repositories>
        <repository>
            <id>3arthqu4ke</id>
            <url>https://3arthqu4ke.github.io/maven</url>
        </repository>
    </repositories>

    <dependency>
        <groupId>io.github.headlesshq.headlessmc</groupId>
        <artifactId>headlessmc-launcher</artifactId>
        <version>$VERSION</version>
    </dependency>
    ```

=== "Gradle"

    ``` groovy
    repositories {
        maven {
            url 'https://3arthqu4ke.github.io/maven'
        }
    }

    dependencies {
        implementation 'io.github.headlesshq.headlessmc:headlessmc-launcher:$VERSION'
    }
    ```

=== "Maven (Jitpack)"

    ``` xml
    <repositories>
        <repository>
            <id>Jitpack</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

    <dependency>
        <groupId>com.github.3arthqu4ke.headlessmc</groupId>
        <artifactId>headlessmc-launcher</artifactId>
        <version>$VERSION</version>
    </dependency>
    ```

=== "Gradle (Jitpack)"

    ``` groovy
    repositories {
        maven {
            url 'https://jitpack.io'
        }
    }

    dependencies {
        implementation 'com.github.3arthqu4ke.headlessmc:headlessmc-launcher:$VERSION'
    }
    ```
