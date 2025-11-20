########## PUBLIC API (VISIBLE TO CONSUMERS) ##########

# Keep the main fragment API (fully visible)
-keep class com.deep.base.BaseBottomSheetFragment { *; }

# Keep the public utility object (fully visible)
-keep class com.deep.utils.PublicObjectExample { *; }


########## INTERNAL IMPLEMENTATION (HIDDEN / OBFUSCATED) ##########

# Anything in internal packages is free to obfuscate/remove
# No keep rules → R8 will hide them automatically.

# OPTIONAL: If your internal code references things that cause warnings
# (use ONLY if needed)
# -dontwarn com.deep.internal.**