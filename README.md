<h1 align="center">VirtualReflection</h1>

<p align="center">Virtual reflection of Kotlin all target</p>

<p align="center">
<img src="https://img.shields.io/badge/Kotlin-Multiplatform-%237f52ff?logo=kotlin">
<img src="https://img.shields.io/badge/license-Apache%202-blue.svg?maxAge=2592000">
<img src="https://img.shields.io/maven-central/v/io.github.ltttttttttttt/VirtualReflection"/>
</p>

<div align="center">us English | <a href="https://github.com/ltttttttttttt/VirtualReflection/blob/main/README_CN.md">cn 简体中文</a></div>

## ability

1. Set all classes in certain packages to support virtual reflection
2. Mark certain classes that can or cannot be virtually reflected through annotations

## How to use

Step 1.Your app dir, build.gradle.kts add:

version
= [![](https://img.shields.io/maven-central/v/io.github.ltttttttttttt/VirtualReflection)](https://repo1.maven.org/maven2/io/github/ltttttttttttt/VirtualReflection/)

* If it is a single platform, add it to build.gradle.kts in the app module directory

```kotlin
plugins {
    ...
    id("com.google.devtools.ksp") version "1.7.10-1.0.6"//this, The left 1.7.10 corresponds to your the Kotlin version,more version: https://github.com/google/ksp/releases
}
//The fourth step of configuring ksp to generate directory reference links: https://github.com/ltttttttttttt/Buff/blob/main/README.md
dependencies {
    ...
    implementation("io.github.ltttttttttttt:VirtualReflection-lib:$version")//this, such as 1.2.1
    ksp("io.github.ltttttttttttt:VirtualReflection:$version")//this, such as 1.2.1
}
```

* If it is multi-platform, add it to build.gradle.kts in the common module directory

```kotlin
plugins {
    ...
    id("com.google.devtools.ksp") version "1.7.10-1.0.6"
}
...
val commonMain by getting {
    //Configure the ksp generation directory
    kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
    dependencies {
        ...
        api("io.github.ltttttttttttt:VirtualReflection-lib:$version")//this, such as 1.2.1
    }
}

...
dependencies {
    add("kspCommonMainMetadata", "io.github.ltttttttttttt:VirtualReflection:$version")//this, such as 1.2.1
}
```

Step 2.Use VirtualReflection

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
//Constructing Objects Using Non parametric Constructors
KClass.newInstance()
//Constructing Objects Using Parametric Constructors
KClass.newInstance(parameters...)
//By string method
VirtualReflectionUtil.newInstance("com.lt.virtual_reflection.bean.A")
//Additional configuration of classes or files that can be virtually reflected
@ReflectionObject
//Configure the constructor within the path to not support virtual reflection
@NotReflectionObjectConstructor
```
