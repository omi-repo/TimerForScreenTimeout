package kost.romi.timerforscreentimeout

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.room.Room
import kost.romi.timerforscreentimeout.data.source.local.TimerDatabase
import timber.log.Timber
import timber.log.Timber.DebugTree

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
class MainActivity : AppCompatActivity() {

    // Interaction with the DevicePolicyManager
    var mDPM: DevicePolicyManager? = null
    var mDeviceAdmin: ComponentName? = null

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))
        val navController: NavController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration.Builder().build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        // Placeholder for fragment
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        navHostFragment.navController

        // Handling Admin
        // Prepare to work with the Device Policy Manager
        mDPM = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        mDeviceAdmin = ComponentName(this, MyDeviceAdminReceiver::class.java)
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdmin)
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "EXPLANATION")
        startActivityForResult(intent, 0)
        MyDeviceAdminReceiver().onEnabled(this, intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration) ||
                super.onSupportNavigateUp()
    }
}