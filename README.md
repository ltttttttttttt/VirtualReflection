<h1 align="center">VirtualReflection</h1>

<p align="center">Virtual reflection of Kotlin all target</p>

<p align="center">
<img src="https://img.shields.io/badge/license-Apache%202-blue.svg?maxAge=2592000">
<img src="https://jitpack.io/v/ltttttttttttt/VirtualReflection.svg"/>
</p>

<div align="center">us English | <a href="https://github.com/ltttttttttttt/VirtualReflection/blob/main/README_CN.md">cn 简体中文</a></div>

## ability

1. TODO

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

Step 3.Use VirtualReflection TODO

Add the @Buff to your bean, call the addBuff() transform to the new Any, The attribute (such as
name) not in the constructor will be automatically converted to MutableState&lt;T&gt;

```kotlin
@Buff
class BuffBean(
    val id: Int? = null,
) {
    var name: String? = null
}
```

Example(reference UseBuff.kt):

```kotlin
val buffBean = BuffBean(0)
val bean = buffBean.addBuff()//Transform to the BuffBeanWithBuff
bean.name//The name's getter and setter have the effect of MutableState<T>
bean.removeBuff()//Fallback to BuffBean(optional)
```

Step 4.Add ksp dir to the srcDir

Your app dir, build.gradle.kts add:

```kotlin
//If your project is the android, and the productFlavors is not set
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

//If your project is the android, and the productFlavors is set
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

//If your project is the jvm or more
kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}
```

Step 5.Config

Serialize of this project uses kotlinx-serialization by default, To modify, Your app dir,
build.gradle.kts add:

```kotlin
ksp {
    //Set the Annotation of the class, Usually as follows
    arg("classSerializeAnnotationWithBuff", "//Not have")
    //Set the Annotation of the field to transient, Usually as follows
    arg("fieldSerializeTransientAnnotationWithBuff", "@kotlin.jvm.Transient")
}
```

Add custom code, reference [KspOptions.handlerCustomCode], Your app dir, build.gradle.kts add:

```kotlin
ksp {
    arg("customInClassWithBuff", "//Class end")//in class
    arg("customInFileWithBuff", "//File end")//in file
}
```