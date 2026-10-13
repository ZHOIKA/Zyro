@file:Suppress("UnstableApiUsage")

import java.util.Properties

plugins {
    id("zyro.android.application")
    id("zyro.android.application.compose")
    id("zyro.android.hilt")
}


android {

    namespace = "com.my.zyro"


    defaultConfig {

        applicationId = "com.my.zyro"

        versionCode =
            libs.versions.version.code.get().toInt()

        versionName =
            libs.versions.version.name.get()


        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"


        vectorDrawables {
            useSupportLibrary = true
        }
        lint {
            abortOnError = false
        }
    }



    signingConfigs {
        val propertiesFile = rootProject.file("keystore.properties")
        if (propertiesFile.exists()) {
            create("release") {
                val props = Properties()
                props.load(propertiesFile.inputStream())
                val keystorePath = props.getProperty("storeFile")
                if (!keystorePath.isNullOrBlank()) {
                    val releaseKeystore = rootProject.file(keystorePath)
                    if (releaseKeystore.exists()) {
                        storeFile = releaseKeystore
                        storePassword = props.getProperty("storePassword")
                        keyAlias = props.getProperty("keyAlias")
                        keyPassword = props.getProperty("keyPassword")

                    }
                }
            }
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }

        release {
            signingConfigs.findByName("release")?.let {
                signingConfig = it
            }
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }




    packaging {

        resources {

            excludes +="/META-INF/{AL2.0,LGPL2.1}"
        }
    }




    dependenciesInfo {

        includeInApk = false

        includeInBundle = true
    }
}





dependencies {


    implementation(projects.domain)

    implementation(projects.theme)



    implementation(projects.featureStartup)

    implementation(projects.featureCrashHandler)

    implementation(projects.featureProfile)

    implementation(projects.featureAbout)

    implementation(projects.featureSettings)

    implementation(projects.featureLogs)



    implementation(projects.featureRpcBase)

    implementation(projects.featureAppsRpc)

    implementation(projects.featureMediaRpc)

    implementation(projects.featureConsoleRpc)

    implementation(projects.featureCustomRpc)

    implementation(projects.featureExperimentalRpc)

    implementation(projects.featureHome)



    implementation(projects.common.preference)

    implementation(projects.common.navigation)



    // Extras

    implementation(libs.app.compat)

    implementation(libs.accompanist.navigation.animation)

    implementation(libs.kotlinx.serialization.json)



    // Material

    implementation(libs.material3)

    implementation(libs.androidx.material)

    implementation(libs.material3.windows.size)
}

gradle.taskGraph.whenReady {
    val buildsReleaseApk = allTasks.any {
        it.project == project && (it.name == "assembleRelease" || it.name == "packageRelease" || it.name == "bundleRelease")
    }
    if (buildsReleaseApk) {
        val signing = android.signingConfigs.findByName("release")
        check(signing != null && signing.storeFile?.isFile == true && !signing.storePassword.isNullOrBlank() &&
            !signing.keyAlias.isNullOrBlank() && !signing.keyPassword.isNullOrBlank()) {
            "Release requires the permanent signing keystore and all signing properties."
        }
    }
}
