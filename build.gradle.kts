plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.kotlin.kapt") version "2.1.0"
}


group = "com.theapache64"
// [latest version - i promise!]
version = "1.1.2"

tasks.jar {
    this.project.projectDir
    val projectName = this.project.name
    val group = this.project.group
    archiveFileName.set("$projectName.main.jar")
    manifest {
        attributes["Main-Class"] = "${group}.$projectName.MainKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://jitpack.io") } // Add jitpack
}

dependencies {
    testImplementation("junit:junit:4.13.2")

    // Picocli A Mighty Tiny Command Line Interface : Java command line parser with both an annotations API and a programmatic API. Usage
    // help with ANSI styles and colors. Autocomplete. Nested subcommands. Easily included
    // as source to avoid adding a dependency.
    implementation("info.picocli:picocli:4.7.7")

    // Picocli Codegen : Picocli Code Generation - Tools to generate documentation, configuration, source
    // code and other files from a picocli model.
    kapt("info.picocli:picocli-codegen:4.7.7")

    // Dagger : A fast dependency injector for Android and Java.
    implementation("com.google.dagger:dagger:2.57.2")

    // Dagger Compiler : Tools to generate Dagger injection and module adapters from annotated code and validate
    // them.
    kapt("com.google.dagger:dagger-compiler:2.57.2")

    // Kotlinx Coroutines Core : Coroutines support libraries for Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

    // Retrofit : A type-safe HTTP client for Android and Java.
    implementation("com.squareup.retrofit2:retrofit:3.0.0")

    // Converter: Scalars : A Retrofit Converter for Java's scalar value types.
    implementation("com.squareup.retrofit2:converter-scalars:3.0.0")

    // Converter: Moshi : A Retrofit Converter which uses Moshi for serialization.
    implementation("com.squareup.retrofit2:converter-moshi:3.0.0")

    implementation("com.github.theapache64:retrosheet:2.0.0-beta03")

    // Moshi Kotlin Codegen : Moshi Kotlin Codegen
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.15.2")

    // Moshi : Moshi
    implementation("com.squareup.moshi:moshi:1.15.2")

    // Expekt : BDD assertion library for Kotlin
    testImplementation("com.winterbe:expekt:0.5.0")
}
