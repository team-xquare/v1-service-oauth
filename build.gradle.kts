import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.7"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"
    kotlin("kapt") version "1.6.21"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2021.0.2"

dependencies {
    // jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // amqp
    implementation("org.springframework.boot:spring-boot-starter-amqp")

    // jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // sleuth
    implementation("org.springframework.cloud:spring-cloud-sleuth-zipkin")
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")

    // mysql
    runtimeOnly("mysql:mysql-connector-java")

    // configuration processor
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // oauth
    implementation("org.springframework.security:spring-security-oauth2-authorization-server:0.2.3")
    implementation("com.nimbusds:nimbus-jose-jwt:9.22")

    // jpa
    implementation("org.springframework.data:spring-data-jpa")
    runtimeOnly("mysql:mysql-connector-java")
    implementation("com.vladmihalcea:hibernate-types-52:2.16.2")

    // http client
    implementation("org.apache.httpcomponents:httpclient:4.5.13")

    // validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // secondary cache
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
