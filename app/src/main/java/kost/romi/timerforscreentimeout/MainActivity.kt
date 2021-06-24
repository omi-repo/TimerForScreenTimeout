package kost.romi.timerforscreentimeout

import android.Manifest
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint

/**
 * This app is for locking the phone by counting down to zero.
 * This app is also partially a demo app for using :
 * - Data Binding.
 * - Android Navigation Component.
 * - Room Database.
 * - Observable state LiveData to update RecyclerView.
 * - ViewModel Factory.
 * - Admin request Intent.
 */

/* Once Hilt is set up in your Application class and an application-level component is available,
 Hilt can provide dependencies to other Android classes that have the @AndroidEntryPoint annotation. */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Interaction with the DevicePolicyManager
    var mDPM: DevicePolicyManager? = null
    var mDeviceAdmin: ComponentName? = null

    private lateinit var appBarConfiguration: AppBarConfiguration

    //    private val DELAY = 3000
    private val DELAY = 10
    var defTimeOut = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))
        val navController: NavController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration.Builder().build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(false)

        // Placeholder for fragment
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        navHostFragment.navController

//        // Handling Admin
//        // Prepare to work with the Device Policy Manager
//        mDPM = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
//        mDeviceAdmin = ComponentName(this, MyDeviceAdminReceiver::class.java)
//        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
//        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdmin)
//        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "EXPLANATION")
//        startActivityForResult(intent, 0)
//        MyDeviceAdminReceiver().onEnabled(this, intent)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration) ||
                super.onSupportNavigateUp()
    }
}