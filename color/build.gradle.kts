
plugins {
    id ("zyro.android.library")
    id ("zyro.android.library.compose")
}
android {
    namespace = "com.zyro.color"
}
dependencies {
    api(libs.compose.ui)
    api(libs.core.ktx)
    api(libs.material3)
}