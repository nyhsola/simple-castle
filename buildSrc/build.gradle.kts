plugins {
    `kotlin-dsl`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of("17"))
    }
}

repositories {
    mavenCentral()
    google()
    maven { url = uri("https://artifactory.nimblygames.com/artifactory/ng-public") }
}

dependencies {
    implementation("com.badlogicgames.packr:packr:4.0.0")

    implementation(gradleApi())
    implementation(localGroovy())
}