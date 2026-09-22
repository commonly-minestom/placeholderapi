plugins {
    `java-library`
    id("me.champeau.jmh") version "0.7.2"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    disableAutoTargetJvm()
}

base {
    archivesName.set("PlaceholderAPI-Common")
}

dependencies {
    api("net.kyori:adventure-platform-api:4.4.1")
    jmh("org.openjdk.jmh:jmh-core:1.37")
    jmh("org.openjdk.jmh:jmh-generator-annprocess:1.37")
    jmhAnnotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")
}