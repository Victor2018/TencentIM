package com.victor.tencentim.data

import java.io.Serializable

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: SignalingData
 * Author: Victor
 * Date: 2022/5/25 14:23
 * Description: 
 * -----------------------------------------------------------------
 */

data class SignalingData(
    var action: String? = null,
    var inviteID: String? = null,//inviteID 邀请 ID，如果邀请失败，返回 null
    var inviter: String? = null,//邀请者昵称
    var groupID: String? = null,//	发起邀请所在群组
    var inviteeList: List<String>? = null,//被邀请者
    var data: String? = null
) : Serializable