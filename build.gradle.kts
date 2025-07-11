buildscript {
    extra.apply {
        set("compile_sdk", 34)
        set("target_sdk", 34)
        set("min_sdk", 29)
        set("kotlin_version", "1.9.20")
        set("compose_bom_version", "2023.10.01")
        set("compose_compiler_version", "1.5.5")
    }
}

plugins {
    id("com.android.application") version "8.11.1" apply false
    id("com.android.library") version "8.11.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
    id("com.google.devtools.ksp") version "1.9.20-1.0.14" apply false
    id("org.jetbrains.kotlin.plugin.parcelize") version "1.9.20" apply false
    id("com.google.dagger.hilt.android") version "2.48.1" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}