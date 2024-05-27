plugins {
    id("java")
}

group = "io.github"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    val lombok_version = "1.18.32"
    compileOnly("org.projectlombok:lombok:" + lombok_version)
    annotationProcessor("org.projectlombok:lombok:" + lombok_version)
}

tasks.test {
    useJUnitPlatform()
}