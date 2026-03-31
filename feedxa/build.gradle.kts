plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    id("maven-publish")
}

android {
    namespace = "com.asb.feedxa"
    compileSdk = 36  // ✅ تصحيح: رقم API فقط

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()  // ✅ إضافة Javadoc (اختياري)
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.material3)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.activity.compose)
    implementation(libs.coil.compose)
    implementation(libs.material.icons.extended)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.ui)
    // تم إزالة المكرر: implementation(libs.androidx.compose.material3.v120)
}

// ✅ تعريف group و version مرة واحدة فقط
group = "com.github.amralsaidy"
version = "1.0.2"

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = group.toString()
                artifactId = "feedxa"
                version = version.toString()

                // ✅ إضافة POM info للمساعدة في التوثيق
                pom {
                    name.set("FeedXA")
                    description.set("Android Feed library with Compose support")
                    url.set("https://github.com/amralsaidy/YOUR_REPO_NAME")
                    licenses {
                        license {
                            name.set("Apache-2.0")
                            url.set("https://opensource.org/licenses/Apache-2.0")
                        }
                    }
                    developers {
                        developer {
                            id.set("amralsaidy")
                            name.set("Amr AlSaidy")
                        }
                    }
                }
            }
        }
    }
}