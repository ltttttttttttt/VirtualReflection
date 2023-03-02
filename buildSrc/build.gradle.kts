plugins {
    `kotlin-dsl`
}

repositories {
    maven("https://mirrors.tencent.com/nexus/repository/maven-public/")
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.7")
    implementation("net.dongliu:apk-parser:2.6.9")
    implementation("dom4j:dom4j:1.6.1")
    implementation("com.squareup.okio:okio:2.10.0")
    implementation("javax.activation:activation:1.1.1")
}