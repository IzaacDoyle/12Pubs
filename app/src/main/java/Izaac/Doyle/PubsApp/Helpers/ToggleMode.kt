package Izaac.Doyle.PubsApp.Helpers

import androidx.appcompat.app.AppCompatDelegate

 fun setTheme( themeCode: String) {
    if (themeCode.equals("day", ignoreCase = true)) {
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO
        )
    }
     if (themeCode.equals("night",ignoreCase = true)){
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_YES
        )
    }
    if (themeCode.equals("default",ignoreCase = true)){
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        )
    }
}