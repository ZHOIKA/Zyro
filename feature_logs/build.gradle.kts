plugins {
    id ("zyro.android.library")
    id ("zyro.android.library.compose")
    id ("zyro.android.feature")
    id ("zyro.android.hilt")
}

android {
    namespace = "com.my.zyro.feature_logs"
}

dependencies {
    implementation (projects.theme)
    implementation(libs.activity.compose)
}