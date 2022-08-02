package com.victor.tencentim

import com.victor.tencentim.util.BrandUtil

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: PushCertificate
 * Author: Victor
 * Date: 2022/5/19 19:51
 * Description: 
 * -----------------------------------------------------------------
 */

object PushCertificate {
    const val HUAWEI: Long = 23668
    const val XIAO_MI: Long = 23667
    const val OPPO: Long = 23667
    const val VIVO: Long = 23667

    fun getPushCertificate(): Long {
        if (BrandUtil.isBrandHuawei()) {
            return HUAWEI
        }
        if (BrandUtil.isBrandXiaoMi()) {
            return XIAO_MI
        }
        if (BrandUtil.isBrandOppo()) {
            return OPPO
        }
        if (BrandUtil.isBrandVivo()) {
            return VIVO
        }
        return 0
    }
}