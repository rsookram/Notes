buildscript {

    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath(Versions.androidGradlePlugin)
        classpath(Versions.kotlinGradlePlugin)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}
