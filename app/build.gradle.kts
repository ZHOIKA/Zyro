@file:Suppress("UnstableApiUsage")

plugins {
    id("zyro.android.application")
    id("zyro.android.application.compose")
    id("zyro.android.hilt")
}

import java.io.File


android {

    namespace = "com.my.zyro"

    defaultConfig {

        applicationId = "com.my.zyro"

        versionCode = libs.versions.version.code.get().toInt()
        versionName = libs.versions.version.name.get()

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }


    signingConfigs {

        create("release") {

            // GitHub Actions
            val keystorePath =
                System.getenv("KEYSTORE_FILE")


            if (keystorePath != null) {

                storeFile = File(keystorePath)

                storePassword =
                    System.getenv("KEYSTORE_PASSWORD")

                keyAlias =
                    System.getenv("KEY_ALIAS")

                keyPassword =
                    System.getenv("KEY_PASSWORD")

            }

            // Compilação local
            else {

                val localKeystore =
                    rootProject.file("keystore.properties")


                if (localKeystore.exists()) {

                    val props =
                        java.util.Properties()

                    props.load(
                        localKeystore.inputStream()
                    )


                    storeFile =
                        file(
                            props["storeFile"] as String
                        )


                    storePassword =
                        props["storePassword"] as String


                    keyAlias =
                        props["keyAlias"] as String


                    keyPassword =
                        props["keyPassword"] as String
                }
            }
        }
    }



    buildTypes {


        debug {

            applicationIdSuffix = ".debug"

        }



        release {


            signingConfig =
                signingConfigs.getByName("release")


            isMinifyEnabled = true

            isShrinkResources = true


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

            excludes +=
                "/META-INF/{AL2.0,LGPL2.1}"

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



    implementation(libs.app.compat)
    implementation(libs.accompanist.navigation.animation)
    implementation(libs.kotlinx.serialization.json)



    implementation(libs.material3)
    implementation(libs.androidx.material)
    implementation(libs.material3.windows.size)

}
