repositories {
    jcenter()
}

val ktlint by configurations.creating
val ktlintCheck by tasks.creating(JavaExec::class) {
    group = "verification"
    description = "Check Kotlin code style."
    classpath = configurations.getByName("ktlint")
    main = "com.pinterest.ktlint.Main"
    args = listOf("**/src/**/*.kt", "--reporter=html,output=$buildDir/reports/ktlint/ktlint.html")
    args = listOf(
        "src/**/*.kt",
        "--reporter=html,output=${project.rootDir}/.framgia-ci-reports/ktlint/ktlint.html"
    )
}

val ktlintFormat by tasks.creating(JavaExec::class) {
    group = "formatting"
    description = "Fix Kotlin code style deviations."
    classpath = configurations.getByName("ktlint")
    main = "com.pinterest.ktlint.Main"
    args = listOf("-F", "**/*.kt")
}

dependencies {
    ktlint(Deps.ktlint)
}
