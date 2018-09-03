# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn android.content.**
-dontwarn android.graphics.drawable.**
-dontwarn android.view.LayoutInflater
-dontwarn android.util.Xml

-dontwarn com.caverock.androidsvg.R$styleable
-dontwarn com.caverock.androidsvg.SVGImageView
-dontwarn com.caverock.androidsvg.SVGAndroidRenderer

-dontwarn javax.annotation.**

-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-dontwarn okhttp3.internal.platform.ConscryptPlatform
-dontwarn org.mapsforge.map.android.rotation.RotateView
-dontwarn org.mapsforge.map.android.rotation.SmoothCanvas
-keep class org.xmlpull.v1.** { *; }
