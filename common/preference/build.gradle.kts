plugins {
    id ("zyro.android.library")
    id ("zyro.android.library.compose")
}

android {
    namespace = "com.my.zyro.preference"
}

dependencies {
    implementation(projects.color)
    implementation(projects.domain)
    implementation(projects.common.resources)
    implementation(libs.material3)
    implementation(libs.mmkv)
    implementation(libs.kotlinx.coroutine)
    implementation(libs.compose.ui)
    implementation(libs.kotlinx.serialization.json)
}