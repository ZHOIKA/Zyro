plugins {
    id ("zyro.android.library")
    id ("zyro.android.library.compose")
    id ("zyro.android.feature")
}

android {
    namespace = "com.my.zyro.feature_crash_handler"
}

dependencies {
    implementation (libs.activity.compose)
    implementation(libs.crashx)
    implementation (libs.blankj.utilcodex)
    implementation (libs.material.icons.extended)
    implementation (projects.theme)
}