# pdf-generator-android-library
# the library is in a state of development!

Integrating the project is simple, All you need to do is follow the below steps

Step 1. In settings.gradle, add maven { url 'https://jitpack.io' }
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
