import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.maven

/**
 * 注册各种maven服务器(包含镜像服务器)
 */
fun RepositoryHandler.mavenServers() {
    mavenCenterJitpack()
    mavenCenterTencentCloud()
    mavenCentral()
    jcenter()
    google()
    mavenCenterAliCloud()
    mavenCenterHuaweiCloud()
    maven("https://repo1.maven.org/maven2/")//友盟
    maven("https://artifact.bytedance.com/repository/pangle")//字节
    maven("https://artifact.bytedance.com/repository/AwemeOpenSDK")//字节
}

/**
 * 腾讯云镜像
 */
fun RepositoryHandler.mavenCenterTencentCloud() {
    maven("https://mirrors.tencent.com/nexus/repository/maven-public/")
}

/**
 * 华为云镜像
 */
fun RepositoryHandler.mavenCenterHuaweiCloud() {
    maven("https://repo.huaweicloud.com/repository/maven")
}

/**
 * 阿里云镜像
 * 阿里云云效仓库：https://maven.aliyun.com/mvn/guide
 */
fun RepositoryHandler.mavenCenterAliCloud() {
    maven("https://maven.aliyun.com/repository/jcenter")
    maven("https://maven.aliyun.com/repository/google")
    maven("https://maven.aliyun.com/nexus/content/groups/public/")
    maven("https://maven.aliyun.com/repository/central")
    maven("https://maven.aliyun.com/repository/public")
    maven("https://maven.aliyun.com/repository/spring")
    maven("https://maven.aliyun.com/repository/spring-plugin")
    maven("https://maven.aliyun.com/repository/gradle-plugin")
    maven("https://maven.aliyun.com/repository/grails-core")
    maven("https://maven.aliyun.com/repository/apache-snapshots")
}

/**
 * jitpack服务器
 */
fun RepositoryHandler.mavenCenterJitpack() {
    maven("https://jitpack.io")
}