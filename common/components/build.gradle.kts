plugins {
    id("zyro.android.library")
    id("zyro.android.library.compose")
}

android {
    namespace = "com.my.zyro.ui.components"
}

dependencies {
    implementation(projects.theme)
    implementation(libs.material3)
    implementation(projects.color)
    implementation(libs.android.svg)
    implementation(libs.coil)
    implementation(projects.common.resources)
    implementation(libs.material.icons.extended)
    implementation(libs.blankj.utilcodex)
}