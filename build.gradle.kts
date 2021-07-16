import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    `java-test-fixtures`
}

group = "juxt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.0")
    implementation(platform("org.http4k:http4k-bom:4.9.10.0"))
    implementation("org.http4k:http4k-core")
    implementation("org.http4k:http4k-server-netty")
    implementation("org.http4k:http4k-client-apache")
    implementation("org.http4k:http4k-contract")
    implementation("org.http4k:http4k-format-jackson")
    implementation("org.webjars:swagger-ui:3.25.2")

    implementation("pro.juxt.crux", "crux-kotlin-dsl", "0.0.4")
    implementation("com.ubertob.kondor", "kondor-core", "1.6.1")

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testImplementation("com.natpryce:hamkrest:1.8.0.1")
    testImplementation("org.http4k", "http4k-testing-hamkrest", "4.9.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")

    testFixturesImplementation("com.ubertob.kondor", "kondor-core", "1.6.1")
    testFixturesImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testFixturesImplementation("com.natpryce:hamkrest:1.8.0.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}