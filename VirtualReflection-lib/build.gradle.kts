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
    android {
        publishLibraryVariants("debug", "release")
    }

    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }

    ios()
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

        val iosMain by getting
        val iosTest by getting
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosSimulatorArm64Test by getting {
            dependsOn(iosTest)
        }

        val jsMain by getting

        val wasmJsMain by getting
    }
}

android {
    compileSdk = 33
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