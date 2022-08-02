package com.victor.tencentim

import android.text.TextUtils
import android.util.Base64
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.zip.Deflater
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: TLSSignatureUtil
 * Author: Victor
 * Date: 2022/5/18 11:27
 * Description: 
 * -----------------------------------------------------------------
 */

object TLSSignatureUtil {
    fun genUserSig(userId: String?): String? {
        return userId?.let {
            GenTLSSignature(
                AppConfig.TENCENT_IM_APP_ID, it,
                AppConfig.EXPIRETIME, null,
                AppConfig.SECRETKEY
            )
        }
    }

    fun GenTLSSignature(
        sdkappid: Int,
        userId: String,
        expire: Int,
        userbuf: ByteArray?,
        priKeyContent: String?
    ): String? {
        var userSig = ""
        if (TextUtils.isEmpty(priKeyContent)) {
            return userSig
        }
        val currTime = System.currentTimeMillis() / 1000
        val sigDoc = JSONObject()
        sigDoc.put("TLS.ver", "2.0")
        sigDoc.put("TLS.identifier", userId)
        sigDoc.put("TLS.sdkappid", sdkappid)
        sigDoc.put("TLS.expire", expire)
        sigDoc.put("TLS.time", currTime)

        var base64UserBuf: String? = null
        if (null != userbuf) {
            base64UserBuf = Base64.encodeToString(userbuf, Base64.NO_WRAP)
            try {
                sigDoc.put("TLS.userbuf", base64UserBuf)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        val sig = hmacsha256(
            sdkappid,
            userId,
            currTime,
            expire,
            priKeyContent,
            base64UserBuf
        )
        if (sig?.length == 0) {
            return ""
        }
        sigDoc.put("TLS.sig", sig)
        val compressor = Deflater()
        compressor.setInput(sigDoc.toString().toByteArray(Charset.forName("UTF-8")))
        compressor.finish()
        val compressedBytes = ByteArray(2048)
        val compressedBytesLength = compressor.deflate(compressedBytes)
        compressor.end()

        userSig =
            String(base64EncodeUrl(Arrays.copyOfRange(compressedBytes, 0, compressedBytesLength)))
        return userSig
    }

    private fun hmacsha256(
        sdkappid: Int,
        userId: String,
        currTime: Long,
        expire: Int,
        priKeyContent: String?,
        base64Userbuf: String?
    ): String? {
        var contentToBeSigned = """
            TLS.identifier:$userId
            TLS.sdkappid:$sdkappid
            TLS.time:$currTime
            TLS.expire:$expire
            
            """.trimIndent()
        if (null != base64Userbuf) {
            contentToBeSigned += "TLS.userbuf:$base64Userbuf\n"
        }
        return try {
            val byteKey = priKeyContent?.toByteArray(charset("UTF-8"))
            val hmac = Mac.getInstance("HmacSHA256")
            val keySpec = SecretKeySpec(byteKey, "HmacSHA256")
            hmac.init(keySpec)
            val byteSig = hmac.doFinal(contentToBeSigned.toByteArray(charset("UTF-8")))
            String(Base64.encode(byteSig, Base64.NO_WRAP))
        } catch (e: UnsupportedEncodingException) {
            ""
        } catch (e: NoSuchAlgorithmException) {
            ""
        } catch (e: InvalidKeyException) {
            ""
        }
    }

    private fun base64EncodeUrl(input: ByteArray): ByteArray {
        val base64 = String(Base64.encode(input, Base64.NO_WRAP)).toByteArray()
        for (i in base64.indices)
            when (base64[i]) {
                '+'.code.toByte() -> base64[i] = '*'.code.toByte()
                '/'.code.toByte() -> base64[i] = '-'.code.toByte()
                '='.code.toByte() -> base64[i] = '_'.code.toByte()
                else -> {
                }
            }
        return base64
    }

}