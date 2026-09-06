plugins {
    java
    application
    id("com.diffplug.spotless") version "8.2.1"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

application {
    mainClass.set("Main")
}

dependencies {
    implementation("com.googlecode.lanterna:lanterna:3.1.2")
    implementation("com.google.code.gson:gson:2.14.0")
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

tasks.register<Jar>("roguelike") {
    manifest {
        attributes["Main-Class"] = "Main"
    }
    archiveBaseName.set("rogue")
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}