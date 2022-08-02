package com.victor.tencentim

import com.hok.lib.im.util.TIMMessageBuilder
import com.tencent.imsdk.v2.V2TIMMessage
import com.victor.tencentim.module.TencentImModule


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
     * 发送自定义消息
     * @param account
     * @param imgPath
     */
    fun sendCustomMessage(userId: String?, data: String,
                           description: String?,
                           extension: ByteArray?): V2TIMMessage? {
        val message = TIMMessageBuilder.buildCustomMessage(data, description, extension)
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
}