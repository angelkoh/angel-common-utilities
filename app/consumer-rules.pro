
-keepclassmembernames class * {
    public protected <methods>;
}

# keep class BuildConfig
-keep public class **.BuildConfig { *; }

# keep class members of R
-keepclassmembers class **.R$* {public static <fields>;}