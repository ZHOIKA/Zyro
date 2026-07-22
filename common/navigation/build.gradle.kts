plugins {
    id ("zyro.android.library")
    id ("zyro.android.library.compose")
}

android {
    namespace = "com.my.zyro.navigation"
}

dependencies {
    implementation (libs.compose.ui)
    implementation (libs.compose.navigation)
    implementation (libs.accompanist.navigation.animation)
}