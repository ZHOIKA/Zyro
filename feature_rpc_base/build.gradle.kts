plugins {
    id ("zyro.android.library")
    id ("zyro.android.feature")
    id ("zyro.android.hilt")
}

android {
    namespace = "com.my.zyro.feature_rpc_base"
}

dependencies {
    implementation (libs.blankj.utilcodex)
    implementation(libs.androidx.material)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.coil)
}