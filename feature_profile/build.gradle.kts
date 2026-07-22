plugins {
    id ("zyro.android.library")
    id ("zyro.android.library.compose")
    id ("zyro.android.feature")
    id ("zyro.android.hilt")
}

android {
    namespace = "com.my.zyro.feature_profile"
}

dependencies {
    implementation (projects.theme)
    implementation (projects.gateway)
    implementation (libs.coil)
    implementation (libs.activity.compose)
    implementation (libs.kotlinx.serialization.json)
}