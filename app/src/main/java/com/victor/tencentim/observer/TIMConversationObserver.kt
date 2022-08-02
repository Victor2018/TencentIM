package com.hok.lib.im.observer

import android.util.Log
import com.google.gson.Gson
import com.tencent.imsdk.v2.V2TIMConversation
import com.tencent.imsdk.v2.V2TIMConversationListener
import com.victor.library.bus.LiveDataBus
import com.victor.tencentim.action.ConversationActions

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: TIMMessageReceiptObserver
 * Author: Victor
 * Date: 2022/5/19 14:36
 * Description: 会话监听
 * -----------------------------------------------------------------
 */

class TIMConversationObserver: V2TIMConversationListener() {
    val TAG = "TIMConversationObserver"

    override fun onSyncServerStart() {
        super.onSyncServerStart()
        Log.e(TAG,"onSyncServerStart()......")
        LiveDataBus.sendMulti(ConversationActions.SYNC_SERVER_START)
    }

    override fun onSyncServerFinish() {
        super.onSyncServerFinish()
        Log.e(TAG,"onSyncServerFinish()......")
        LiveDataBus.sendMulti(ConversationActions.SYNC_SERVER_FINISH)
    }

    override fun onSyncServerFailed() {
        super.onSyncServerFailed()
        Log.e(TAG,"onSyncServerFailed()......")
        LiveDataBus.sendMulti(ConversationActions.SYNC_SERVER_FAILED)
    }

    override fun onNewConversation(conversationList: MutableList<V2TIMConversation>?) {
        super.onNewConversation(conversationList)
        Log.e(TAG,"onNewConversation()......conversationList = ${Gson().toJson(conversationList)}")
        LiveDataBus.sendMulti(ConversationActions.NEW_CONVERSATION,conversationList)
    }

    override fun onConversationChanged(conversationList: MutableList<V2TIMConversation>?) {
        super.onConversationChanged(conversationList)
        Log.e(TAG,"onConversationChanged()......conversationList = ${Gson().toJson(conversationList)}")
        LiveDataBus.sendMulti(ConversationActions.CONVERSATION_CHANGED,conversationList)
    }

    override fun onTotalUnreadMessageCountChanged(totalUnreadCount: Long) {
        super.onTotalUnreadMessageCountChanged(totalUnreadCount)
        Log.e(TAG,"onTotalUnreadMessageCountChanged()......totalUnreadCount = $totalUnreadCount")
        LiveDataBus.sendMulti(ConversationActions.TOTAL_UNREAD_MESSAGE_COUNT_CHANGED,totalUnreadCount)
    }
}