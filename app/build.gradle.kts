import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id(Plugins.androidApp)
    kotlin(Plugins.kotlinAndroid)
    kotlin(Plugins.kotlinExt)
    kotlin(Plugins.kotlinApt)
    id(Plugins.detekt).version(Versions.detekt)
//    id(Plugins.googleServices)
}

buildscript {
    apply(from = "../ktlint.gradle.kts")
    apply(from = "../autodimension.gradle.kts")
}

//tasks {
//    kotlin.check {
//        dependsOn("ktlintCheck")
//        dependsOn("ktlintFormat")
//    }
//}

android {
    compileSdkVersion(Versions.compile_sdk_version)
    buildToolsVersion(Versions.build_tools_version)
    defaultConfig {
        applicationId = "com.vtnd.lus"
        minSdkVersion(Versions.min_sdk_version)
        targetSdkVersion(Versions.target_sdk_version)
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles(
            file("proguard-rules.pro")
        )
        val properties = Properties()
        if (rootProject.file("local.properties").exists()) {
            properties.load(rootProject.file("local.properties").inputStream())
        }
        // Inject the Maps API key into the manifest
        manifestPlaceholders["mapsApiKey"] = properties.getProperty("MAPS_API_KEY", "")
    }

    flavorDimensions("appVariant")
    productFlavors {
        create("dev") {
            dimension = "appVariant"
            applicationIdSuffix = ".dev"
            resValue("string", "app_name", "LUS Dev")
            buildConfigField("String", "BASE_URL", "\"https://lus-sever.herokuapp.com/\"")
        }
        create("prd") {
            dimension = "appVariant"
            resValue("string", "app_name", "LUS")
            buildConfigField(
                "String",
                "BASE_URL",
                "\"https://lus-sever.herokuapp.com/\""
            )
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("../keystores/lus-company")
            storePassword = ""
            keyAlias = ""
            keyPassword = ""
        }
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            versionNameSuffix = "-Debug"
            resValue("string", "app_name", "Lus-Dev")
        }
        getByName("release") {
            isDebuggable = false
            isZipAlignEnabled = true
            isShrinkResources = true
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
            setProguardFiles(
                    setOf(
                            getDefaultProguardFile("proguard-android.txt"),
                            "proguard-rules.pro"
                    )
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    androidExtensions {
        isExperimental = true
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
    buildFeatures{
        dataBinding = true
    }

    kapt {
        useBuildCache = true
    }

    configurations.all {
        resolutionStrategy.force("com.google.code.findbugs:jsr305:1.3.9")
    }

    lintOptions {
        disable("MissingTranslation")
        check("Interoperability")
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Deps.kotlin_stdlib)
    implementation(Deps.support_app_compat)

    implementation(Deps.support_design)
    implementation(Deps.support_constraintLayout)
    implementation(Deps.support_annotations)
    implementation(Deps.support_recyclerview)
    implementation(Deps.support_cardview)
    implementation(Deps.support_core_ktx)
    implementation(Deps.swiperefreshlayout)
    implementation(Deps.glide_runtime)
    implementation(Deps.glide_drawable)
    kapt(Deps.glide_compiler)
    implementation(Deps.glide_integration)
    implementation(Deps.dexter) //Permissionx

    // androidx preference
    implementation(Deps.preference)
    implementation(Deps.legacy_preference)
    //Json
    implementation(Deps.retrofit_runtime)
    implementation(Deps.okhttp_logging_interceptor)
    implementation(Deps.okhttp)
    implementation(Deps.moshiAdapter)
    implementation(Deps.moshiKotlin)
    implementation(Deps.converterMoshi)
    kapt(Deps.moshiCodegen)

    //Coroutine
    implementation(Deps.coroutines_core)
    implementation(Deps.coroutines_android)
    implementation(Deps.support_core_ktx)
    testImplementation(Deps.coroutines_test)

    //Lifecycler
    implementation(Deps.lifecycle_extension)
    implementation(Deps.lifecycle_livedata_ktx)
    implementation(Deps.lifecycle_viewmodel_ktx)
    kapt(Deps.lifecycle_compiler)


    //Navigation
    implementation(Deps.navigation_fragment)
    implementation(Deps.navigation_ui)
    implementation(Deps.navigation_fragment_ktx)
    implementation(Deps.navigation_ui_ktx)

    //Room
    implementation(Deps.room_runtime)
    implementation(Deps.room_testing)
    implementation(Deps.room_ktx)
    kapt(Deps.room_compiler)

    //Koin
    implementation(Deps.koin_ext)
    implementation(Deps.koin_android)
    implementation(Deps.koin_viewmodel)
    kapt(Deps.support_databinding)

    //Test
    implementation(Deps.mockito_inline)
    implementation(Deps.mockito_kotlin)
    implementation(Deps.arch_core_testing)
    implementation(Deps.junit)
    androidTestImplementation(Deps.atsl_runner)
    androidTestImplementation(Deps.espresso_core)

    //Firebase
    implementation("com.google.android.gms:play-services-maps:17.0.0")
    implementation("com.google.android.gms:play-services-location:17.1.0")
    implementation("com.google.android.gms:play-services-places:17.0.0")
    implementation("com.google.android.libraries.places:places-compat:2.4.0")
    //implementation(Deps.firebase_analytics)
    implementation("com.github.florent37:shapeofview:1.4.7")
    implementation("com.amulyakhare:com.amulyakhare.textdrawable:1.0.1")
    //Circle ImageView
    implementation(Deps.circle_image)

    //Timber
    implementation(Deps.timber)
    //OTP VIew
    implementation(Deps.otp_view)

    //Socket io
    implementation(Deps.socketio) {
        exclude("org.json", "json")
    }

    //dotsloader
    implementation("com.agrawalsuneet.androidlibs:dotsloader:1.4")
}
