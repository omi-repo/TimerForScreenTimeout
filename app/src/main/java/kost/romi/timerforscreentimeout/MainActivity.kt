package kost.romi.timerforscreentimeout

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import timber.log.Timber
import timber.log.Timber.DebugTree


class MainActivity : AppCompatActivity() {

    // Interaction with the DevicePolicyManager
    var mDPM: DevicePolicyManager? = null
    var mDeviceAdmin: ComponentName? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Placeholder for fragment
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        navHostFragment.navController

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        // Prepare to work with the Device Policy Manager
        // Prepare to work with the Device Policy Manager
        mDPM = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        mDeviceAdmin = ComponentName(this, MyDeviceAdminReceiver::class.java)

        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdmin)
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "EXPLANATION")
        startActivityForResult(intent, 0)
        MyDeviceAdminReceiver().onEnabled(this, intent)

        // Binding to activity_main.xml
//        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
//        setContentView(binding.root)

    }

}