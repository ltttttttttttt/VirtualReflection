<h1 align="center">VirtualReflection</h1>

<p align="center">Kotlin全平台的虚拟反射</p>

<p align="center">
<img src="https://img.shields.io/badge/license-Apache%202-blue.svg?maxAge=2592000">
<img src="https://jitpack.io/v/ltttttttttttt/VirtualReflection.svg"/>
</p>

<div align="center"><a href="https://github.com/ltttttttttttt/VirtualReflection/blob/main/README.md">us English</a> | cn 简体中文</div>

## 目前功能

1. TODO

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
    ksp("com.github.ltttttttttttt:VirtualReflection:$version")//this,比如0.0.3
}
```

Step 3.使用VirtualReflection TODO

给你的bean类加上@Buff注解,然后调用类的addBuff方法转换为新类,会将其中非构造中的属性(如name)自动转换为MutableState&lt;T&gt;

```kotlin
@Buff
class BuffBean(
    val id: Int? = null,
) {
    var name: String? = null
}
```

代码示例如下(具体可以参考项目中UseBuff.kt文件):

```kotlin
val buffBean = BuffBean(0)//这个BuffBean可以自己new出来,也可以通过序列化等方式
val bean = buffBean.addBuff()//增加Buff,类型改为BuffBeanWithBuff
bean.name//这个name的get和set就有了MutableState<T>的效果
bean.removeBuff()//退回为BuffBean(可选方法,可以不使用)
```

Step 4.将ksp的代码生成目录加入源码目录

在app模块目录内的build.gradle.kts内添加:

```kotlin
//如果你的是安卓项目,且未设置多渠道
android {
    buildTypes {
        release {
            kotlin {
                sourceSets.main {
                    kotlin.srcDir("build/generated/ksp/release/kotlin")
                }
            }
        }
        debug {
            kotlin {
                sourceSets.main {
                    kotlin.srcDir("build/generated/ksp/debug/kotlin")
                }
            }
        }
    }
    kotlin {
        sourceSets.test {
            kotlin.srcDir("build/generated/ksp/test/kotlin")
        }
    }
}

//如果你的是安卓项目,且设置了多渠道
applicationVariants.all {
    outputs.all {
        val flavorAndBuildTypeName = name
        kotlin {
            sourceSets.main {
                kotlin.srcDir(
                    "build/generated/ksp/${
                        flavorAndBuildTypeName.split("-").let {
                            it.first() + it.last()[0].toUpperCase() + it.last().substring(1)
                        }
                    }/kotlin"
                )
            }
        }
    }
}
kotlin {
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}

//如果你的是jvm等项目
kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}
```

Step 5.配置

本项目对序列化的默认支持为:kotlinx-serialization,如需修改,在app模块目录内的build.gradle.kts内添加:

```kotlin
ksp {
    //设置类序列化所需要的注解,其他序列化库一般不需要,所以我们放一个注释即可
    arg("classSerializeAnnotationWithBuff", "//Not have")
    //设置属性无需被序列化的注解,一般使用jvm中的transient关键字
    arg("fieldSerializeTransientAnnotationWithBuff", "@kotlin.jvm.Transient")
}
```

支持自定义增加代码,属性参考[KspOptions.handlerCustomCode],在app模块目录内的build.gradle.kts内添加:

```kotlin
ksp {
    arg("customInClassWithBuff", "//Class end")//类内
    arg("customInFileWithBuff", "//File end")//类外,kt文件内
}
```