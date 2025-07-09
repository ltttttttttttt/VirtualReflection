import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    //https://github.com/vanniktech/gradle-maven-publish-plugin
    //https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-publish-libraries.html#publish-to-maven-central-using-continuous-integration
    //https://central.sonatype.com/publishing/deployments
    id("com.vanniktech.maven.publish") version publishVersion
}

group = "io.github.ltttttttttttt"
//上传到mavenCentral命令: ./gradlew publishAllPublicationsToSonatypeRepository
//mavenCentral后台: https://s01.oss.sonatype.org/#stagingRepositories
version = mVersion

mavenPublishing {
    publishToMavenCentral()

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
        publishLibraryVariants("release")
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
    namespace = "com.lt.reflection"
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