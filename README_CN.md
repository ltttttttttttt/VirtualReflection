<h1 align="center">VirtualReflection</h1>

<p align="center">Kotlin全平台的虚拟反射</p>

<p align="center">
<img src="https://img.shields.io/badge/license-Apache%202-blue.svg?maxAge=2592000">
<img src="https://jitpack.io/v/ltttttttttttt/VirtualReflection.svg"/>
</p>

<div align="center"><a href="https://github.com/ltttttttttttt/VirtualReflection/blob/main/README.md">us English</a> | cn 简体中文</div>

## 目前功能

1. 将某些包里面的所有类设置为支持虚拟反射

## 使用方式

Step 1.在项目的根目录的build.gradle.kts内添加:

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

Step 2.在app模块目录内的build.gradle.kts内添加:

version = [![](https://jitpack.io/v/ltttttttttttt/VirtualReflection.svg)](https://jitpack.io/#ltttttttttttt/VirtualReflection)

```kotlin
plugins {
    ...
    id("com.google.devtools.ksp") version "1.7.10-1.0.6"//this,前面的1.7.10对应你的kotlin版本,更多版本参考: https://github.com/google/ksp/releases
}

dependencies {
    ...
    ksp("com.github.ltttttttttttt:VirtualReflection:$version")//this,比如1.0.0
}
```

Step 3.使用VirtualReflection

配置需要虚拟反射的包,在app模块目录内的build.gradle.kts内的android(或kotlin)中添加:

```kotlin
ksp {
    arg("packageListWithVirtualReflection", "com.lt.virtual_reflection.bean/*你的包路径*/")
}
```

然后即可使用

```kotlin
//使用无参构造函数构造对象
KClass.newInstance()
//使用有参构造函数构造对象
KClass.newInstance(参数...)
```