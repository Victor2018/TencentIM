package com.victor.tencentim.observer

import android.util.Log
import com.hok.lib.im.action.TIMJoinGroupActions
import com.tencent.imsdk.v2.V2TIMCallback
import com.victor.library.bus.LiveDataBus

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: TIMLoginObserver
 * Author: Victor
 * Date: 2022/5/19 14:39
 * Description: 加入群监听
 * -----------------------------------------------------------------
 */

class TIMJoinGroupObserver(var tag: String): V2TIMCallback {
    val TAG = "TIMJoinGroupObserver"
    override fun onError(code: Int, desc: String?) {
        Log.e(TAG,"$tag-onError()......code = $code")
        Log.e(TAG,"onError()......desc = $desc")
        LiveDataBus.sendMulti(TIMJoinGroupActions.JOIN_GROUP_FAILED,desc)
    }

    override fun onSuccess() {
        Log.e(TAG,"$tag-onSuccess()......")
        LiveDataBus.sendMulti(TIMJoinGroupActions.JOIN_GROUP_SUCCESS)
    }

}