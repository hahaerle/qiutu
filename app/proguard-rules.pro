# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-dontwarn android.support.**
-dontwarn com.umeng.**
-dontwarn org.androidannotations.api.**

-keep public class * extends android.app.Activity
-keep public class * extends android.view.View
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service

-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * {
public <init>(android.content.Context);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}


-keepattributes *Annotation*
-keepclassmembers class ** {
    public void onEvent*(**);
}
#shareSdk
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-dontwarn cn.sharesdk.**
#shareSdk
#JPush
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
#JPush
-keep class de.greenrobot.event.**{ *;}
-keep class com.umeng.** { *;}
-keep class android.support.v4.view.**{ *;}
