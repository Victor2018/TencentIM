package com.victor.tencentim.data

import java.io.Serializable

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: InviteData
 * Author: Victor
 * Date: 2022/5/25 14:48
 * Description: 
 * -----------------------------------------------------------------
 */

class InviteData: Serializable {
    var cmd: String? = null
    var room_id: Int = 0
    var userIDs: List<String>? = null
}