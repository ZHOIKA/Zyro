plugins {
    id ("zyro.android.library")
    id ("zyro.android.library.compose")
    id ("zyro.android.feature")
    id ("zyro.android.hilt")
}

android {
    namespace = "com.my.zyro.feature_console_rpc"
}

dependencies {
    implementation (projects.featureRpcBase)
    implementation (libs.material.icons.extended)
    implementation (libs.coil)
    implementation (libs.activity.compose)
    implementation (libs.kotlinx.serialization.json)
}