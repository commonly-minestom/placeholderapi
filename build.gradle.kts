plugins {
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version "9.3.1"
}

group = "me.clip"
version = "2.12.4"

description = "An awesome placeholder provider!"

allprojects {

    apply { plugin("java-library") }

    group = rootProject.group
    version = rootProject.version

    repositories {
        maven("https://oss.sonatype.org/content/repositories/snapshots/")

        mavenCentral()
        mavenLocal()

        maven("https://repo.codemc.org/repository/maven-public/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    dependencies {
        compileOnlyApi("org.jetbrains:annotations:23.0.0")

        testImplementation("org.junit.jupiter:junit-jupiter:6.0.2")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

    configurations {
        testImplementation {
            extendsFrom(compileOnly.get())
        }
    }

    tasks {
        processResources {
            eachFile { expand("version" to project.version) }
        }

        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        test {
            useJUnitPlatform()
        }
    }
}