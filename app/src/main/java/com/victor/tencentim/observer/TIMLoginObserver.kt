package com.hok.lib.im.observer

import android.util.Log
import com.tencent.imsdk.v2.V2TIMCallback
import com.hok.lib.im.action.TIMLoginActions
import com.victor.library.bus.LiveDataBus
import com.victor.tencentim.module.TencentImModule

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: TIMLoginObserver
 * Author: Victor
 * Date: 2022/5/19 14:39
 * Description: 登录监听
 * -----------------------------------------------------------------
 */

class TIMLoginObserver(var tag: String): V2TIMCallback {
    val TAG = "TIMLoginObserver"
    override fun onError(code: Int, desc: String?) {
        Log.e(TAG,"$tag-onError()......code = $code")
        Log.e(TAG,"$tag-onError()......desc = $desc")
        LiveDataBus.sendMulti(TIMLoginActions.LOGIN_TIM_FAILED,desc)
        if (code == 7508) {
            TencentImModule.instance.loginRetryDelay()
        }
    }

    override fun onSuccess() {
        Log.e(TAG,"$tag-onSuccess()......")
        LiveDataBus.sendMulti(TIMLoginActions.LOGIN_TIM_SUCCESS)
    }

}