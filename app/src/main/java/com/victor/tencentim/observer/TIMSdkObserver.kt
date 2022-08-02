package com.victor.tencentim.observer

import android.util.Log
import com.tencent.imsdk.v2.V2TIMSDKListener
import com.tencent.imsdk.v2.V2TIMUserFullInfo
import com.victor.library.bus.LiveDataBus
import com.victor.tencentim.TencentImModule
import com.victor.tencentim.action.SDKActions

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: TIMSdkObserver
 * Author: Victor
 * Date: 2022/5/19 14:32
 * Description: 在线状态
 * -----------------------------------------------------------------
 */

class TIMSdkObserver: V2TIMSDKListener() {
    val TAG = "TIMOnlineObserver"
    override fun onConnecting() {
        Log.e(TAG,"onConnecting()......正在连接到腾讯云服务器")
        LiveDataBus.sendMulti(SDKActions.CONNECTING)
    }

    override fun onConnectSuccess() {
        Log.e(TAG,"onConnectSuccess()......已经成功连接到腾讯云服务器")
        TencentImModule.instance.loginRetryDelay()
        LiveDataBus.sendMulti(SDKActions.CONNECTING_SUCCESS)
    }

    override fun onConnectFailed(code: Int, error: String) {
        Log.e(TAG,"onConnectFailed()......连接腾讯云服务器失败")
        LiveDataBus.sendMulti(SDKActions.CONNECTING_FAILED,error)
    }

    override fun onKickedOffline() {
        super.onKickedOffline()
        Log.e(TAG,"onKickedOffline()......当前用户被踢下线")
        LiveDataBus.sendMulti(SDKActions.KICKED_OFFLINE)
    }

    override fun onUserSigExpired() {
        super.onUserSigExpired()
        Log.e(TAG,"onUserSigExpired()......登录票据已经过期")
        TencentImModule.instance.loginRetryDelay()
        LiveDataBus.sendMulti(SDKActions.USER_SIG_EXPIRED)
    }

    override fun onSelfInfoUpdated(info: V2TIMUserFullInfo?) {
        super.onSelfInfoUpdated(info)
        Log.e(TAG,"onSelfInfoUpdated()......当前用户的资料发生了更新")
        LiveDataBus.sendMulti(SDKActions.SELF_INFO_UPDATED,info)
    }

}