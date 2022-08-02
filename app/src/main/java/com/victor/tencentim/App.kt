package com.victor.tencentim

import android.app.Application
import com.tencent.imsdk.v2.V2TIMUserFullInfo

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: App
 * Author: Victor
 * Date: 2022/5/18 10:22
 * Description: 
 * -----------------------------------------------------------------
 */

class App : Application() {

    companion object {
        private lateinit var instance: App
        fun get() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        TencentImModule.instance.init(this)
    }
}