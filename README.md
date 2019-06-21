# Preferencer
Processor to simplify Android Shared Preferences

[![](https://jitpack.io/v/RaunySouza/preferencer.svg)](https://jitpack.io/#RaunySouza/preferencer)

# Including in your project

We need to include the [apt plugin](https://bitbucket.org/hvisser/android-apt) in our classpath to enable Annotation Processing:

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
    }
}

allProjects {
  repositories {
    // required to find the project's artifacts
    maven { url "https://www.jitpack.io" }
  }
}
```

Add the library to the project-level build.gradle, using the apt plugin to enable Annotation Processing:

```groovy
apply plugin: 'com.neenbedankt.android-apt'

def preferencer_version = "0.0.1-alpha3"

dependencies {
  apt "com.github.RaunySouza.preferencer:preferencer-processor:${preferencer_version}"
  compile "com.github.RaunySouza.preferencer:preferencer:${preferencer_version}"
}
```
