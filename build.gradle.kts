// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()
        maven(url = "https://maven.fabric.io/public")
    }

    dependencies {
        classpath(ClassPaths.android_gradle_plugin)
        classpath(ClassPaths.kotlin_gradle_plugin)
        classpath(ClassPaths.navigation_safeargs_plugin)
        classpath(ClassPaths.oss_licenses_plugin)
//        classpath(ClassPaths.google_service_plugin)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "https://www.jitpack.io")
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        maven(url = "https://maven.google.com")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
