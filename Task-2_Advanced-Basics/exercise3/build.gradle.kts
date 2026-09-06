plugins {
    java
    application
    id("com.diffplug.spotless") version "8.2.1"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(18))
    }
}

application {
    mainClass.set("PetDiet")
}

spotless {
    java {
        googleJavaFormat("1.17.0")
    }
}

repositories {
    mavenCentral()
}


tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

dependencies {
    // Aggregator dependency for API and Engine
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

tasks.test {
    useJUnitPlatform()
}