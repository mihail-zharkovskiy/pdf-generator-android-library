# pdf-generator-android-library


Integrating the project is simple, All you need to do is follow the below steps

Step 1. Add the JitPack repository to your build file. Add it in your root build.gradle at the end of repositories:

```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
    }
}
```

Step 1a. If step 1 does not work go to settings.gradle
```
dependencyResolutionManagement {
    ...
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}

```

Step 2. Add the dependency
```
dependencies {
   implementation 'com.github.mihail-zharkovskiy:pdf-generator-android-library:1.0.0'
}
```
