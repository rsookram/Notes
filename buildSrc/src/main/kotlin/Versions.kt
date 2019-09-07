object Versions {

    private const val kotlin = "1.3.50"
    private const val kotlinCoroutines = "1.3.0"
    private const val epoxy = "3.7.0"
    private const val room = "2.1.0"

    const val minSdk = 25
    const val targetSdk = 28

    const val androidGradlePlugin = "com.android.tools.build:gradle:3.5.0"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin"

    const val appCompat = "androidx.appcompat:appcompat:1.1.0"
    const val materialComponents = "com.google.android.material:material:1.0.0"
    const val coreKtx = "androidx.core:core-ktx:1.0.2"
    const val activityKtx = "androidx.activity:activity-ktx:1.0.0"
    const val lifecycleViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.1.0"

    const val inboxRecyclerView = "me.saket:inboxrecyclerview:1.0.0"

    const val epoxyCore = "com.airbnb.android:epoxy:$epoxy"
    const val epoxyProcessor = "com.airbnb.android:epoxy-processor:$epoxy"

    const val roomRuntime = "androidx.room:room-runtime:$room"
    const val roomKtx = "androidx.room:room-ktx:$room"
    const val roomCompiler = "androidx.room:room-compiler:$room"

    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlin"
    const val kotlinCoroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutines"
    const val kotlinCoroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinCoroutines"
}
