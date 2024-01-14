plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    `maven-publish`
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    jvm()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        jvmTest.dependencies {
            implementation(libs.kotlin.logging)
            implementation(libs.logback.classic)
        }
    }

    publishing {
        repositories {
            maven {
                url = uri("https://github.com/JustBurrow/semantic-version")
                credentials {
                    username = project.findProperty("gpr.user") as String?
                        ?: System.getenv("USERNAME")
                    password = project.findProperty("gpr.key") as String?
                        ?: System.getenv("TOKEN")
                }
            }
        }
    }
}

android {
    namespace = "kr.lul.version"
    compileSdk = 34

    defaultConfig {
        minSdk = 27
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    @Suppress("UnstableApiUsage")
    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }

    dependencies {
        testImplementation(libs.junit)
        testImplementation(libs.kotlin.logging)
        testImplementation(libs.logback.classic)
    }
}

