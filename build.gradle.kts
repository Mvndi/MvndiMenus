plugins {
    java
    id("com.gradleup.shadow") version "8.3.2"
    id("com.github.ben-manes.versions") version "0.48.0"
}

var gprUser = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
var gprToken = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")

// Change to true when releasing
val release = false
val majorVersion = "1.14.1"
val minorVersion = if (release) "Release" else "DEV-" + System.getenv("BUILD_NUMBER")

group = "com.extendedclip"
version = "$majorVersion-$minorVersion"

repositories {
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.glaremasters.me/repository/public/")
    maven("https://nexus.phoenixdevt.fr/repository/maven-public/")
    maven("https://jitpack.io")
    maven {
        url = uri("https://maven.pkg.github.com/Mvndi/MvndiCore")
        credentials { username = gprUser; password = gprToken }
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")

    compileOnly(libs.vault)
    compileOnly(libs.authlib)

    compileOnly(libs.headdb)
    compileOnly(libs.itemsadder)
    compileOnly(libs.oraxen)
    compileOnly(libs.mmoitems)
    compileOnly(libs.score)

    compileOnly(libs.papi)

    implementation(libs.nashorn)
    implementation(libs.adventure.platform)

    compileOnly("org.jetbrains:annotations:23.0.0")
    compileOnly("net.mvndicraft:mvndicore:2.0.0-SNAPSHOT")
}

tasks {
    shadowJar {
        relocate("org.objectweb.asm", "com.extendedclip.deluxemenus.libs.asm")
        relocate("org.openjdk.nashorn", "com.extendedclip.deluxemenus.libs.nashorn")
        relocate("net.kyori", "com.extendedclip.deluxemenus.libs.adventure")
        archiveFileName.set("MvndiMenus-${rootProject.version}.jar")
    }
    build {
        dependsOn(shadowJar)
    }
    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    processResources {
        filesMatching("plugin.yml") {
            expand("version" to rootProject.version)
        }
    }
}
