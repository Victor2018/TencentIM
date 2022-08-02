package com.hok.lib.im.observer

import android.annotation.SuppressLint
import android.util.Log
import com.tencent.imsdk.v2.V2TIMMessage
import com.hok.lib.im.action.TIMLoginActions
import com.tencent.imsdk.v2.V2TIMSendCallback
import com.victor.library.bus.LiveDataBus
import com.victor.tencentim.action.MessageActions

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: TIMMessageSendStatusObserver
 * Author: Victor
 * Date: 2022/5/19 14:34
 * Description: 消息发送状态监听
 * -----------------------------------------------------------------
 */

class TIMMessageSendStatusObserver: V2TIMSendCallback<V2TIMMessage> {
    val TAG = "MsgSendStatusObserver"
    override fun onSuccess(message: V2TIMMessage?) {
        Log.e(TAG,"sendMessage-onSuccess")
        LiveDataBus.sendMulti(MessageActions.REFRESH_MESSAGE_STATUS,message)
    }

    override fun onError(code: Int, desc: String?) {
        // 图片消息发送失败
        Log.e(TAG,"sendMessage-onError-code = $code")
        Log.e(TAG,"sendMessage-onError-desc = $desc")
        if (code == 6014) {//未登录
            LiveDataBus.sendMulti(TIMLoginActions.LOGIN_TIM)
        }
    }

    override fun onProgress(progress: Int) {
        Log.e(TAG,"sendMessage-onProgress-progress = $progress")
        LiveDataBus.sendMulti(MessageActions.REFRESH_MESSAGE_PROCESS,progress)
    }

}