plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
    application
}

group = "com.hse.cyber"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")

    // Jackson для Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Kotlin reflection (обязательно для Spring)
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // PostgreSQL
    runtimeOnly("org.postgresql:postgresql")

    // Тесты — Spring Boot Test уже включает JUnit 5, Mockito, MockMvc
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")

    // H2 для тестов вместо PostgreSQL
    testRuntimeOnly("com.h2database:h2")
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}

//plugins {
//    kotlin("jvm") version "1.9.0"
//    application
//}

application {
    mainClass.set("com.hse.cyber.MainApplicationKt") // adjust to your actual main class
}