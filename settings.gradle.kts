import java.util.Properties

val localProps = Properties().apply {
    val file = File(rootDir, "local.properties")
    if (file.exists()) load(file.inputStream())
}

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // VK ID SDK
        maven { url = uri("https://artifactory-external.vkpartner.ru/artifactory/vkid-sdk-android/") }
        // Yandex MapKit (требует GitHub авторизацию)
        maven {
            url = uri("https://maven.pkg.github.com/yandex/mapkit-android-demo")
            credentials {
                username = localProps["github_user"]?.toString() ?: ""
                password = localProps["github_token"]?.toString() ?: ""
            }
        }
    }
}

rootProject.name = "mobileedv-firebase"
include(":app")
include(":core")
include(":core:navigation")
include(":core:ui")
include(":habits:domain")
include(":habits:data")
include(":habits:ui")
include(":stats:domain")
include(":stats:ui")
