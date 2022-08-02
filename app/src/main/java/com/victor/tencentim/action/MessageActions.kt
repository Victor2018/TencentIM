package com.victor.tencentim.action

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: MessageActions
 * Author: Victor
 * Date: 2022/5/19 18:39
 * Description:
 * -----------------------------------------------------------------
 */

object MessageActions {
    const val RECY_NEW_MESSAGE = "RECY_NEW_MESSAGE"
    const val RECY_MESSAGE_READ_RECEIPTS = "RECY_MESSAGE_READ_RECEIPTS"
    const val RECY_C2C_READ_RECEIPT = "RECY_C2C_READ_RECEIPT"
    const val RECY_MESSAGE_MODIFIED = "RECY_MESSAGE_MODIFIED"
    const val RECY_MESSAGE_REVOKED = "RECY_MESSAGE_REVOKED"

    /**
     * 刷新消息状态
     */
    const val REFRESH_MESSAGE_STATUS = "REFRESH_MESSAGE_STATUS"

    /**
     * 图片、视频、文件等消息发送进度回调
     */
    const val REFRESH_MESSAGE_PROCESS = "REFRESH_MESSAGE_PROCESS"
}