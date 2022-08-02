package com.victor.tencentim.observer

import android.util.Log
import com.google.gson.Gson
import com.tencent.imsdk.v2.V2TIMSignalingListener
import com.hok.lib.im.action.SignalingActions
import com.victor.library.bus.LiveDataBus
import com.victor.tencentim.data.SignalingData

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: TIMSignalingObserver
 * Author: Victor
 * Date: 2022/5/25 12:23
 * Description: 信令监听
 * -----------------------------------------------------------------
 */

class TIMSignalingObserver : V2TIMSignalingListener() {
    val TAG = "TIMSignalingObserver"
    override fun onReceiveNewInvitation(
        inviteID: String?,
        inviter: String?,
        groupID: String?,
        inviteeList: MutableList<String>?,
        data: String?
    ) {
        super.onReceiveNewInvitation(inviteID, inviter, groupID, inviteeList, data)
        Log.e(TAG,"onReceiveNewInvitation()......收到邀请")
        Log.e(TAG,"onReceiveNewInvitation()......inviteID = $inviteID")
        Log.e(TAG,"onReceiveNewInvitation()......inviter = $inviter")
        Log.e(TAG,"onReceiveNewInvitation()......groupID = $groupID")
        Log.e(TAG,"onReceiveNewInvitation()......inviteeList = ${Gson().toJson(inviteeList)}")
        Log.e(TAG,"onReceiveNewInvitation()......data = $data")
        var signalingData = SignalingData(SignalingActions.RECEIVE_NEW_INVITATION,inviteID, inviter, groupID, inviteeList, data)
        LiveDataBus.sendMulti(SignalingActions.RECEIVE_NEW_INVITATION,signalingData)
    }

    override fun onInvitationCancelled(inviteID: String?, inviter: String?, data: String?) {
        super.onInvitationCancelled(inviteID, inviter, data)
        Log.e(TAG,"onInvitationCancelled()......邀请被取消")
        Log.e(TAG,"onInvitationCancelled()......inviteID = $inviteID")
        Log.e(TAG,"onInvitationCancelled()......inviter = $inviter")
        Log.e(TAG,"onInvitationCancelled()......data = $data")
        var signalingData = SignalingData(SignalingActions.INVITATION_CANCELLED,
            inviteID, inviter, null, null, data)
        LiveDataBus.sendMulti(SignalingActions.INVITATION_CANCELLED,signalingData)
    }

    override fun onInvitationTimeout(inviteID: String?, inviteeList: MutableList<String>?) {
        super.onInvitationTimeout(inviteID, inviteeList)
        Log.e(TAG,"onInvitationTimeout()......邀请超时")
        Log.e(TAG,"onInvitationTimeout()......inviteID = $inviteID")
        Log.e(TAG,"onInvitationTimeout()......inviteeList = ${Gson().toJson(inviteeList)}")
        var signalingData = SignalingData(SignalingActions.INVITATION_TIME_OUT,
            inviteID, null, null, inviteeList, null)
        LiveDataBus.sendMulti(SignalingActions.INVITATION_TIME_OUT,signalingData)
    }

    override fun onInviteeAccepted(inviteID: String?, invitee: String?, data: String?) {
        super.onInviteeAccepted(inviteID, invitee, data)
        Log.e(TAG,"onInviteeAccepted()......被邀请者接受邀请")
        Log.e(TAG,"onInviteeAccepted()......inviteID = $inviteID")
        Log.e(TAG,"onInviteeAccepted()......invitee = $invitee")
        var signalingData = SignalingData(SignalingActions.INVITEE_ACCEPTED,
            inviteID, null, null, listOfNotNull(invitee), data)
        LiveDataBus.sendMulti(SignalingActions.INVITEE_ACCEPTED,signalingData)
    }

    override fun onInviteeRejected(inviteID: String?, invitee: String?, data: String?) {
        super.onInviteeRejected(inviteID, invitee, data)
        Log.e(TAG,"onInviteeRejected()......被邀请者拒绝邀请")
        Log.e(TAG,"onInviteeRejected()......inviteID = $inviteID")
        Log.e(TAG,"onInviteeRejected()......invitee = $invitee")
        Log.e(TAG,"onInviteeRejected()......data = $data")
        var signalingData = SignalingData(SignalingActions.INVITEE_REJECTED,
            inviteID, null, null, listOfNotNull(invitee), data)
        LiveDataBus.sendMulti(SignalingActions.INVITEE_REJECTED,signalingData)
    }
}