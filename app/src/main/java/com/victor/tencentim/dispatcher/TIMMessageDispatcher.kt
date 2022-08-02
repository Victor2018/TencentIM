package com.victor.tencentim.dispatcher

import android.text.TextUtils
import com.google.gson.Gson
import com.hok.lib.im.util.TIMMessageBuilder
import com.tencent.imsdk.v2.V2TIMMessage
import com.victor.tencentim.module.TencentImModule
import com.victor.tencentim.data.TIMCustomData
import java.nio.charset.Charset


/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: TIMMessageDispatcher
 * Author: Victor
 * Date: 2022/5/19 15:38
 * Description: 腾讯IM消息分发
 * -----------------------------------------------------------------
 */

object TIMMessageDispatcher {

    /**
     * 发送文本消息
     * @param netid
     * @param msg
     */
    fun sendTxtMessage(userId: String?, msg: String?): V2TIMMessage? {
        val message = TIMMessageBuilder.buildTextMessage(msg)
        TencentImModule.instance.sendMessage(message,userId)
        return message
    }

    /**
     * 发送图片消息
     * @param account
     * @param imgPath
     */
    fun sendImageMessage(userId: String?, imgPath: String?): V2TIMMessage? {
        val message = TIMMessageBuilder.buildImageMessage(imgPath)
        TencentImModule.instance.sendMessage(message,userId)
        return message
    }

    /**
     * 发送视频消息
     * @param account
     * @param imgPath
     */
    fun sendVideoMessage(userId: String?, imgPath: String?,videoPath: String?, duration: Long): V2TIMMessage? {
        val message = TIMMessageBuilder.buildVideoMessage(imgPath,videoPath, duration)
        TencentImModule.instance.sendMessage(message,userId)
        return message
    }

    /**
     * 发送语音消息
     * @param account
     * @param imgPath
     */
    fun sendSoundMessage(userId: String?, soundPath: String?,duration: Int): V2TIMMessage? {
        val message = TIMMessageBuilder.buildSoundMessage(soundPath,duration)
        TencentImModule.instance.sendMessage(message,userId)
        return message
    }

    /**
     * 发送表情消息
     * @param account
     * @param imgPath
     */
    fun sendFaceMessage(userId: String?,groupId: Int, faceName: String): V2TIMMessage? {
        val message = TIMMessageBuilder.buildFaceMessage(groupId,faceName)
        TencentImModule.instance.sendMessage(message,userId)
        return message
    }

    /**
     * 发送位置消息
     * @param account
     * @param imgPath
     */
    fun sendLocationMessage(userId: String?,desc: String?, longitude: Double, latitude: Double): V2TIMMessage? {
        val message = TIMMessageBuilder.buildLocationMessage(desc, longitude, latitude)
        TencentImModule.instance.sendMessage(message,userId)
        return message
    }

    /**
     * 发送文件消息
     * @param account
     * @param imgPath
     */
    fun sendFileMessage(userId: String?,filePath: String?): V2TIMMessage? {
        val message = TIMMessageBuilder.buildFileMessage(filePath)
        TencentImModule.instance.sendMessage(message,userId)
        return message
    }

    /**
     * 发送转发消息
     * @param account
     * @param imgPath
     */
    fun sendForwardMessage(userId: String?,message: V2TIMMessage?): V2TIMMessage? {
        val message = TIMMessageBuilder.buildForwardMessage(message)
        TencentImModule.instance.sendMessage(message,userId)
        return message
    }

    /**
     * 发送合并消息
     * @param account
     * @param imgPath
     */
    fun sendMergeMessage(userId: String?,msgList: List<V2TIMMessage>?,
                           title: String?,
                           abstractList: List<String>?,
                           compatibleText: String?): V2TIMMessage? {
        val message = TIMMessageBuilder.buildMergeMessage(msgList, title, abstractList, compatibleText)
        TencentImModule.instance.sendMessage(message,userId)
        return message
    }

    /**
     * 发送提问消息
     */
    fun sendCustomMessage(userId: String?, data: String,
                           description: String?,
                           extension: ByteArray?): V2TIMMessage? {
        val message = TIMMessageBuilder.buildCustomMessage(data, description, extension)
        TencentImModule.instance.sendMessage(message,userId)
        return message
    }

    /**
     * 发送自定义提问消息
     */
    fun sendAskCustomMessage(userId: String?, content: String?): V2TIMMessage? {
        val message = TIMMessageBuilder.buildAskMessage(content, null, null)
        TencentImModule.instance.sendMessage(message,userId)
        return message
    }

    /**
     * 发送自定义回复消息
     */
    fun sendAnswerCustomMessage(userId: String?, content: String?, replyMessage: V2TIMMessage?): V2TIMMessage? {
        val message = TIMMessageBuilder.buildAnswerMessage(content, replyMessage,null, null)
        TencentImModule.instance.sendMessage(message,userId)
        return message
    }

    /**
     * 发送自定义提问消息
     */
    fun sendQuestionnaireCustomMessage(userId: String?, title:String?, content: String?): V2TIMMessage? {
        val message = TIMMessageBuilder.buildQuestionnaireMessage(title,content, null, null)
        TencentImModule.instance.sendMessage(message,userId)
        return message
    }

    /**
     * 发送提问消息
     * @param account
     * @param imgPath
     */
    fun sendAskMessage(userId: String?, msg: String): V2TIMMessage? {

        val message = TIMMessageBuilder.buildTextMessage(msg)

        var answerMessage = TIMCustomData()
        answerMessage.subMsgType = 6
//        answerMessage.title = "提问消息"

        message?.cloudCustomData = Gson().toJson(answerMessage)

        TencentImModule.instance.sendMessage(message,userId)
        return message
    }

    /**
     * 发送回复消息
     */
    fun sendAnswerMessage(userId: String?, msg: String?,replyMessage: V2TIMMessage?): V2TIMMessage? {
        val message = TIMMessageBuilder.buildTextMessage(msg)

        var answerMessage = TIMCustomData()
        answerMessage.subMsgType = 7
//        answerMessage.title = "回复消息"
        answerMessage.msgData = Gson().toJson(replyMessage)

        message?.cloudCustomData = Gson().toJson(answerMessage)

        TencentImModule.instance.sendMessage(message,userId)
        return message
    }
    /**
     * 发送群自定义消息
     * @param account
     * @param imgPath
     */
    fun sendGroupCustomMessage(userId: String?, customMessage: String): V2TIMMessage? {
        val message = TIMMessageBuilder.buildGroupCustomMessage(customMessage)
        TencentImModule.instance.sendMessage(message,userId)
        return message
    }

    /**
     * 是否是我发的消息
     * @param fromAccount
     * @return
     */
    fun isMessageSend(message: V2TIMMessage?,mUserId: String?): Boolean {
        var sendUserId =  message?.sender
//        return message?.isSelf ?: false
        return TextUtils.equals(sendUserId,mUserId)
    }

    // 判断消息方向，是否是接收到的消息
    fun isReceivedMessage(message: V2TIMMessage?,mUserId: String?): Boolean {
        return !isMessageSend(message,mUserId)
    }

    fun getCustomData(message: V2TIMMessage?): TIMCustomData? {
        var customDataStr = message?.customElem?.data?.let { String(it, Charset.forName("UTF-8")) }
        return Gson().fromJson(customDataStr, TIMCustomData::class.java)
    }

}