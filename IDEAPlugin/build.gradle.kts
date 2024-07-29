plugins {
  id("java")
  id("org.jetbrains.intellij") version "1.17.3"
}

group = "net.kore.meep"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  if (!rootDir.absolutePath.endsWith("IDEAPlugin")) {
    implementation(project(path = ":API"))
    compileOnly("com.mojang:brigadier:1.0.18")
  }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
  version.set("2024.1.2")
  type.set("IC") // Target IDE Platform

  plugins.set(listOf("com.intellij.java"))
}

tasks {
  // Set the JVM compatibility versions
  withType<JavaCompile> {
    sourceCompatibility = "17"
    targetCompatibility = "17"
  }

  patchPluginXml {
    sinceBuild.set("241")
    untilBuild.set("242.*")
  }

  signPlugin {
    certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
    privateKey.set(System.getenv("PRIVATE_KEY"))
    password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
  }

  publishPlugin {
    token.set(System.getenv("PUBLISH_TOKEN"))
  }
}
