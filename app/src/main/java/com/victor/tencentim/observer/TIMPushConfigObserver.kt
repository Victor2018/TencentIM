package com.hok.lib.im.observer

import android.util.Log
import com.tencent.imsdk.v2.V2TIMCallback
import com.victor.library.bus.LiveDataBus
import com.victor.tencentim.action.PushConfigActions

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: TIMPushConfigObserver
 * Author: Victor
 * Date: 2022/5/19 19:43
 * Description: 
 * -----------------------------------------------------------------
 */

class TIMPushConfigObserver : V2TIMCallback {
    val TAG = "TIMPushConfigObserver"
    override fun onSuccess() {
        Log.e(TAG,"setOfflinePushConfig-onSuccess")
        LiveDataBus.sendMulti(PushConfigActions.CONFIG_PUSH_SUCCESS)
    }

    override fun onError(code: Int, desc: String?) {
        Log.e(TAG,"setOfflinePushConfig-onError-code = $code")
        Log.e(TAG,"setOfflinePushConfig-onError-desc = $desc")
        LiveDataBus.sendMulti(PushConfigActions.CONFIG_PUSH_FAILED,desc)
    }
}