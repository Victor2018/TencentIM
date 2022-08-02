package com.victor.tencentim.module

import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.Log
import com.google.gson.Gson
import com.hok.lib.im.observer.*
import com.tencent.imsdk.v2.*
import com.victor.tencentim.AppConfig
import com.victor.tencentim.interfaces.OnTIMUserInfoListener
import com.victor.tencentim.PushCertificate
import com.victor.tencentim.TLSSignatureUtil
import com.victor.tencentim.data.TIMLoginInfo
import com.victor.tencentim.observer.TIMJoinGroupObserver
import com.victor.tencentim.observer.TIMSdkObserver
import com.victor.tencentim.observer.TIMSignalingObserver
import com.victor.tencentim.util.WebConfig

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: TencentImModule
 * Author: Victor
 * Date: 2022/5/18 14:16
 * Description:
 * -----------------------------------------------------------------
 */

class TencentImModule {
    val TAG = "TencentImModule"

    private object Holder { val instance = TencentImModule() }

    companion object {
        val  instance: TencentImModule by lazy { Holder.instance }

        /**
         * 腾讯IM连接重试
         */
        const val TIM_LOGIN_RETRY = 2022
    }

    /**
     * 腾讯登录信息实体类
     */
    var mTIMLoginInfo: TIMLoginInfo? = null

    /**
     * 重试次数
     */
    private val mRetryCount: Long = 0

    /**
     * 重试时间
     */
    private var mRetryTime: Long = 0

    /**
     * 云信消息Handler
     */
    private var mTIMHandler: TIMHandler? = null

    var mTIMSdkObserver: TIMSdkObserver? = null
    var mTIMSignalingObserver: TIMSignalingObserver? = null
    var mTIMConversationObserver: TIMConversationObserver? = null
    var mTIMMessageObserver: TIMMessageObserver? = null


    fun init(context: Context) {
        initIMSdk(context)

        mTIMSdkObserver = TIMSdkObserver()
        mTIMSignalingObserver = TIMSignalingObserver()
        mTIMConversationObserver = TIMConversationObserver()
        mTIMMessageObserver = TIMMessageObserver()

        registerObserver(true)
    }

    fun initIMSdk (context: Context) {
        val config = V2TIMSDKConfig()
        config.logLevel = V2TIMSDKConfig.V2TIM_LOG_INFO
        V2TIMManager.getInstance().initSDK(context, AppConfig.TENCENT_IM_APP_ID, config)
    }

    fun registerObserver (register: Boolean) {
        //接收IM SDK初始化、当前用户被踢下线、登录票据经过期、当前用户的资料发生变更
        registerIMSdkObserver(register)
        //接收会话会话变更、未读数变更监听
        registerConversationObserver(register)
        //接收新消息、已读回执、消息变更、消息撤回监听
        registerMessageObserver(register)
        //信令监听
        registerSignalingObserver(register)
    }

    fun hasLogin(): Boolean {
        return V2TIMManager.getInstance().loginStatus == V2TIMManager.V2TIM_STATUS_LOGINED
    }

    fun login (info: TIMLoginInfo?) {
        info?.userSig = TLSSignatureUtil.genUserSig(info?.userId)
        mTIMLoginInfo = info
        //验证登录信息
        if (info == null) {
            Log.e(TAG, "LoginInfo is null")
        } else if (hasLogin()) {
            Log.d(TAG, "HasLogin = true " + info.userId)
        } else {
            Log.d(TAG, "Login Start...parm = " + Gson().toJson(info))
            V2TIMManager.getInstance().login(info.userId,info?.userSig, TIMLoginObserver("login"))
        }
    }

    fun logout () {
        V2TIMManager.getInstance().logout(TIMLoginObserver("logout"))
    }

    fun retryLogin() {
        val temp = System.currentTimeMillis()
        if (temp - mRetryTime >= 15 * 1000) {
            mRetryTime = temp
            login(mTIMLoginInfo)
            Log.e(TAG, "TIM Retry Login...")
        }
    }

    fun getTIMHandler(): TIMHandler? {
        if (mTIMHandler == null) {
            mTIMHandler = TIMHandler()
        }
        return mTIMHandler
    }

    /**
     * 获取延迟次数
     *
     * @return
     */
    fun getRetryDelay(): Int {
        return if (mRetryCount < 10) {
            3000
        } else if (mRetryCount >= 10 && mRetryCount < 20) {
            5000
        } else {
            10000
        }
    }

    /**
     * 延迟重新登录
     */
    fun loginRetryDelay() {
        getTIMHandler()?.removeMessages(TIM_LOGIN_RETRY)
        getTIMHandler()?.sendEmptyMessageDelayed(
            TIM_LOGIN_RETRY,
            getRetryDelay().toLong()
        )
    }

    class TIMHandler : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                TIM_LOGIN_RETRY -> instance.retryLogin()
            }
        }
    }

    fun getUserInfo(userId: String,listener: OnTIMUserInfoListener?) {
        val userIdList: MutableList<String> = ArrayList()
        userIdList.add(userId)
        V2TIMManager.getInstance().getUsersInfo(
            userIdList, object : V2TIMValueCallback<List<V2TIMUserFullInfo>> {
                override fun onSuccess(v2TIMUserFullInfos: List<V2TIMUserFullInfo>) {
                    if (v2TIMUserFullInfos.isEmpty()) {
                        Log.e(TAG, "get logined userInfo failed. list is empty")
                        return
                    }
                    listener?.OnTIMUserInfo(0,"",v2TIMUserFullInfos[0])
                }

                override fun onError(code: Int, desc: String) {
                    Log.e(TAG,"get logined userInfo failed. code = $code-desc = $desc")
                    listener?.OnTIMUserInfo(code,desc,null)
                }
            })
    }

    fun getIMPushInfo(message: V2TIMMessage?): V2TIMOfflinePushInfo {
        val v2TIMOfflinePushInfo = V2TIMOfflinePushInfo()
        v2TIMOfflinePushInfo.setExt(Gson().toJson(message).toByteArray())
        // OPPO必须设置ChannelID才可以收到推送消息，这个channelID需要和控制台一致
        v2TIMOfflinePushInfo.setAndroidOPPOChannelID("oppo_push")

        return v2TIMOfflinePushInfo
    }

    /**
     * 设置推送配置
     */
    fun setOfflinePushConfig(pushToken: String?) {
        var businessID = PushCertificate.getPushCertificate()

        var v2TIMOfflinePushConfig = V2TIMOfflinePushConfig(businessID, pushToken, false)
        V2TIMManager.getOfflinePushManager()
            .setOfflinePushConfig(v2TIMOfflinePushConfig, TIMPushConfigObserver())
    }

    /**
     * @param userId TIM 用户id
     * @param count 拉取消息的个数，不宜太多，会影响消息拉取的速度，这里建议一次拉取 20 个
     * @param lastMsg 获取消息的起始消息，如果传 null，起始消息为会话的最新消息
     */
    fun getC2CHistoryMessageList(userId: String,lastTIMMsg: V2TIMMessage?,
                                 callback: V2TIMValueCallback<List<V2TIMMessage>>) {
        V2TIMManager.getMessageManager().getC2CHistoryMessageList(userId,
            WebConfig.PAGE_SIZE, lastTIMMsg,callback)
    }

    fun getHistoryMessageList(chatId: String,isForward: Boolean,isGroup: Boolean,lastTIMMsg: V2TIMMessage?,
                              callback: V2TIMValueCallback<List<V2TIMMessage>>) {

        val optionBackward = V2TIMMessageListGetOption()
        optionBackward.count = WebConfig.PAGE_SIZE
        if (isForward) {
            optionBackward.getType = V2TIMMessageListGetOption.V2TIM_GET_CLOUD_OLDER_MSG
        } else {
            optionBackward.getType = V2TIMMessageListGetOption.V2TIM_GET_CLOUD_NEWER_MSG
        }
        optionBackward.lastMsg = lastTIMMsg

        if (isGroup) {
            optionBackward.groupID = chatId
        } else {
            optionBackward.userID = chatId
        }
        V2TIMManager.getMessageManager().getHistoryMessageList(optionBackward,callback)
    }

    fun getGroupHistoryMessageList(groupID: String,lastTIMMsg: V2TIMMessage?,
                                   callback: V2TIMValueCallback<List<V2TIMMessage>>) {

        V2TIMManager.getMessageManager().getGroupHistoryMessageList(groupID, WebConfig.PAGE_SIZE,lastTIMMsg,callback)
    }

    fun sendMessage(v2TIMMessage: V2TIMMessage?,userId: String?) {
        Log.e(TAG, "sendMessage-hasLogin() = " + hasLogin())
        if (!hasLogin()) {
            retryLogin()
        }
        Log.e(TAG,"sendMessage-message = " + Gson().toJson(v2TIMMessage))
        var pushInfo = getIMPushInfo(v2TIMMessage)

        // 发送给对方
        V2TIMManager.getMessageManager().sendMessage(v2TIMMessage, userId, null,
            V2TIMMessage.V2TIM_PRIORITY_DEFAULT, false, pushInfo,
            TIMMessageSendStatusObserver())

    }

    /**
     * 加入群組
     * @param groupID
     * @param message
     */
    fun joinGroup(groupID: String?,message: String?) {
        V2TIMManager.getInstance().joinGroup(groupID,message,TIMJoinGroupObserver("joinGroup"))
    }

    /**
     * 退出群組
     * @param groupID
     */
    fun quitGroup(groupID: String?) {
        V2TIMManager.getInstance().quitGroup(groupID, TIMJoinGroupObserver("quitGroup"))
    }

    /**
     * 接收IM SDK初始化、当前用户被踢下线、登录票据经过期、当前用户的资料发生变更
     */
    fun registerIMSdkObserver(register: Boolean) {
        if (register) {
            V2TIMManager.getInstance().addIMSDKListener(mTIMSdkObserver)
        } else {
            V2TIMManager.getInstance().removeIMSDKListener(mTIMSdkObserver)
        }
    }

    /**
     * 接收会话会话变更、未读数变更监听
     */
    fun registerConversationObserver(register: Boolean) {
        if (register) {
            V2TIMManager.getConversationManager().addConversationListener(mTIMConversationObserver)
        } else {
            V2TIMManager.getConversationManager().removeConversationListener(mTIMConversationObserver)
        }
    }

    /**
     * 接收新消息、已读回执、消息变更、消息撤回监听
     */
    fun registerMessageObserver (register: Boolean) {
        if (register) {
            V2TIMManager.getMessageManager().addAdvancedMsgListener(TIMMessageObserver())
        } else {
            V2TIMManager.getMessageManager().addAdvancedMsgListener(TIMMessageObserver())
        }
    }

    /**
     * IM-信令监听
     */
    fun registerSignalingObserver(register: Boolean) {
        if (register) {
            V2TIMManager.getSignalingManager().addSignalingListener(mTIMSignalingObserver)
        } else {
            V2TIMManager.getSignalingManager().removeSignalingListener(mTIMSignalingObserver)
        }
    }


}