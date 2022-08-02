package com.victor.tencentim.data

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: TIMCustomData
 * Author: Victor
 * Date: 2022/5/30 16:43
 * Description: 腾讯im自定义消息
 * -----------------------------------------------------------------
 */

open class TIMCustomData {
    /**
     * 消息id
     */
    var msgId: String? = null
    /**
     * 消息内容json数据
     */
    var msgData: String? = null

    /**
     * 子消息类型：1，文本；2,文件消息
     */
    var subMsgType: Int = 0

    /**
     * 文本消息类型：1，普通；2，提问；3，回答；4，欢迎新人通知
     */
    var textMsgType: Int = 0

    /**
     * 用户标签名称
     */
    var labelName: String? = null

    /**
     * 审核标志：1不通过；2通过
     */
    var passFlag: Int = 0

}