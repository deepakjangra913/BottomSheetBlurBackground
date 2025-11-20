# ============================================
# KEEP PUBLIC API - Readable names
# ============================================
# Keep your main public fragment
-keep public class com.deep.base.BaseBottomSheetFragment {
    public *;
    protected *;
}

# Keep public utilities
-keep public class com.deep.utils.PublicObjectExample {
    public *;
}

# Keep all public classes in base and utils packages
-keep public class com.deep.base.** { public *; protected *; }
-keep public class com.deep.utils.** { public *; }

# ============================================
# OBFUSCATE INTERNAL CLASSES
# ============================================
# Allow obfuscation of internal package
-keep,allowobfuscation class com.deep.internal.** { *; }

# Repackage obfuscated classes to short names
-repackageclasses 'com.deep.a'

# Allow aggressive optimization
-allowaccessmodification
-overloadaggressively

# ============================================
# REMOVE DEBUG LOGS (Optional)
# ============================================
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# ============================================
# KEEP VIEW BINDING CLASSES
# ============================================
-keep class * extends androidx.viewbinding.ViewBinding {
    public static *** inflate(...);
    public static *** bind(...);
}

# Specifically keep your DialogBaseBottomSheetBinding
-keep class com.deep.base.databinding.DialogBaseBottomSheetBinding { *; }
-keep class com.deep.base.databinding.** { *; }

# ============================================
# ANDROID DEFAULTS
# ============================================
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions

# Keep Fragment constructors
-keepclassmembers public class * extends androidx.fragment.app.Fragment {
    public <init>(...);
}

# Keep BottomSheetDialogFragment
-keep class * extends com.google.android.material.bottomsheet.BottomSheetDialogFragment {
    public <init>(...);
}