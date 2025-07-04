plugins {
    kotlin("multiplatform")
    id("convention.publication")
}

group = "io.github.ltttttttttttt"
//上传到mavenCentral命令: ./gradlew publishAllPublicationsToSonatypeRepository
//mavenCentral后台: https://s01.oss.sonatype.org/#stagingRepositories
version = mVersion

kotlin {
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    sourceSets {
        val commonMain by getting
        val commonTest by getting

        val jvmMain by getting {
            dependencies {
                implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
                implementation("com.github.ltttttttttttt:KSPUtil:1.0.1")
                implementation(project(":VirtualReflection-lib"))
            }
        }
        val jvmTest by getting
    }
}

afterEvaluate {
    try {
        tasks.findByName("signKotlinMultiplatformPublication")!!
            .mustRunAfter(tasks.findByName("publishJvmPublicationToSonatypeRepository"))
    } catch (ignore: Exception) {
    }
}