import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN

val kotlinVersion = "1.3.72"

plugins {
    kotlin("jvm") version "1.3.72"
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
    java
    idea
    application
}


application {
    mainClassName = "org.jlleitschuh.gradle.ktlint.sample.kotlin.MainKt"
}


repositories {
    mavenCentral()
    jcenter()
}


ktlint {
  //  additionalEditorconfigFile.set(File("/Users/dev/.editorconfig"))
    debug.set(true)
    verbose.set(true)
   // android.set(false)
    outputToConsole.set(true)
    outputColorName.set("RED")
    ignoreFailures.set(true)
   // enableExperimentalRules.set(true)
    reporters {
        reporter(PLAIN)
        reporter(CHECKSTYLE)
    }

    kotlinScriptAdditionalPaths {
        include(fileTree("src"))
    }

    filter {
        exclude("**/generated/**")
        exclude("**/build/**")
    }
}

dependencies {

    implementation(kotlin("stdlib"))

    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

/*

tasks.withType<CreateStartScripts>() {
    this.applicationName = "higher-order-functions"
    // this.classpath?.plus(outputDir("))
}*/

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
