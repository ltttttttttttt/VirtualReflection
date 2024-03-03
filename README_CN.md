<h1 align="center">VirtualReflection</h1>

<p align="center">Kotlin全平台的虚拟反射</p>

<p align="center">
<img src="https://img.shields.io/badge/Kotlin-Multiplatform-%237f52ff?logo=kotlin">
<img src="https://img.shields.io/badge/license-Apache%202-blue.svg?maxAge=2592000">
<img src="https://img.shields.io/maven-central/v/io.github.ltttttttttttt/VirtualReflection"/>
</p>

<div align="center"><a href="https://github.com/ltttttttttttt/VirtualReflection/blob/main/README.md">us English</a> | cn 简体中文</div>

## 目前功能

1. 将某些包里面的所有类设置为支持虚拟反射
2. 通过注解标记某些类可以或不可以被虚拟反射

## 使用方式

Step 1.在app模块目录内的build.gradle.kts内添加:

version
= [![](https://img.shields.io/maven-central/v/io.github.ltttttttttttt/VirtualReflection)](https://repo1.maven.org/maven2/io/github/ltttttttttttt/VirtualReflection/)

* 如果是单平台,在app模块目录内的build.gradle.kts内添加

```kotlin
plugins {
    ...
    id("com.google.devtools.ksp") version "1.7.10-1.0.6"//this,前面的1.7.10对应你的kotlin版本,更多版本参考: https://github.com/google/ksp/releases
}
//配置ksp生成目录参考链接的第四步: https://github.com/ltttttttttttt/Buff/blob/main/README_CN.md?plain=1
dependencies {
    ...
    implementation("io.github.ltttttttttttt:VirtualReflection-lib:$version")//this,比如1.2.1
    ksp("io.github.ltttttttttttt:VirtualReflection:$version")//this,比如1.2.1
}
```

* 如果是多平台,在common模块目录内的build.gradle.kts内添加

```kotlin
plugins {
    ...
    id("com.google.devtools.ksp") version "1.7.10-1.0.6"//this,前面的1.7.10对应你的kotlin版本,更多版本参考: https://github.com/google/ksp/releases
}
...
val commonMain by getting {
    //配置ksp生成目录
    kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
    dependencies {
        ...
        api("io.github.ltttttttttttt:VirtualReflection-lib:$version")//this,比如1.2.1
    }
}

...
dependencies {
    add("kspCommonMainMetadata", "io.github.ltttttttttttt:VirtualReflection:$version")//this,比如1.2.1
}
```

Step 2.使用VirtualReflection

配置需要虚拟反射的包,在app模块目录内的build.gradle.kts内的android(或kotlin)中添加:

```kotlin
ksp {
    //配置多个路径用空格隔开
    arg("packageListWithVirtualReflection", "com.lt.virtual_reflection.bean/*你的包路径*/")
    //配置生成的方法名,默认是newInstance
    //arg("functionNameWithVirtualReflection", xxx)
    // TODO by lt 2024/3/2 23:38 默认是用string获取对象,可以通过参数改成通过class获取对象,这样就可以不用混淆了,改好后去掉混淆
}
```

然后即可使用

```kotlin
//使用无参构造函数构造对象
KClass.newInstance()
//使用有参构造函数构造对象
KClass.newInstance(参数...)
//通过字符串方式
VirtualReflectionUtil.newInstance("MainActivity")
//额外配置可以虚拟反射的类或文件
@ReflectionObject
//配置路径内的构造函数,使其不支持虚拟反射
@NotReflectionObjectConstructor
```