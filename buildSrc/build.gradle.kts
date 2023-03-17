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
    implementation("com.badlogicgames.packr:packr:3.0.3")

    implementation(gradleApi())
    implementation(localGroovy())
}