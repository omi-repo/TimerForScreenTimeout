package kost.romi.timerforscreentimeout

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import kost.romi.timerforscreentimeout.databinding.ActivityMainBinding
import timber.log.Timber
import timber.log.Timber.DebugTree


class MainActivity : AppCompatActivity() {

    // Interaction with the DevicePolicyManager
    var mDPM: DevicePolicyManager? = null
    var mDeviceAdmin: ComponentName? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

//        checkIfUserIsDeviceOwner(savedInstanceState)

        // Prepare to work with the Device Policy Manager
        // Prepare to work with the Device Policy Manager
        mDPM = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        mDeviceAdmin = ComponentName(this, MyDeviceAdminReceiver::class.java)

        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdmin)
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "EXPLANATION")
        startActivityForResult(intent, 1)

        MyDeviceAdminReceiver().onEnabled(this, intent)

        // Binding to activity_main.xml
        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        // Placeholder for fragment
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        navHostFragment.navController

    }

    private fun checkIfUserIsDeviceOwner(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val manager = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager

            if (manager.isDeviceOwnerApp(applicationContext.packageName)) {

                // This app is set up as the device owner. Show the main features.
                Timber.i("The app is the device owner.")
//                Toast.makeText(this, "The app is the device owner.", Toast.LENGTH_SHORT).show()

                // Placeholder for fragment
                val navHostFragment = supportFragmentManager.findFragmentById(
                    R.id.nav_host_fragment
                ) as NavHostFragment
                navHostFragment.navController

            } else {

                // This app is not set up as the device owner. Show instructions.
                Timber.i("The app is not the device owner.")
//                Toast.makeText(this, "The app is not the device owner.", Toast.LENGTH_SHORT).show()

                var devicePolicyManager =
                    getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
                var componentName = getComponentName()
                var active: Boolean = devicePolicyManager.isAdminActive(componentName)
                if (active) {
//                    devicePolicyManager.removeActiveAdmin(componentName)
                } else {
                    intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
                    intent.putExtra(
                        DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        "You should enable the app!"
                    )
                    startActivityForResult(intent, RESULT_OK)
//                    registerForActivityResult(intent)
                }

                // Placeholder for fragment
                val navHostFragment = supportFragmentManager.findFragmentById(
                    R.id.nav_host_fragment
                ) as NavHostFragment
                navHostFragment.navController

            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
    }
}