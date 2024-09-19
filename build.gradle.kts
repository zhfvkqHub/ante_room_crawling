import com.github.gradle.node.npm.task.NpmTask

plugins {
    id("java")
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
    id("com.github.node-gradle.node") version "5.0.0"
}

group = "com.anteprj"
version = "1.0-SNAPSHOT"

val frontendResourceDir = "${project.projectDir}/frontend"

tasks.jar {
    enabled = false
}

node {
    download.set(true)
    version.set("22.7.0")
    yarnVersion.set("1.22.22")
    workDir.set(file("${project.projectDir}/.gradle/nodejs"))
    yarnWorkDir.set(file("${project.projectDir}/.gradle/yarn"))
    npmWorkDir.set(file("${project.projectDir}/.gradle/npm"))
    nodeProjectDir.set(file(frontendResourceDir))
}

tasks.register<NpmTask>("setUp") {
    group = "Frontend"
    description = "Install Node.js packages"
    args.set(listOf("install"))
    inputs.files(file("$frontendResourceDir/package.json"))
    outputs.dir(file("$frontendResourceDir/node_modules"))
}

tasks.register<NpmTask>("buildFrontEnd") {
    group = "Frontend"
    dependsOn(tasks.named("setUp"))
    description = "Build vue.js"
    args.set(listOf("run", "build"))
    inputs.dir(file(frontendResourceDir))
    outputs.dir(file("$frontendResourceDir/dist"))
}

tasks.register<Copy>("copyVueBuild") {
    group = "Frontend"
    description = "Copy vue.js build files"
    dependsOn(tasks.named("buildFrontEnd"))
    from(file("$frontendResourceDir/dist"))
    into(file("${project.projectDir}/src/main/resources/static"))
}

tasks.named<ProcessResources>("processResources") {
    dependsOn(tasks.named("copyVueBuild"))
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Object Mapping
    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.3.Final")

    // mariadb
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")

    // selenium
    implementation("org.seleniumhq.selenium:selenium-java:4.18.1")
    implementation("org.seleniumhq.selenium:selenium-chrome-driver:4.18.1")

    // firebase
    implementation("com.google.firebase:firebase-admin:9.1.1")

    //querydsl
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.jsoup:jsoup:1.15.3")
}

tasks.test {
    useJUnitPlatform()
}
