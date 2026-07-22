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
    }



    signingConfigs {

        create("release") {

            val keystorePropertiesFile =
                rootProject.file("keystore.properties")


            if (!keystorePropertiesFile.exists()) {

                throw GradleException(
                    "❌ keystore.properties não encontrado"
                )
            }


            val properties = Properties()


            properties.load(
                keystorePropertiesFile.inputStream()
            )


            val keystoreName =
                properties.getProperty("storeFile")


            if (keystoreName.isNullOrEmpty()) {

                throw GradleException(
                    "❌ storeFile não definido"
                )
            }



            storeFile =
                rootProject.file(keystoreName)


            if (!storeFile.exists()) {

                throw GradleException(
                    "❌ Keystore não encontrado: $storeFile"
                )
            }



            storePassword =
                properties.getProperty("storePassword")


            keyAlias =
                properties.getProperty("keyAlias")


            keyPassword =
                properties.getProperty("keyPassword")



            println(
                "✅ Keystore carregado: $storeFile"
            )
        }
    }




    buildTypes {


        debug {

            applicationIdSuffix =
                ".debug"
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




    // Extras

    implementation(libs.app.compat)

    implementation(libs.accompanist.navigation.animation)

    implementation(libs.kotlinx.serialization.json)




    // Material

    implementation(libs.material3)

    implementation(libs.androidx.material)

    implementation(libs.material3.windows.size)
}
