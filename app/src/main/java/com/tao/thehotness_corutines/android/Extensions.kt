package com.tao.thehotness_corutines.android

import android.support.v7.app.AppCompatActivity


fun AppCompatActivity.appComponent() = (application as BggApplication).appComponent
