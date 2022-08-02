package com.hok.lib.im.observer

import android.util.Log
import com.google.gson.Gson
import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener
import com.tencent.imsdk.v2.V2TIMMessage
import com.tencent.imsdk.v2.V2TIMMessageReceipt
import com.victor.library.bus.LiveDataBus
import com.victor.tencentim.action.MessageActions
import java.nio.charset.Charset

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: TIMMessageObserver
 * Author: Victor
 * Date: 2022/5/19 14:34
 * Description: 接收新消息
 * -----------------------------------------------------------------
 */

class TIMMessageObserver: V2TIMAdvancedMsgListener() {
    val TAG = "TIMMessageObserver"

    override fun onRecvNewMessage(msg: V2TIMMessage?) {
        super.onRecvNewMessage(msg)
        Log.e(TAG,"onRecvNewMessage-msg = ${Gson().toJson(msg)}")
        Log.e(TAG,"onRecvNewMessage-msg?.elemType = ${msg?.elemType}")
        LiveDataBus.sendMulti(MessageActions.RECY_NEW_MESSAGE,msg)
        when (msg?.elemType) {
            V2TIMMessage.V2TIM_ELEM_TYPE_TEXT ->{
                Log.e(TAG,"onRecvNewMessage-V2TIM_ELEM_TYPE_TEXT-msg = ${Gson().toJson(msg?.textElem)}")
            }
            V2TIMMessage.V2TIM_ELEM_TYPE_IMAGE ->{
                Log.e(TAG,"onRecvNewMessage-V2TIM_ELEM_TYPE_IMAGE-msg = ${Gson().toJson(msg?.imageElem)}")
            }
            V2TIMMessage.V2TIM_ELEM_TYPE_VIDEO ->{
                Log.e(TAG,"onRecvNewMessage-V2TIM_ELEM_TYPE_VIDEO-msg = ${Gson().toJson(msg?.videoElem)}")
            }
            V2TIMMessage.V2TIM_ELEM_TYPE_LOCATION -> {
                Log.e(TAG,"V2TIM_ELEM_TYPE_LOCATION-msg = ${Gson().toJson(msg?.locationElem)}")
            }
            V2TIMMessage.V2TIM_ELEM_TYPE_FACE -> {
                Log.e(TAG,"V2TIM_ELEM_TYPE_FACE-msg = ${Gson().toJson(msg?.faceElem)}")
            }
            V2TIMMessage.V2TIM_ELEM_TYPE_FILE -> {
                Log.e(TAG,"V2TIM_ELEM_TYPE_FILE-msg = ${Gson().toJson(msg?.fileElem)}")
            }
            V2TIMMessage.V2TIM_ELEM_TYPE_SOUND -> {
                Log.e(TAG,"V2TIM_ELEM_TYPE_SOUND-msg = ${Gson().toJson(msg?.soundElem)}")
            }
            V2TIMMessage.V2TIM_ELEM_TYPE_MERGER -> {
                Log.e(TAG,"V2TIM_ELEM_TYPE_MERGER-msg = ${Gson().toJson(msg?.mergerElem)}")
            }
            V2TIMMessage.V2TIM_ELEM_TYPE_CUSTOM -> {
                Log.e(TAG,"V2TIM_ELEM_TYPE_CUSTOM-msg = ${String (msg?.customElem.data, Charset.forName("UTF-8"))}")
            }
            V2TIMMessage.V2TIM_ELEM_TYPE_GROUP_TIPS -> {
                Log.e(TAG,"V2TIM_ELEM_TYPE_GROUP_TIPS-msg = ${Gson().toJson(msg?.groupTipsElem)}")
            }
            V2TIMMessage.V2TIM_ELEM_TYPE_NONE -> {
                Log.e(TAG,"V2TIM_ELEM_TYPE_NONE-msg = ${Gson().toJson(msg)}")
            }
        }

    }

    override fun onRecvMessageReadReceipts(receiptList: MutableList<V2TIMMessageReceipt>?) {
        super.onRecvMessageReadReceipts(receiptList)
        Log.e(TAG,"onRecvMessageReadReceipts()......")
        LiveDataBus.sendMulti(MessageActions.RECY_MESSAGE_READ_RECEIPTS,receiptList)
    }

    override fun onRecvC2CReadReceipt(receiptList: MutableList<V2TIMMessageReceipt>?) {
        super.onRecvC2CReadReceipt(receiptList)
        Log.e(TAG,"onRecvC2CReadReceipt()......")
        LiveDataBus.sendMulti(MessageActions.RECY_C2C_READ_RECEIPT,receiptList)
    }

    override fun onRecvMessageModified(msg: V2TIMMessage?) {
        super.onRecvMessageModified(msg)
        Log.e(TAG,"onRecvMessageModified()......")
        LiveDataBus.sendMulti(MessageActions.RECY_MESSAGE_MODIFIED,msg)
    }

    override fun onRecvMessageRevoked(msgID: String?) {
        super.onRecvMessageRevoked(msgID)
        Log.e(TAG,"onRecvMessageRevoked()......")
        LiveDataBus.sendMulti(MessageActions.RECY_MESSAGE_REVOKED,msgID)
    }
}