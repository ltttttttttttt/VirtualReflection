import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("convention.publication")
    id("com.vanniktech.maven.publish") version "0.33.0"//https://github.com/vanniktech/gradle-maven-publish-plugin https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-publish-libraries.html#publish-to-maven-central-using-continuous-integration
}

group = "io.github.ltttttttttttt"
//上传到mavenCentral命令: ./gradlew publishAllPublicationsToSonatypeRepository
//mavenCentral后台: https://s01.oss.sonatype.org/#stagingRepositories
version = mVersion

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(group.toString(), project.name, version.toString())

    pom {
        name = project.name
        description = "Virtual reflection of KMP"
        inceptionYear = "2023"
        url = "https://github.com/ltttttttttttt/VirtualReflection"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "ltttttttttttt"
                name = "lt"
                email = "lt.dygzs@qq.com"
                url = "https://github.com/ltttttttttttt"
            }
        }
        scm {
            url = "https://github.com/ltttttttttttt/VirtualReflection"
        }
    }
}

kotlin {
    androidTarget {
        publishLibraryVariants("debug", "release")
    }

    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    js(IR) {
        browser()
        compilations.all {
            defaultSourceSet.resources.srcDir("/resources")
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "VirtualReflection-lib"
        browser {
            commonWebpackConfig {
                outputFileName = "VirtualReflection-lib.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(project.projectDir.path)
                    }
                }
            }
        }
        binaries.executable()
    }

    cocoapods {
        summary = "VirtualReflection"
        homepage = "https://github.com/ltttttttttttt/VirtualReflection"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "VirtualReflection"
            isStatic = true
        }
        extraSpecAttributes["resources"] =
            "['resources/**']"
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting

        val androidMain by getting
        val androidUnitTest by getting

        val jvmMain by getting
        val jvmTest by getting

        val iosX64Main by getting
        val iosX64Test by getting
        val iosArm64Main by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Main by getting
        val iosSimulatorArm64Test by getting

        val jsMain by getting

        val wasmJsMain by getting
    }
}

android {
    compileSdk = 35
    defaultConfig {
        minSdk = 21
        targetSdk = 31
        sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
        sourceSets["main"].res.srcDir("resources")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

afterEvaluate {
    try {
        tasks.findByName("signAndroidReleasePublication")!!
            .mustRunAfter(tasks.findByName("publishAndroidDebugPublicationToSonatypeRepository"))
        tasks.findByName("signIosArm64Publication")!!
            .mustRunAfter(tasks.findByName("publishAndroidDebugPublicationToSonatypeRepository"))
        tasks.findByName("signIosSimulatorArm64Publication")!!
            .mustRunAfter(tasks.findByName("publishAndroidDebugPublicationToSonatypeRepository"))
        tasks.findByName("signIosX64Publication")!!
            .mustRunAfter(tasks.findByName("publishAndroidDebugPublicationToSonatypeRepository"))
        tasks.findByName("signJsPublication")!!
            .mustRunAfter(tasks.findByName("publishAndroidDebugPublicationToSonatypeRepository"))
        tasks.findByName("signJvmPublication")!!
            .mustRunAfter(tasks.findByName("publishAndroidDebugPublicationToSonatypeRepository"))
        tasks.findByName("signKotlinMultiplatformPublication")!!
            .mustRunAfter(tasks.findByName("publishAndroidDebugPublicationToSonatypeRepository"))
        tasks.findByName("signIosArm64Publication")!!
            .mustRunAfter(tasks.findByName("publishAndroidReleasePublicationToSonatypeRepository"))
        tasks.findByName("signIosSimulatorArm64Publication")!!
            .mustRunAfter(tasks.findByName("publishIosArm64PublicationToSonatypeRepository"))
        tasks.findByName("signIosSimulatorArm64Publication")!!
            .mustRunAfter(tasks.findByName("publishAndroidReleasePublicationToSonatypeRepository"))
        tasks.findByName("signIosX64Publication")!!
            .mustRunAfter(tasks.findByName("publishIosArm64PublicationToSonatypeRepository"))
        tasks.findByName("signIosX64Publication")!!
            .mustRunAfter(tasks.findByName("publishAndroidReleasePublicationToSonatypeRepository"))
        tasks.findByName("signJsPublication")!!
            .mustRunAfter(tasks.findByName("publishIosArm64PublicationToSonatypeRepository"))
        tasks.findByName("signJsPublication")!!
            .mustRunAfter(tasks.findByName("publishAndroidReleasePublicationToSonatypeRepository"))
        tasks.findByName("signJvmPublication")!!
            .mustRunAfter(tasks.findByName("publishIosArm64PublicationToSonatypeRepository"))
        tasks.findByName("signJvmPublication")!!
            .mustRunAfter(tasks.findByName("publishAndroidReleasePublicationToSonatypeRepository"))
        tasks.findByName("signKotlinMultiplatformPublication")!!
            .mustRunAfter(tasks.findByName("publishIosArm64PublicationToSonatypeRepository"))
        tasks.findByName("signKotlinMultiplatformPublication")!!
            .mustRunAfter(tasks.findByName("publishAndroidReleasePublicationToSonatypeRepository"))
        tasks.findByName("signIosX64Publication")!!
            .mustRunAfter(tasks.findByName("publishIosSimulatorArm64PublicationToSonatypeRepository"))
        tasks.findByName("signJsPublication")!!
            .mustRunAfter(tasks.findByName("publishIosSimulatorArm64PublicationToSonatypeRepository"))
        tasks.findByName("signJvmPublication")!!
            .mustRunAfter(tasks.findByName("publishIosSimulatorArm64PublicationToSonatypeRepository"))
        tasks.findByName("signKotlinMultiplatformPublication")!!
            .mustRunAfter(tasks.findByName("publishIosSimulatorArm64PublicationToSonatypeRepository"))
        tasks.findByName("signJsPublication")!!
            .mustRunAfter(tasks.findByName("publishIosX64PublicationToSonatypeRepository"))
        tasks.findByName("signJvmPublication")!!
            .mustRunAfter(tasks.findByName("publishIosX64PublicationToSonatypeRepository"))
        tasks.findByName("signKotlinMultiplatformPublication")!!
            .mustRunAfter(tasks.findByName("publishIosX64PublicationToSonatypeRepository"))
        tasks.findByName("signJvmPublication")!!
            .mustRunAfter(tasks.findByName("publishJsPublicationToSonatypeRepository"))
        tasks.findByName("signKotlinMultiplatformPublication")!!
            .mustRunAfter(tasks.findByName("publishJsPublicationToSonatypeRepository"))
        tasks.findByName("signKotlinMultiplatformPublication")!!
            .mustRunAfter(tasks.findByName("publishJvmPublicationToSonatypeRepository"))
        tasks.findByName("signWasmJsPublication")!!
            .mustRunAfter(tasks.findByName("publishKotlinMultiplatformPublicationToSonatypeRepository"))
        tasks.findByName("signWasmJsPublication")!!
            .mustRunAfter(tasks.findByName("publishJvmPublicationToSonatypeRepository"))
        tasks.findByName("signWasmJsPublication")!!
            .mustRunAfter(tasks.findByName("publishJsPublicationToSonatypeRepository"))
        tasks.findByName("signWasmJsPublication")!!
            .mustRunAfter(tasks.findByName("publishIosX64PublicationToSonatypeRepository"))
        tasks.findByName("signWasmJsPublication")!!
            .mustRunAfter(tasks.findByName("publishIosSimulatorArm64PublicationToSonatypeRepository"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}