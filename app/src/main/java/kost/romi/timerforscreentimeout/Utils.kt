package kost.romi.timerforscreentimeout

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar

/**
 * Helper functions to simplify permission checks/requests.
 */
fun Context.hasPermission(permission: String): Boolean {
    // Background permissions didn't exit prior to Q, so it's approved by default.
    if (permission == Manifest.permission.WRITE_SETTINGS &&
        android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
        return true
    }

    return ActivityCompat.checkSelfPermission(this, permission) ==
            PackageManager.PERMISSION_GRANTED
}
