# Preferencer
Processor to simplify Android Shared Preferences

[![](https://jitpack.io/v/RaunySouza/preferencer.svg)](https://jitpack.io/#RaunySouza/preferencer)

## Usage

To use Preferencer is simple, just create an interface that represents your SharedPreference fields, and that's it.
Preferencer will do the rest for you, generating all boilerplate code needed to use SharedPreferences and some plus
features.
 
 ```java
 
@SharedPreference
public interface Example {
 
   @DefaultString("John Doe")
   String name();
   
   @DefaultInt(30)
   int age();
 
}
```

So, Preferencer will generate a class using this interface as base, creating every method as a preference.

```java

ExamplePreference pref = new ExamplePreference(context);
String name = pref.name()
                get();
                
pref.age().put(28);

```

Beside that, you can edit preferences in batch:

```java

...

pref.edit()
    .name().put("John Snow")
    .age().remove()
    .apply();

```

Remember, generated class DOES NOT implement the base interface, instead use the methods to create its own methods.
So, after created, the class will have as name InterfaceName + Preference, for instance, Settings.java will generate SettingsPreference.java.
But, if your interface name starts with "I" (ISettings.java), Preferencer will only remove "I", keeping the rest, e.g.: ISettings.java will generate Settings.java

# Including in your project

```groovy
buildscript {
    repositories {
        jcenter()
    }
   ...
}

allProjects {
  repositories {
    // required to find the project's artifacts
    maven { url "https://www.jitpack.io" }
  }
}
```

Add the library to the project-level build.gradle:

```groovy
def preferencer_version = "0.0.1-alpha3"

dependencies {
  annotationProcessor "com.github.RaunySouza.preferencer:preferencer-processor:${preferencer_version}"
  compile "com.github.RaunySouza.preferencer:preferencer:${preferencer_version}"
}
```

# License

    Copyright 2016-2017 Rauny Souza
     
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
     
    http://www.apache.org/licenses/LICENSE-2.0
     
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
