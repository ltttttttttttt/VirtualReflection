<h1 align="center">VirtualReflection</h1>

<p align="center">Virtual reflection of Kotlin all target</p>

<p align="center">
<img src="https://img.shields.io/badge/license-Apache%202-blue.svg?maxAge=2592000">
<img src="https://jitpack.io/v/ltttttttttttt/VirtualReflection.svg"/>
</p>

<div align="center">us English | <a href="https://github.com/ltttttttttttt/VirtualReflection/blob/main/README_CN.md">cn 简体中文</a></div>

## ability

1. Set all classes in certain packages to support virtual reflection

## How to use

Step 1.Root dir, build.gradle.kts add:

```kotlin
buildscript {
    repositories {
        maven("https://jitpack.io")//this
        ...
    }
}

allprojects {
    repositories {
        maven("https://jitpack.io")//this
        ...
    }
}
```

Step 2.Your app dir, build.gradle.kts add:

version = [![](https://jitpack.io/v/ltttttttttttt/VirtualReflection.svg)](https://jitpack.io/#ltttttttttttt/VirtualReflection)

```kotlin
plugins {
    ...
    id("com.google.devtools.ksp") version "1.7.10-1.0.6"//this, The left 1.7.10 corresponds to your the Kotlin version,more version: https://github.com/google/ksp/releases
}

dependencies {
    ...
    ksp("com.github.ltttttttttttt:VirtualReflection:$version")//this, such as 0.0.3
}
```

Step 3.Use VirtualReflection

Configure packages that require virtual reflection, Your app dir, build.gradle.kts -> android(or kotlin) add:

```kotlin
ksp {
    //Configure multiple paths separated by spaces
    arg("packageListWithVirtualReflection", "com.lt.virtual_reflection.bean/*your package*/")
    //Configure the generated function name, which defaults to newInstance
    //arg("functionNameWithVirtualReflection", xxx)
}
```

use

```kotlin
KClass.newInstance()
//or
KClass.newInstance(parameters...)
```
