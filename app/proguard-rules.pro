# Add project specific ProGuard rules here.

# Keep Room generated code
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Keep Moshi models & codegen
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-keep class com.squareup.moshi.** { *; }
-keepclassmembers class * {
    @com.squareup.moshi.Json *;
    @com.squareup.moshi.JsonClass *;
}

# Keep Retrofit models & interfaces
-keepattributes Signature
-keepattributes Exceptions
-keepclassmembers class * {
    @retrofit2.http.* <methods>;
}

# Firebase & App Check rules
-keep class com.google.firebase.** { *; }

# Compose rules
-keepclassmembers class * extends androidx.compose.ui.node.LayoutNode { *; }
