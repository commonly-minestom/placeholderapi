import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version "9.3.1"
}

repositories {
    mavenCentral()
    maven("https://repo.minestom.net/")
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25

    withJavadocJar()
    withSourcesJar()

    disableAutoTargetJvm()
}

base {
    archivesName.set("PlaceholderAPI-Minestom")
}

val javaComponent: SoftwareComponent = components["java"]

dependencies {
    api(project(":common"))

    compileOnly("net.minestom:minestom:2026.09.12-26.2")
    compileOnly("org.slf4j:slf4j-api:2.0.17")
    implementation("org.yaml:snakeyaml:2.7")
    testImplementation("net.minestom:testing:2026.09.12-26.2")
}

val shadowJarTask = tasks.named("shadowJar", ShadowJar::class.java)
val sourcesJarTask = tasks.named("sourcesJar", Jar::class.java)
val javadocJarTask = tasks.named("javadocJar", Jar::class.java)

tasks {
    build {
        dependsOn(named("shadowJar"))
    }

    test {
        systemProperty("minestom.inside-test", true)
    }

    withType<JavaCompile> {
        options.release = 25
    }

    withType<ShadowJar> {
        configurations = listOf(project.configurations.runtimeClasspath.get())

        archiveClassifier.set("")

        relocate("org.yaml", "me.clip.placeholderapi.libs.yaml")
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                artifactId = "placeholderapi-minestom"

                artifact(shadowJarTask) {
                    classifier = ""
                }

                artifact(sourcesJarTask) {
                    builtBy(sourcesJarTask)
                }

                artifact(javadocJarTask) {
                    builtBy(javadocJarTask)
                }
            }
        }

        repositories {
            maven {
                if ("-DEV" in version.toString()) {
                    url = uri("https://repo.extendedclip.com/snapshots")
                } else {
                    url = uri("https://repo.extendedclip.com/releases")
                }

                credentials {
                    username = System.getenv("JENKINS_USER")
                    password = System.getenv("JENKINS_PASS")
                }
            }
        }
    }

    publish.get().setDependsOn(listOf(build.get()))
}
