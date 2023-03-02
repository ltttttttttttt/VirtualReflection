plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.serialization") version kotlinVersion
    id("com.google.devtools.ksp") version kspVersion
}

android {
    namespace = "com.lt.virtual_reflection"
    compileSdk = 32

    defaultConfig {
        applicationId = "com.lt.buffapp"
        minSdk = 21
        targetSdk = 29
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        //vectorDrawables {
        //    useSupportLibrary true
        //}
    }
    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            kotlin {
                sourceSets.main {
                    kotlin.srcDir("build/generated/ksp/debug/kotlin")
                }
            }
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            kotlin {
                sourceSets.main {
                    kotlin.srcDir("build/generated/ksp/release/kotlin")
                }
            }
        }
    }
    kotlin {
        sourceSets.test {
            kotlin.srcDir("build/generated/ksp/test/kotlin")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    //packagingOptions {
    //    resources {
    //        excludes += '/META-INF/{AL2.0,LGPL2.1}'
    //    }
    //}
    //ksp配置
    //ksp {
    //    arg("customInClassWithBuff", "//Class end")
    //    arg("customInFileWithBuff", "//File end")
    //    arg("classSerializeAnnotationWithBuff", "//Not have")
    //    arg("fieldSerializeTransientAnnotationWithBuff", "@kotlin.jvm.Transient")
    //}
}

dependencies {
    implementation(project(":VirtualReflection"))
    ksp(project(":VirtualReflection"))

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}