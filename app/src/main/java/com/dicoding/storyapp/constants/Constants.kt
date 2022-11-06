package com.dicoding.storyapp.constants

import android.Manifest

object Constants {
    const val SPLASH_SCREEN_TIMER: Long = 3000
    const val REQUEST_CODE_PERMISSIONS = 10
    val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
    )
    const val UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    const val CREATED_DATE_FORMAT = "dd-MMM-yyyy"
    const val UTC_TIME_ZONE = "UTC"
    const val DETAIL_STORY = "DETAIL_STORY"
    const val MIN_LENGTH_PASSWORD = 6
    const val SUFFIX_IMAGE_FILE = ".jpg"
    const val SIZE_BYTE_ARRAY = 1024
    const val STREAM_LENGTH = 1000000
    const val DICODING_LATITUDE = -6.8957643
    const val DICODING_LONGITUDE = 107.6338462
}