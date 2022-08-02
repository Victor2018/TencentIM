package com.hok.lib.im.util

import com.google.gson.Gson
import com.tencent.imsdk.v2.V2TIMManager
import com.tencent.imsdk.v2.V2TIMMessage
import com.victor.tencentim.data.*
import java.io.File
import java.nio.charset.Charset

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: TIMMessageBuilder
 * Author: Victor
 * Date: 2022/5/19 15:41
 * Description: 消息构建器
 * -----------------------------------------------------------------
 */

object TIMMessageBuilder {

    //当前版本不支持
    const val isNeedReadReceipt = false

    /**
     * 创建文本消息
     */
    fun buildTextMessage(message: String?): V2TIMMessage? {
        val v2TIMMessage = V2TIMManager.getMessageManager().createTextMessage(message)
        v2TIMMessage.isNeedReadReceipt = isNeedReadReceipt
        return v2TIMMessage
    }

    /**
     * 创建文本消息，并且可以附带 @ 提醒功能
     */
    fun buildTextAtMessage(atUserList: List<String>?, message: String?): V2TIMMessage? {
        val v2TIMMessage = V2TIMManager.getMessageManager().createTextAtMessage(message, atUserList)
        v2TIMMessage.isNeedReadReceipt = isNeedReadReceipt
        return v2TIMMessage
    }

    /**
     * 创建一条图片消息
     * @param imagePath 图片路径
     */
    fun buildImageMessage(imagePath: String?): V2TIMMessage? {
        val v2TIMMessage = V2TIMManager.getMessageManager().createImageMessage(imagePath)
        v2TIMMessage.isNeedReadReceipt = isNeedReadReceipt
        return v2TIMMessage
    }

    /**
     * 创建一条语音消息
     * @param imagePath 图片路径
     */
    fun buildSoundMessage(soundPath: String?,duration: Int): V2TIMMessage? {
        val v2TIMMessage = V2TIMManager.getMessageManager().createSoundMessage(soundPath,duration)
        v2TIMMessage.isNeedReadReceipt = isNeedReadReceipt
        return v2TIMMessage
    }

    /**
     * 创建一条视频消息
     * @param imgPath   视频缩略图路径
     * @param videoPath 视频路径
     * @param duration  视频的时长
     */
    fun buildVideoMessage(
        imgPath: String?,
        videoPath: String?,
        duration: Long
    ): V2TIMMessage? {
        val v2TIMMessage = V2TIMManager.getMessageManager()
            .createVideoMessage(videoPath, "mp4", Math.round(duration * 1.0f / 1000), imgPath)
        v2TIMMessage.isNeedReadReceipt = isNeedReadReceipt
        return v2TIMMessage
    }

    /**
     * 创建一条文件消息
     * @param filePath 文件路径
     */
    fun buildFileMessage(filePath: String?): V2TIMMessage? {
        val file = File(filePath)
        val v2TIMMessage = V2TIMManager.getMessageManager().createFileMessage(filePath, file.name)
        v2TIMMessage.isNeedReadReceipt = isNeedReadReceipt
        return v2TIMMessage
    }

    /**
     * 创建一条位置消息
     *
     * @param desc 地址描述
     * @param latitude 维度
     * @param longitude 经度
     */
    fun buildLocationMessage (desc: String?, longitude: Double, latitude: Double): V2TIMMessage? {
        val v2TIMMessage = V2TIMManager.getMessageManager().createLocationMessage(desc,longitude,latitude)
        v2TIMMessage.isNeedReadReceipt = isNeedReadReceipt
        return v2TIMMessage
    }

    /**
     * 创建一条自定义表情的消息
     *
     * @param groupId  自定义表情所在的表情组id
     * @param faceName 表情的名称
     */
    fun buildFaceMessage(groupId: Int, faceName: String): V2TIMMessage? {
        val v2TIMMessage = V2TIMManager.getMessageManager()
            .createFaceMessage(groupId, faceName.toByteArray())
        v2TIMMessage.isNeedReadReceipt = isNeedReadReceipt
        return v2TIMMessage
    }

    /**
     * 创建一条转发消息
     * @param message 转发消息
     */
    fun buildForwardMessage(message: V2TIMMessage?): V2TIMMessage? {
        val v2TIMMessage = V2TIMManager.getMessageManager().createForwardMessage(message)
        v2TIMMessage.isNeedReadReceipt = isNeedReadReceipt
        return v2TIMMessage
    }

    /**
     * 创建一条 合并 转发消息
     * @param message 转发消息
     */
    fun buildMergeMessage(
        msgList: List<V2TIMMessage>?,
        title: String?,
        abstractList: List<String>?,
        compatibleText: String?): V2TIMMessage? {

        if (msgList == null || msgList.isEmpty()) {
            return null
        }
        val mergeMsg = V2TIMManager.getMessageManager().createMergerMessage(msgList, title, abstractList, compatibleText)
        mergeMsg.isNeedReadReceipt = isNeedReadReceipt
        return mergeMsg
    }

    /**
     * 创建一条自定义消息
     * @param data        自定义消息内容，可以是任何内容
     * @param description 自定义消息描述内容，可以被搜索到
     * @param extension   扩展内容
     */
    fun buildCustomMessage(
        data: String,
        description: String?,
        extension: ByteArray?
    ): V2TIMMessage? {
        val v2TIMMessage = V2TIMManager.getMessageManager()
            .createCustomMessage(data.toByteArray(), description, extension)
        v2TIMMessage.isNeedReadReceipt = isNeedReadReceipt
        return v2TIMMessage
    }

    /**
     * 创建一条群消息自定义内容
     * @param customMessage 消息内容
     */
    fun buildGroupCustomMessage(customMessage: String): V2TIMMessage? {
        val v2TIMMessage = V2TIMManager.getMessageManager().createCustomMessage(customMessage.toByteArray())
        v2TIMMessage.isNeedReadReceipt = isNeedReadReceipt
        return v2TIMMessage
    }

    /**
     * 创建一条提问自定义消息
     * @param data        自定义消息内容，可以是任何内容
     * @param description 自定义消息描述内容，可以被搜索到
     * @param extension   扩展内容
     */
    fun buildAskMessage(
        content: String?,
        description: String?,
        extension: ByteArray?
    ): V2TIMMessage? {

        var customData = TIMCustomData ()
        customData.subMsgType = SubMessageType.ASK

        var askData = AskMessage()
//        askData.askContent = content

        customData.msgData = Gson().toJson(askData)

        val data = Gson().toJson(customData)

        val v2TIMMessage = V2TIMManager.getMessageManager()
            .createCustomMessage(data.toByteArray(), description, extension)
        v2TIMMessage.isNeedReadReceipt = isNeedReadReceipt
        return v2TIMMessage
    }

    /**
     * 创建一条回复自定义消息
     * @param data        自定义消息内容，可以是任何内容
     * @param description 自定义消息描述内容，可以被搜索到
     * @param extension   扩展内容
     */
    fun buildAnswerMessage(
        content: String?,
        replyMessage: V2TIMMessage?,
        description: String?,
        extension: ByteArray?
    ): V2TIMMessage? {

        var customData = TIMCustomData ()
        customData.subMsgType = SubMessageType.ANSWER

        var answerData = AnswerMessage()
//        answerData.whoAsk = replyMessage?.sender
//        answerData.askContent = getAskContent(replyMessage)
//        answerData.answerContent = content

        customData.msgData = Gson().toJson(answerData)

        val data = Gson().toJson(customData)

        val v2TIMMessage = V2TIMManager.getMessageManager()
            .createCustomMessage(data.toByteArray(), description, extension)
        v2TIMMessage.isNeedReadReceipt = isNeedReadReceipt
        return v2TIMMessage
    }

    fun buildQuestionnaireMessage(
        title: String?,
        content: String?,
        description: String?,
        extension: ByteArray?
    ): V2TIMMessage? {

        var customData = TIMCustomData ()
        customData.subMsgType = SubMessageType.QUESTIONNAIRE

        var questionnaireMessage = QuestionnaireMessage()
        questionnaireMessage.questionnaireTitle = title
        questionnaireMessage.questionnaireDesc = content

        customData.msgData = Gson().toJson(questionnaireMessage)

        val data = Gson().toJson(customData)

        val v2TIMMessage = V2TIMManager.getMessageManager()
            .createCustomMessage(data.toByteArray(), description, extension)
        v2TIMMessage.isNeedReadReceipt = isNeedReadReceipt
        return v2TIMMessage
    }

    fun getAskContent (replyMessage: V2TIMMessage?): String? {
        var content: String? = null
        when (replyMessage?.elemType) {
            V2TIMMessage.V2TIM_ELEM_TYPE_TEXT -> {
                content = replyMessage?.textElem?.text
            }
            V2TIMMessage.V2TIM_ELEM_TYPE_CUSTOM -> {
                var customDataStr = replyMessage?.customElem?.data?.let { String(it, Charset.forName("UTF-8")) }
                var customData = Gson().fromJson(customDataStr, TIMCustomData::class.java)
                if (customData?.subMsgType == SubMessageType.ASK) {
                    var askData = Gson().fromJson(customData.msgData, AskMessage::class.java)
//                    content = askData?.askContent
                }
            }
        }
        return content
    }

}