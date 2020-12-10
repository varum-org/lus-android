@Suppress("unused")
object Versions {
    const val kotlin = "1.4.10"
    const val android_gradle_plugin = "4.0.1"

    const val compile_sdk_version = 30
    const val build_tools_version = "30.0.1"
    const val min_sdk_version = 23
    const val target_sdk_version = 30

    const val appcompat = "1.2.0"
    const val security_crypto = "1.0.0-alpha02"
    const val arch_core = "2.1.0"
    const val androidx_test = "1.2.0"
    const val androidx_test_ext = "1.1.1"
    const val core_ktx = "1.1.0"
    const val espresso = "3.2.0"
    const val fragment = "1.3.0-alpha05"
    const val glide = "4.11.0"
    const val glide_drawable = "1.2.21"
    const val junit = "4.12"
    const val lifecycle = "2.3.0-alpha06"
    const val mockito = "2.25.0"
    const val powermock = "2.0.2"
    const val mockito_kotlin = "2.2.0"
    const val mockwebserver = "3.8.1"
    const val navigation = "2.2.0-rc02"
    const val okhttp = "4.5.0"
    const val retrofit = "2.6.2"
    const val moshi = "1.9.3"
    const val converter_moshi = "2.4.0"
    const val room = "2.2.5"
    const val room_coroutines = "2.1.0-alpha04"
    const val support = "1.0.0"
    const val constraint_layout = "2.0.2"
    const val material = "1.3.0-alpha03"
    const val work = "2.2.0"
    const val koin = "2.1.6"
    const val firebase_analytics = "17.2.2"
    const val google_service_plugin = "4.3.3"
    const val dexter = "6.0.2"
    const val swiperefreshlayout = "1.1.0-alpha03"
    const val viewpager2 = "1.0.0"
    const val ktlint = "0.36.0"
    const val detekt = "1.3.0"
    const val oss_licenses_plugin = "0.10.0"
    const val oss_licenses_plugin_version = "17.0.0"
    const val play_services_location = "17.0.0"
    const val firebase_messaging = "20.1.0"
    const val mp_android_chart = "v3.1.0"
    const val lottie = "3.3.1"
    const val qr_code = "1.9.8"
    const val anko = "0.10.8"
    const val coroutines = "1.4.2"
    const val databinding = "2.3.1"
    const val circle_image = "3.1.0"
    const val preference= "1.1.1"
    const val legacy_preference = "1.0.0"
    const val timber = "4.7.1"
    const val otp_view = "2.1.2"
    const val socketio = "1.0.0"
}

@Suppress("unused")
object ClassPaths {
    const val android_gradle_plugin =
        "com.android.tools.build:gradle:${Versions.android_gradle_plugin}"
    const val kotlin_gradle_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val navigation_safeargs_plugin =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
    const val google_service_plugin =
        "com.google.gms:google-services:${Versions.google_service_plugin}"
    const val oss_licenses_plugin =
        "com.google.android.gms:oss-licenses-plugin:${Versions.oss_licenses_plugin}"

}

@Suppress("unused")
object Plugins {
    const val androidApp = "com.android.application"
    const val kotlin = "kotlin"
    const val kotlinAndroid = "android"
    const val kotlinExt = "android.extensions"
    const val kotlinApt = "kapt"
    const val javalib = "java-library"
    const val androidLib = "com.android.library"
    const val navigationSafeArgs = "androidx.navigation.safeargs.kotlin"
    const val googleServices = "com.google.gms.google-services"
    const val ossLicenses = "com.google.android.gms.oss-licenses-plugin"
    const val detekt = "io.gitlab.arturbosch.detekt"
    const val fabric = "io.fabric"
}

@Suppress("unused")
object Deps {
    const val support_annotations = "androidx.annotation:annotation:${Versions.support}"
    const val support_app_compat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val support_recyclerview = "androidx.recyclerview:recyclerview:${Versions.support}"
    const val support_cardview = "androidx.cardview:cardview:${Versions.support}"
    const val support_constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraint_layout}"
    const val support_databinding = "com.android.databinding:compiler:${Versions.databinding}"

    const val support_design = "com.google.android.material:material:${Versions.material}"
    const val support_core_utils = "androidx.legacy:legacy-support-core-utils:${Versions.support}"
    const val support_core_ktx = "androidx.core:core-ktx:${Versions.core_ktx}"
    const val support_fragment_runtime = "androidx.fragment:fragment:${Versions.fragment}"
    const val support_fragment_runtime_ktx = "androidx.fragment:fragment-ktx:${Versions.fragment}"
    const val support_fragment_testing = "androidx.fragment:fragment-testing:${Versions.fragment}"

    const val security_crypto = "androidx.security:security-crypto:${Versions.security_crypto}"

    const val room_runtime = "androidx.room:room-runtime:${Versions.room}"
    const val room_compiler = "androidx.room:room-compiler:${Versions.room}"
    const val room_testing = "androidx.room:room-testing:${Versions.room}"
    const val room_coroutines = "androidx.room:room-coroutines:${Versions.room_coroutines}"
    const val room_ktx = "androidx.room:room-ktx:${Versions.room}"

    const val lifecycle_runtime = "androidx.lifecycle:lifecycle-runtime:${Versions.lifecycle}"
    const val lifecycle_java8 = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
    const val lifecycle_compiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"
    const val lifecycle_viewmodel_ktx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifecycle_extension = "androidx.lifecycle:lifecycle-extensions:2.2.0"
    const val lifecycle_livedata_ktx =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"

    const val arch_core_runtime = "androidx.arch.core:core-runtime:${Versions.arch_core}"
    const val arch_core_testing = "androidx.arch.core:core-testing:${Versions.arch_core}"

    const val retrofit_runtime = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofit_gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val retrofit_mock = "com.squareup.retrofit2:retrofit-mock:${Versions.retrofit}"
    const val okhttp_logging_interceptor =
        "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
    const val okhttp =
        "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
    const val moshi = "com.squareup.moshi:moshi:${Versions.moshi}"
    const val moshiAdapter = "com.squareup.moshi:moshi-adapters:${Versions.moshi}"
    const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    const val moshiCodegen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"
    const val converterMoshi = "com.squareup.retrofit2:converter-moshi:${Versions.converter_moshi}"

    const val espresso_core = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val espresso_contrib = "androidx.test.espresso:espresso-contrib:${Versions.espresso}"
    const val espresso_intents = "androidx.test.espresso:espresso-intents:${Versions.espresso}"

    const val atsl_core = "androidx.test:core:${Versions.androidx_test}"
    const val atsl_ext_junit = "androidx.test.ext:junit:${Versions.androidx_test_ext}"
    const val atsl_runner = "androidx.test:runner:${Versions.androidx_test}"
    const val atsl_rules = "androidx.test:rules:${Versions.androidx_test}"

    const val mockito_core = "org.mockito:mockito-core:${Versions.mockito}"
    const val mockito_inline = "org.mockito:mockito-inline:${Versions.mockito}"
    const val mockito_kotlin =
        "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockito_kotlin}"
    const val power_mock_mockito2 = "org.powermock:powermock-api-mockito2:${Versions.powermock}"
    const val power_mock_junit4 = "org.powermock:powermock-module-junit4:${Versions.powermock}"

    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val kotlin_test = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
    const val kotlin_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val kotlin_allopen = "org.jetbrains.kotlin:kotlin-allopen:${Versions.kotlin}"

    const val glide_runtime = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glide_drawable = "pl.droidsonroids.gif:android-gif-drawable:${Versions.glide_drawable}"
    const val glide_compiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    const val glide_integration = "com.github.bumptech.glide:okhttp3-integration:${Versions.glide}"

    const val junit = "junit:junit:${Versions.junit}"
    const val mock_web_server = "com.squareup.okhttp3:mockwebserver:${Versions.mockwebserver}"

    const val work_runtime = "androidx.work:work-runtime:${Versions.work}"
    const val work_testing = "androidx.work:work-testing:${Versions.work}"
    const val work_firebase = "androidx.work:work-firebase:${Versions.work}"
    const val work_runtime_ktx = "androidx.work:work-runtime-ktx:${Versions.work}"

    const val navigation_runtime = "androidx.navigation:navigation-runtime:${Versions.navigation}"
    const val navigation_runtime_ktx =
        "androidx.navigation:navigation-runtime-ktx:${Versions.navigation}"
    const val navigation_fragment = "androidx.navigation:navigation-fragment:${Versions.navigation}"
    const val navigation_fragment_ktx =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigation_ui = "androidx.navigation:navigation-ui:${Versions.navigation}"
    const val navigation_ui_ktx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val navigation_safe_args_plugin =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"

    const val koin_android = "org.koin:koin-androidx-scope:${Versions.koin}"
    const val koin_viewmodel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    const val koin_ext = "org.koin:koin-androidx-ext:${Versions.koin}"
    const val firebase_analytics =
        "com.google.firebase:firebase-analytics:${Versions.firebase_analytics}"
    const val dexter = "com.karumi:dexter:${Versions.dexter}"
    const val swiperefreshlayout =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swiperefreshlayout}"
    const val viewpager2 = "androidx.viewpager2:viewpager2:${Versions.viewpager2}"
    const val ktlint = "com.pinterest:ktlint:${Versions.ktlint}"
    const val play_services_oss_licenses =
        "com.google.android.gms:play-services-oss-licenses:${Versions.oss_licenses_plugin_version}"
    const val play_services_location =
        "com.google.android.gms:play-services-location:${Versions.play_services_location}"
    const val firebase_messaging =
        "com.google.firebase:firebase-messaging:${Versions.firebase_messaging}"
    const val youtube_player_api_path = "libs/YouTubeAndroidPlayerApi.jar"
    const val mp_android_chart = "com.github.PhilJay:MPAndroidChart:${Versions.mp_android_chart}"
    const val lottie = "com.airbnb.android:lottie:${Versions.lottie}"
    const val qr_code = "me.dm7.barcodescanner:zbar:${Versions.qr_code}"

    const val anko_core = "org.jetbrains.anko:anko:${Versions.anko}"
    const val anko_design = "org.jetbrains.anko:anko-design:${Versions.anko}"

    const val coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutines_android =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val coroutines_play_services =
            "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.coroutines}"
    const val coroutines_test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
    const val circle_image = "de.hdodenhof:circleimageview:${Versions.circle_image}"
    const val preference = "androidx.preference:preference-ktx:${Versions.preference}"
    const val legacy_preference =
        "androidx.legacy:legacy-preference-v14:${Versions.legacy_preference}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val otp_view = "com.github.mukeshsolanki:android-otpview-pinview:${Versions.otp_view}"
    const val socketio = "io.socket:socket.io-client:${Versions.socketio}"
}
