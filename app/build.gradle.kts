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

        create("release") {

            val propertiesFile =
                rootProject.file("keystore.properties")


            if (!propertiesFile.exists()) {

                throw GradleException(
                    "❌ keystore.properties não encontrado"
                )
            }


            val props = Properties()


            props.load(
                propertiesFile.inputStream()
            )


            val keystorePath =
                props.getProperty("storeFile")


            if (keystorePath.isNullOrBlank()) {

                throw GradleException(
                    "❌ storeFile não configurado"
                )
            }


            val releaseKeystore =
                rootProject.file(keystorePath)


            if (!releaseKeystore.exists()) {

                throw GradleException(
                    "❌ Arquivo keystore não encontrado: $releaseKeystore"
                )
            }


            storeFile = releaseKeystore


            storePassword =
                props.getProperty("storePassword")


            keyAlias =
                props.getProperty("keyAlias")


            keyPassword =
                props.getProperty("keyPassword")


            println(
                "✅ Release keystore: $releaseKeystore"
            )
        }
    }





    buildTypes {


        debug {

            applicationIdSuffix = ".debug"
        }



        release {


            signingConfig =
                signingConfigs.getByName("release")


            isMinifyEnabled = false

            isShrinkResources = false



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



    // Extras

    implementation(libs.app.compat)

    implementation(libs.accompanist.navigation.animation)

    implementation(libs.kotlinx.serialization.json)



    // Material

    implementation(libs.material3)

    implementation(libs.androidx.material)

    implementation(libs.material3.windows.size)
}