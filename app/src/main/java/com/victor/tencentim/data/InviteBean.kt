package com.victor.tencentim.data

import java.io.Serializable

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: InviteBean
 * Author: Victor
 * Date: 2022/5/25 14:45
 * Description: 
 * -----------------------------------------------------------------
 */

class InviteBean: Serializable {
    var businessID: String? = null
    var call_action: Int = 0
    var call_type: Int = 0//1，语音邀请；2，视频邀请
    var platform: String? = null
    var room_id: Int = 0
    var version: Int = 0
    var data: InviteData? = null
    var switch_to_audio_call: String? = null
}