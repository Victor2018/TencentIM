package com.victor.tencentim.interfaces

import com.tencent.imsdk.v2.V2TIMUserFullInfo

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: OnTIMUserInfoListener
 * Author: Victor
 * Date: 2022/5/19 15:18
 * Description: 
 * -----------------------------------------------------------------
 */

interface OnTIMUserInfoListener {
    fun OnTIMUserInfo(code: Int, desc: String,data: V2TIMUserFullInfo?)
}