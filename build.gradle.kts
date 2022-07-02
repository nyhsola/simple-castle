buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/releases/") }
    }

    dependencies {
        classpath(libs.tgradle)
        classpath(libs.kgradle)
    }
}

plugins {
    id("com.google.devtools.ksp")
}

allprojects {
    apply() {
        plugin("idea")
    }
}

configure(subprojects - project(":android")) {
    apply(plugin = "kotlin")
    apply(plugin = "java-library")
    apply(plugin = "com.google.devtools.ksp")
}

subprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/releases/") }
    }
}