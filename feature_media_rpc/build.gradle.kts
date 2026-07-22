plugins {
    id ("zyro.android.library")
    id ("zyro.android.library.compose")
    id ("zyro.android.feature")
}

android {
    namespace = "com.my.zyro.feature_media_rpc"
}

dependencies {
    implementation(projects.featureRpcBase)
    implementation(libs.material.icons.extended)
}