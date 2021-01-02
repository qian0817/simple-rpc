import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.4.20" apply false
}

group = "org.example"
version = "1.0-SNAPSHOT"

subprojects {
    apply(plugin = "kotlin")
    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.jetbrains.kotlin", "kotlin-reflect")
        implementation("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
        testImplementation("org.jetbrains.kotlin", "kotlin-test-junit5")
        implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", "2.12.0")

        implementation("io.github.microutils", "kotlin-logging", "1.11.0")
        implementation("org.slf4j", "slf4j-log4j12", "1.7.30")

        implementation("cn.hutool", "hutool-all", "5.5.6")

        implementation("cglib", "cglib", "3.3.0")
        implementation("io.lettuce", "lettuce-core", "6.0.1.RELEASE")
        implementation("io.netty", "netty-all", "4.1.54.Final")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    java.sourceCompatibility = JavaVersion.VERSION_1_8
    java.targetCompatibility = JavaVersion.VERSION_1_8

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }
}
