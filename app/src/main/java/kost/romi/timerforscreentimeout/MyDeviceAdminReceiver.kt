package kost.romi.timerforscreentimeout

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import timber.log.Timber

class MyDeviceAdminReceiver : DeviceAdminReceiver() {

    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
//        Toast.makeText(context, "Admin is enabled", Toast.LENGTH_SHORT).show()
        Timber.d("class MyDeviceAdminReceiver : DeviceAdminReceiver() { : Admin is enabled")
    }

}