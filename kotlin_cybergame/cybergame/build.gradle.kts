plugins {
    kotlin("jvm") version "2.3.21"
}

group = "com.hse.cyber"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // Source: https://mvnrepository.com/artifact/org.springframework/spring-web
    implementation("org.springframework:spring-web:7.0.7")
    // Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web
    implementation("org.springframework.boot:spring-boot-starter-web:4.0.6")
    // Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-jdbc
    implementation("org.springframework.boot:spring-boot-starter-jdbc:4.0.6")
    // Source: https://mvnrepository.com/artifact/org.postgresql/postgresql
    implementation("org.postgresql:postgresql:42.7.11")

    testImplementation("org.springframework.boot:spring-boot-starter-test:4.0.6")
    testRuntimeOnly("com.h2database:h2")
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}