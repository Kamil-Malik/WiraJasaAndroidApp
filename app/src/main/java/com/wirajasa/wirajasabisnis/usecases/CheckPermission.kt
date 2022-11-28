package com.wirajasa.wirajasabisnis.usecases

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import pub.devrel.easypermissions.EasyPermissions

class CheckPermission(private val context: Context) {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun accessMedia() : Boolean {
        return EasyPermissions
            .hasPermissions(context, Manifest.permission.READ_MEDIA_IMAGES)
    }

    fun accessExternal(): Boolean {
        return EasyPermissions
            .hasPermissions(context, Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}