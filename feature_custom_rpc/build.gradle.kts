plugins {
    id ("zyro.android.library")
    id ("zyro.android.library.compose")
    id ("zyro.android.feature")
    id ("zyro.android.hilt")
}

android {
    namespace = "com.my.zyro.feature_custom_rpc"
}

dependencies {
    implementation (libs.material.icons.extended)
    implementation(libs.accompanist.permission)
    implementation(libs.activity.compose)
    implementation(libs.blankj.utilcodex)
    implementation(libs.coil)
    implementation(libs.kotlinx.serialization.json)
    implementation(projects.featureRpcBase)
    implementation(projects.featureProfile)
}