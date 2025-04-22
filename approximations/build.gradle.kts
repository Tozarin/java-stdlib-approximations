@file:Suppress("VulnerableLibrariesLocal")

plugins {
    java
    `maven-publish`
    id("org.springframework.boot") version "3.3.4" apply false
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

private val jacodbPackage = "com.github.UnitTestBot.jacodb"
private val jacodbVersion = "453ec7c0b3" // jacodb neo branch

private val springVersion = "3.3.4"

dependencies {
    compileOnly("$jacodbPackage:jacodb-api-jvm:$jacodbVersion")
    compileOnly("$jacodbPackage:jacodb-approximations:$jacodbVersion")
    compileOnly(files(rootDir.resolve("usvm-api/usvm-jvm-api.jar")))
    compileOnly("org.springframework.boot:spring-boot-starter-security:$springVersion")
    compileOnly("org.springframework.security:spring-security-test:6.2.0")
    compileOnly("org.springframework.boot:spring-boot-starter-web:$springVersion")
    compileOnly("org.springframework.boot:spring-boot-starter-test:$springVersion")
    compileOnly("org.springframework.boot:spring-boot-starter-data-jpa:$springVersion")
    compileOnly("org.springframework.boot:spring-boot-starter-validation:$springVersion")
    // Fixes "unknown enum constant 'When.MAYBE'" warning
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
}

group = "org.usvm.approximations.java.stdlib"
version = "0.0.0"

tasks.withType<JavaCompile> {
    //options.release = 8
    //options.compilerArgs.add("--add-exports=java.base/sun.nio.ch=ALL-UNNAMED")
    options.compilerArgs.add("-target")
    options.compilerArgs.add("1.8")
    options.compilerArgs.add("-source")
    options.compilerArgs.add("1.8")
    options.compilerArgs.add("-Xlint:unchecked")
    options.compilerArgs.add("-Xlint:all")
    options.compilerArgs.add("-Werror")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
