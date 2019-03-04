package com.loneoaktech.tests.nightdogs.support

import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.loneoaktech.tests.nightdogs.R
import com.loneoaktech.util.nullIfTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

/**
 * Created by BillH on 3/2/2019
 */
abstract class BaseFragment : Fragment(), CoroutineScope, EasyPermissions.PermissionCallbacks {

    companion object {
        private const val RC_PERMISSION_CHECK = 4242
    }

    private lateinit var rootJob: Job

    private var postPermissionBlock: (()->Unit)? = null
    private var requestedPermissions: Array<String>? = null
    private var permissionRationale: Int? = null

    override val coroutineContext: CoroutineContext
        get() = rootJob

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootJob = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        rootJob.cancel()
    }

    /**
     * Service to derived classes: Permission check and request if necessary, then execute an operation.
     */
    protected fun checkPermissionAndRun(permissions: Array<String>, @StringRes rationale: Int, block: ()->Unit ) {
        requestedPermissions = permissions
        postPermissionBlock = block
        permissionRationale = rationale

        checkPermissionAndRunImpl()
    }

    @AfterPermissionGranted(RC_PERMISSION_CHECK)
    fun checkPermissionAndRunImpl() {
        context?.let { ctx ->
            requestedPermissions?.nullIfTrue { it.isEmpty() }?.let { perms ->

                if (EasyPermissions.hasPermissions(ctx, *perms)){
                    postPermissionBlock?.invoke()
                } else {
                    EasyPermissions.requestPermissions(
                        this,
                        getString(permissionRationale ?: R.string.message_permission_needed),
                        RC_PERMISSION_CHECK,
                        *perms
                    )
                }
            }
        }
    }

    /**
     * Overridden to pass the request response to the Easy Permissions lib.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    /**
     * * Part of EasyPermissions.PermissionCallbacks interface
     *
     * Overridden to handle the case where the user denies the permission and check the never ask again.
     * Since we have to have it we need to send the user to the app settings screen to re-enable.
     */
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Timber.i("Permissions denied=$perms")

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms.toList() ))
            AppSettingsDialog
                .Builder(this)
                .setRationale(permissionRationale ?:  R.string.message_required_permission)
                .build()
                .show()
    }

    /**
     * Part of EasyPermissions.PermissionCallbacks interface
     */
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Timber.i("Permissions granted=$perms")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE)
            checkPermissionAndRunImpl()
    }



}