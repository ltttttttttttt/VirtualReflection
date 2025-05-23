import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("convention.publication")
}

group = "io.github.ltttttttttttt"
//上传到mavenCentral命令: ./gradlew publishAllPublicationsToSonatypeRepository
//mavenCentral后台: https://s01.oss.sonatype.org/#stagingRepositories
version = mVersion

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