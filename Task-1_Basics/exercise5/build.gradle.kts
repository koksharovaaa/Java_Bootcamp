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
    mainClass.set("Match")
}

spotless {
    java {
        googleJavaFormat("1.17.0")
    }
}

repositories {
    mavenCentral()
}

sourceSets {
    test {
        java.setSrcDirs(emptyList<String>())
        resources.setSrcDirs(emptyList<String>())
    }
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
