package com.victor.tencentim

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: AppConfig
 * Author: Victor
 * Date: 2022/3/1 12:03
 * Description: 
 * -----------------------------------------------------------------
 */

object AppConfig {
    const val TENCENT_IM_APP_ID = 1400679964
    /**
     * 签名过期时间，建议不要设置的过短
     *
     *
     * 时间单位：秒
     * 默认时间：7 x 24 x 60 x 60 = 604800 = 7 天
     */
    /**
     * Signature validity period, which should not be set too short
     *
     *
     * Time unit: second
     * Default value: 604800 (7 days)
     */
    const val EXPIRETIME = 604800

    /**
     * Follow the steps below to obtain the key required for UserSig calculation.
     * <p>
     * Step 1. Log in to the [Tencent Cloud IM console](https://console.intl.cloud.tencent.com/im), and create an application if you don’t have one.
     * Step 2. Click Application Configuration to go to the basic configuration page and locate Account System Integration.
     * Step 3. Click View Key to view the encrypted key used for UserSig calculation. Then copy and paste the key to the variable below.
     * <p>
     * Note: this method is for testing only. Before commercial launch, please migrate the UserSig calculation code and key to your backend server to prevent key disclosure and traffic stealing.
     * Reference: https://intl.cloud.tencent.com/document/product/1047/34385
     */
    const val SECRETKEY = "4544db697d4ca437be0168f9de91524726d24a6f92e6c49556924b440eb700be"
}