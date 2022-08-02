package com.victor.tencentim

import com.tencent.imsdk.v2.V2TIMCallback
import com.tencent.imsdk.v2.V2TIMManager
import com.tencent.imsdk.v2.V2TIMOfflinePushInfo

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: TIMSignalingDispatcher
 * Author: Victor
 * Date: 2022/5/25 15:40
 * Description: 腾讯IM-信令分发
 * -----------------------------------------------------------------
 */

object TIMSignalingDispatcher {

    /**
     * 邀请某个人
     * @return inviteID 邀请 ID，如果邀请失败，返回 null
     * @param invitee 被邀请人用户 ID
     * @param onlineUserOnly 是否只有在线用户才能收到邀请，如果设置为 true，只有在线用户才能收到，
     * 并且 invite 操作也不会产生历史消息（针对该次 invite 的后续 cancel、accept、reject、timeout 操作也同样不会产生历史消息）
     * @param offlinePushInfo 离线推送信息，其中 desc 为必填字段，推送的时候会默认展示 desc 信息。
     * @param timeout 超时时间，单位秒，如果设置为 0，SDK 不会做超时检测，也不会触发 onInvitationTimeout 回调
     */
    fun sendInvite (invitee: String?, data: String?, onlineUserOnly: Boolean,
                    offlinePushInfo: V2TIMOfflinePushInfo?, timeout: Int, callback: V2TIMCallback?): String? {
        return V2TIMManager.getSignalingManager()
            .invite(invitee,data,onlineUserOnly,offlinePushInfo,timeout,callback)
    }

    /**
     * 邀请群内的某些人
     * @return inviteID 邀请 ID，如果邀请失败，返回 null
     * @param groupID 发起邀请所在群组
     * @param inviteeList 被邀请人列表，inviteeList 必须已经在 groupID 群里，否则邀请无效
     * @param onlineUserOnly 是否只有在线用户才能收到邀请，如果设置为 true，只有在线用户才能收到，
     * 并且 invite 操作也不会产生历史消息（针对该次 invite 的后续 cancel、accept、reject、timeout 操作也同样不会产生历史消息）。
     * @param timeout 超时时间，单位秒，如果设置为 0，SDK 不会做超时检测，也不会触发 onInvitationTimeout 回调
     * 注意：群邀请暂不支持离线推送，如果您需要离线推送，可以针对被邀请的用户单独发离线推送自定义消息，
     */
    fun sendInviteInGroup (groupID: String?,inviteeList: List<String>?,data:String?,
                           onlineUserOnly: Boolean,timeout: Int,callback: V2TIMCallback?): String? {
        return V2TIMManager.getSignalingManager()
            .inviteInGroup(groupID,inviteeList,data,onlineUserOnly,timeout,callback)
    }

    /**
     * 邀请方取消邀请
     * @param inviteID 邀请 ID
     * 注意：如果所有被邀请人都已经处理了当前邀请（包含超时），不能再取消当前邀请。
     */
    fun cancelInvitation (inviteID: String?, data: String?, callback: V2TIMCallback? ) {
        V2TIMManager.getSignalingManager().cancel(inviteID,data,callback)
    }

    /**
     * 接收方接收邀请
     * @param inviteID 邀请 ID
     * 注意：不能接受不是针对自己的邀请，请在收到 onReceiveNewInvitation 回调的时候先判断 inviteeList 有没有自己，
     * 如果没有自己，不能 accept 邀请
     */
    fun acceptInvitation (inviteID: String?, data: String?, callback: V2TIMCallback?) {
        V2TIMManager.getSignalingManager().accept(inviteID,data,callback)
    }

    /**
     * 接收方接收切换语音邀请
     * @param inviteID 邀请 ID
     * 注意：不能接受不是针对自己的邀请，请在收到 onReceiveNewInvitation 回调的时候先判断 inviteeList 有没有自己，
     * 如果没有自己，不能 accept 邀请
     */
    fun acceptSwitchAudioInvitation (inviteID: String?, data: String?, callback: V2TIMCallback?) {
        V2TIMManager.getSignalingManager().accept(inviteID,data,callback)
    }

    /**
     * 接收方拒绝邀请
     * @param inviteID 邀请 ID
     * 注意：不能拒绝不是针对自己的邀请，请在收到 onReceiveNewInvitation 回调的时候先判断 inviteeList 有没有自己，
     * 如果没有自己，不能 reject 邀请
     */
    fun rejectInvitation (inviteID: String?, data: String?, callback: V2TIMCallback? ) {
        V2TIMManager.getSignalingManager().reject(inviteID,data,callback)
    }
}