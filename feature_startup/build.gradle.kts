plugins {
    id ("zyro.android.library")
    id ("zyro.android.library.compose")
    id ("zyro.android.feature")
}

android {
    namespace = "com.my.zyro.feature_startup"
}

dependencies {
    implementation (libs.activity.compose)
    implementation (libs.material.icons.extended)
    implementation (libs.accompanist.permission)
    implementation (libs.kotlinx.serialization.json)
}