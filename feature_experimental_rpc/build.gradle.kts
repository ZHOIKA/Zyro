plugins {
    id ("zyro.android.library")
    id ("zyro.android.library.compose")
    id ("zyro.android.hilt")
    id ("zyro.android.feature")
}

android {
    namespace = "com.my.zyro.feature_experimental_rpc"
}

dependencies {
    implementation(libs.material.icons.extended)
    implementation(libs.activity.compose)
    implementation(libs.blankj.utilcodex)
    implementation(libs.coil)
    implementation(projects.featureRpcBase)
}
