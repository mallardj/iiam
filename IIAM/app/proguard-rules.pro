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

#Common
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes Exceptions
-keep class javax.annotation.*.*{ *; }
-dontwarn javax.annotation.**
-keep class java.nio.*.*{ *; }
-dontwarn java.nio.**
-keep class java.lang.invoke.*.*{ *; }
-dontwarn java.lang.invoke.**

#Models
-keepclassmembers class com.app.iiam.models.*.* {*;}

#DB Class
-keep class com.app.iiam.database.entities.*.*
-keepclassmembers class com.app.iiam.database.entities.*.* { *; }
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#Timber
-dontwarn org.jetbrains.annotations.**

#Material
-keep class com.google.android.material.*.* { *; }
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**
-keep class androidx.*.* { *; }
-keep interface androidx.*.* { *; }

#Picasso
-dontwarn com.squareup.okhttp.**

#Video Load
-dontwarn com.yanzhenjie.album.**
-dontwarn com.yanzhenjie.mediascanner.**

#Pdf generator
-keep class org.spongycastle.*.* { *; }
-dontwarn org.spongycastle.**

#Exo player
-keep class com.google.android.exoplayer.*.* {*;}

#Firebase Crashlytics
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception





