plugins {
    id ("zyro.android.library")
    id ("zyro.android.library.compose")
    id ("zyro.android.feature")
}

android {
    namespace = "com.my.zyro.feature_home"

    defaultConfig {
        buildConfigField(
            "String",
            "VERSION_NAME",
            "\"${libs.versions.version.name.get()}\""
        )
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.accompanist.flowLayout)
    implementation(libs.material.icons.extended)
    implementation(projects.featureRpcBase)
    implementation(projects.featureSettings)
    implementation(projects.common.navigation)
    implementation(libs.coil)
    implementation(libs.activity.compose)
    implementation(libs.androidx.hilt.navigation.compose)
}