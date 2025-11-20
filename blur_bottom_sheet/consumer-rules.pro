# ============================================
# CONSUMER APP RULES
# These rules are applied in the consumer's app
# ============================================

# Keep public API
-keep public class com.deep.base.** {
    public *;
    protected *;
}

-keep public class com.deep.utils.** {
    public *;
}

# Don't warn about obfuscated internal classes
-dontwarn com.deep.a.**
-dontwarn com.deep.internal.**

# Keep ViewBinding for consumers
-keep class com.deep.base.databinding.** { *; }