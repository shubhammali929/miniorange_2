plugins {
    id("com.android.library")
    id("maven-publish")
}

android {
    namespace = "com.andorid.miniorange"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
                "consumer-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("com.android.volley:volley:1.2.1")
    implementation("org.bouncycastle:bcprov-jdk15on:1.70")

}
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                artifact("$buildDir/outputs/aar/miniorange-release.aar")

                groupId = "com.github.shubhammali929"
                artifactId = "miniorange"
                version = "2.0"

                // Include dependencies in the POM file
                pom {
                    withXml {
                        asNode().appendNode("dependencies").apply {
                            configurations.implementation.get().dependencies.forEach { dep ->
                                appendNode("dependency").apply {
                                    appendNode("groupId", dep.group)
                                    appendNode("artifactId", dep.name)
                                    appendNode("version", dep.version)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    tasks.named("publishReleasePublicationToMavenLocal") {
        dependsOn(tasks.named("bundleReleaseAar"))
    }
}