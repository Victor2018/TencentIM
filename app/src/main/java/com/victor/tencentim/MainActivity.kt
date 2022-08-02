package com.victor.tencentim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.hok.lib.im.action.SignalingActions
import com.tencent.imsdk.v2.V2TIMCallback
import com.victor.library.bus.LiveDataBus
import com.victor.tencentim.action.*
import com.victor.tencentim.data.InviteBean
import com.victor.tencentim.data.InviteData
import com.victor.tencentim.data.SignalingData
import com.victor.tencentim.data.TIMLoginInfo
import com.victor.tencentim.module.TencentImModule
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),View.OnClickListener {
    val TAG = "MainActivity"
    var mSignalingData: SignalingData? = null
    var inviteID: String? = null//发起语音、视频通话后返回暂存用于取消语音、视频通话
    var inviteDataJson: String? = null//发起语音、视频通话data暂存用于取消语音、视频通话

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()
    }

    fun initialize () {
        subscribeSdkEvent()
        subscribePushEvent()
        subscribeLoginEvent()
        subscribeConversationEvent()
        subscribeMessageEvent()
        subscribeSignalingEvent()

        mBtnLogin.setOnClickListener(this)
        mBtnSend.setOnClickListener(this)
        mBtnAccept.setOnClickListener(this)
        mBtnSwitchAudio.setOnClickListener(this)
        mBtnReject.setOnClickListener(this)
        mBtnAudioCall.setOnClickListener(this)
        mBtnVideoCall.setOnClickListener(this)
        mBtnCancelCall.setOnClickListener(this)
    }

    /**
     * SDK event
     */
    fun subscribeSdkEvent () {

        LiveDataBus.withMulti(SDKActions.CONNECTING,javaClass.name)
            .observe(this, Observer {
            mTvStatus.text = "CONNECTING"
        })
        LiveDataBus.withMulti(SDKActions.CONNECTING_SUCCESS,javaClass.name)
            .observe(this, Observer {
            mTvStatus.text = "CONNECTING_SUCCESS"
        })
        LiveDataBus.withMulti(SDKActions.CONNECTING_FAILED,javaClass.name)
            .observe(this, Observer {
            mTvStatus.text = "CONNECTING_FAILED"
        })
        LiveDataBus.withMulti(SDKActions.KICKED_OFFLINE,javaClass.name)
            .observe(this, Observer {
            mTvStatus.text = "KICKED_OFFLINE"
        })
        LiveDataBus.withMulti(SDKActions.USER_SIG_EXPIRED,javaClass.name)
            .observe(this, Observer {
            mTvStatus.text = "USER_SIG_EXPIRED"
        })
        LiveDataBus.withMulti(SDKActions.SELF_INFO_UPDATED,javaClass.name)
            .observe(this, Observer {
            mTvStatus.text = "SELF_INFO_UPDATED"
        })
    }

    /**
     * 推送 event
     */
    fun subscribePushEvent () {
        LiveDataBus.withMulti(PushConfigActions.CONFIG_PUSH_SUCCESS,javaClass.name)
            .observe(this, Observer {
            mTvStatus.text = "CONFIG_PUSH_SUCCESS"
        })

        LiveDataBus.withMulti(PushConfigActions.CONFIG_PUSH_FAILED,javaClass.name)
            .observe(this, Observer {
            mTvStatus.text = "CONFIG_PUSH_FAILED"
        })
    }

    /**
     * 登录 event
     */
    fun subscribeLoginEvent () {
        LiveDataBus.withMulti(LoginActions.LOGIN_TIM_SUCCESS,javaClass.name)
            .observe(this, Observer {
            mTvStatus.text = "LOGIN_TIM_SUCCESS"
        })

        LiveDataBus.withMulti(LoginActions.LOGIN_TIM_FAILED,javaClass.name)
            .observe(this, Observer {
            mTvStatus.text = "LOGIN_TIM_FAILED"
        })
    }

    /**
     * 会话 event
     */
    fun subscribeConversationEvent () {
        LiveDataBus.withMulti(ConversationActions.SYNC_SERVER_START,javaClass.name)
            .observe(this, Observer {
            mTvStatus.text = "SYNC_SERVER_START"
        })
        LiveDataBus.withMulti(ConversationActions.SYNC_SERVER_FINISH,javaClass.name)
            .observe(this, Observer {
            mTvStatus.text = "SYNC_SERVER_FINISH"
        })
        LiveDataBus.withMulti(ConversationActions.SYNC_SERVER_FAILED,javaClass.name)
            .observe(this, Observer {
            mTvStatus.text = "SYNC_SERVER_FAILED"
        })
        LiveDataBus.withMulti(ConversationActions.NEW_CONVERSATION,javaClass.name)
            .observe(this, Observer {
            mTvStatus.text = "NEW_CONVERSATION"
        })
        LiveDataBus.withMulti(ConversationActions.CONVERSATION_CHANGED,javaClass.name)
            .observe(this, Observer {
            mTvStatus.text = "CONVERSATION_CHANGED"
        })
        LiveDataBus.withMulti(ConversationActions.TOTAL_UNREAD_MESSAGE_COUNT_CHANGED,javaClass.name)
            .observe(this, Observer {
            mTvStatus.text = "TOTAL_UNREAD_MESSAGE_COUNT_CHANGED"
        })
    }

    /**
     * 消息 event
     */
    fun subscribeMessageEvent () {
        LiveDataBus.withMulti(MessageActions.RECY_NEW_MESSAGE,javaClass.name)
            .observe(this, Observer {
            mTvStatus.text = "RECY_NEW_MESSAGE"
        })
        LiveDataBus.withMulti(MessageActions.RECY_MESSAGE_READ_RECEIPTS,javaClass.name)
            .observe(this, Observer {
            mTvStatus.text = "RECY_MESSAGE_READ_RECEIPTS"
        })
        LiveDataBus.withMulti(MessageActions.RECY_C2C_READ_RECEIPT,javaClass.name)
            .observe(this, Observer {
            mTvStatus.text = "RECY_C2C_READ_RECEIPT"
        })
        LiveDataBus.withMulti(MessageActions.RECY_MESSAGE_MODIFIED,javaClass.name)
            .observe(this, Observer {
            mTvStatus.text = "RECY_MESSAGE_MODIFIED"
        })
        LiveDataBus.withMulti(MessageActions.RECY_MESSAGE_REVOKED,javaClass.name)
            .observe(this, Observer {
            mTvStatus.text = "RECY_MESSAGE_REVOKED"
        })
    }

    /**
     * 信令 event
     */
    fun subscribeSignalingEvent () {
        LiveDataBus.withMulti(SignalingActions.RECEIVE_NEW_INVITATION,javaClass.name)
            .observe(this, Observer {
                mTvStatus.text = "RECEIVE_NEW_INVITATION"
                mSignalingData = it as SignalingData?

                var inviteBean = Gson().fromJson(mSignalingData?.data,InviteBean::class.java)
                if (inviteBean?.call_type == 1) {
                    mBtnAccept.text = "接听语音"
                    mBtnReject.text = "拒绝语音"
                } else if (inviteBean?.call_type == 2) {
                    mBtnAccept.text = "接听视频"
                    mBtnReject.text = "拒绝视频"
                }

                if (TextUtils.equals("switchToAudio",inviteBean?.switch_to_audio_call)) {
                    mBtnSwitchAudio.visibility = View.VISIBLE
                } else {
                    mBtnSwitchAudio.visibility = View.GONE
                }
        })
        LiveDataBus.withMulti(SignalingActions.INVITATION_CANCELLED,javaClass.name)
            .observe(this, Observer {
                mTvStatus.text = "INVITATION_CANCELLED"
                mSignalingData = it as SignalingData?
        })
        LiveDataBus.withMulti(SignalingActions.INVITATION_TIME_OUT,javaClass.name)
            .observe(this, Observer {
                mTvStatus.text = "INVITATION_TIME_OUT"
                mSignalingData = it as SignalingData?
        })
        LiveDataBus.withMulti(SignalingActions.INVITEE_ACCEPTED,javaClass.name)
            .observe(this, Observer {
                mTvStatus.text = "INVITEE_ACCEPTED"
                mSignalingData = it as SignalingData?
        })
        LiveDataBus.withMulti(SignalingActions.INVITEE_REJECTED,javaClass.name)
            .observe(this, Observer {
                mTvStatus.text = "INVITEE_REJECTED"
                mSignalingData = it as SignalingData?
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.mBtnLogin -> {
                var userName = mEtUserName.text.toString()
                if (TextUtils.isEmpty(userName)) {
                    mEtUserName.error = "请输入登录用户名"
                    return
                }
                var timLoginInfo = TIMLoginInfo()
                timLoginInfo.userId = userName
                timLoginInfo.userSig = TLSSignatureUtil.genUserSig(timLoginInfo?.userId)
                TencentImModule.instance.login(timLoginInfo)
            }
            R.id.mBtnSend -> {
//                TIMMessageDispatcher.sendTxtMessage("lee","hello,my name is cherry" + System.currentTimeMillis())
                val imgPath = "/storage/emulated/0/Pictures/im/1652777047316.jpg"
                val videoPath = "/storage/emulated/0/DCIM/Camera/video_20220518_150924.mp4"
                TIMMessageDispatcher.sendImageMessage("lee",imgPath)
//                TIMMessageDispatcher.sendVideoMessage("lee",imgPath,videoPath,16000)
//                TIMMessageDispatcher.sendLocationMessage("lee","襄阳",112.135345,32.004908)

            }
            R.id.mBtnAudioCall -> {
                var inviteData = InviteData()
                inviteData.cmd = "audioCall"
                inviteData.room_id = 100735374
                inviteData.userIDs = listOf("lee")

                var inviteBean = InviteBean()
                inviteBean.businessID = "av_call"
                inviteBean.platform = "Android"
                inviteBean.call_action = 0
                inviteBean.call_type = 1
                inviteBean.room_id = 100735374
                inviteBean.version = 4
                inviteBean.data = inviteData

                inviteDataJson = Gson().toJson(inviteBean)

                inviteID = TIMSignalingDispatcher.sendInvite("lee",inviteDataJson,true,null,30,null)
            }
            R.id.mBtnVideoCall -> {
                var inviteData = InviteData()
                inviteData.cmd = "videoCall"
                inviteData.room_id = 100735374
                inviteData.userIDs = listOf("lee")

                var inviteBean = InviteBean()
                inviteBean.businessID = "av_call"
                inviteBean.platform = "Android"
                inviteBean.call_action = 0
                inviteBean.call_type = 2
                inviteBean.room_id = 100735374
                inviteBean.version = 4
                inviteBean.data = inviteData

                inviteDataJson = Gson().toJson(inviteBean)

                inviteID = TIMSignalingDispatcher.sendInvite("lee",inviteDataJson,true,null,30,null)
            }
            R.id.mBtnCancelCall -> {
                TIMSignalingDispatcher.cancelInvitation(inviteID,inviteDataJson,object :
                    V2TIMCallback {
                    override fun onSuccess() {
                        Toast.makeText(this@MainActivity,"取消成功",Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(code: Int, desc: String?) {
                        Log.e(TAG,"mBtnCancelCall-code = $code")
                        Log.e(TAG,"mBtnCancelCall-desc = $desc")
                        Toast.makeText(this@MainActivity,"取消失败-code = $code-desc = $desc",Toast.LENGTH_SHORT).show()
                    }

                })
            }
            R.id.mBtnAccept -> {
                TIMSignalingDispatcher.acceptInvitation(mSignalingData?.inviteID,mSignalingData?.data,object :
                    V2TIMCallback {
                    override fun onSuccess() {
                        Toast.makeText(this@MainActivity,"接听成功",Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(code: Int, desc: String?) {
                        Log.e(TAG,"mBtnAccept-code = $code")
                        Log.e(TAG,"mBtnAccept-desc = $desc")
                        Toast.makeText(this@MainActivity,"接听失败-code = $code-desc = $desc",Toast.LENGTH_SHORT).show()
                    }
                })
            }
            R.id.mBtnSwitchAudio -> {
                var inviteData = InviteData()
                inviteData.cmd = "switchToAudio"
                inviteData.room_id = 100735374
                inviteData.userIDs = listOf("lee")

                var inviteBean = InviteBean()
                inviteBean.businessID = "av_call"
                inviteBean.platform = "Android"
                inviteBean.call_action = 0
                inviteBean.call_type = 2
                inviteBean.room_id = 100735374
                inviteBean.version = 4
                inviteBean.data = inviteData
                inviteBean.switch_to_audio_call = "switchToAudio"

                inviteDataJson = Gson().toJson(inviteBean)

                inviteID = mSignalingData?.inviteID

                TIMSignalingDispatcher.acceptSwitchAudioInvitation(inviteID,inviteDataJson,object :
                    V2TIMCallback {
                    override fun onSuccess() {
                        Toast.makeText(this@MainActivity,"切换语音成功",Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(code: Int, desc: String?) {
                        Toast.makeText(this@MainActivity,"切换语音失败-code = $code-desc = $desc",Toast.LENGTH_SHORT).show()
                        Log.e(TAG,"mBtnReject-code = $code")
                        Log.e(TAG,"mBtnReject-desc = $desc")
                    }
                })
            }
            R.id.mBtnReject -> {
                TIMSignalingDispatcher.rejectInvitation(mSignalingData?.inviteID,mSignalingData?.data,object :
                    V2TIMCallback {
                    override fun onSuccess() {
                        Toast.makeText(this@MainActivity,"拒绝成功",Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(code: Int, desc: String?) {
                        Toast.makeText(this@MainActivity,"拒绝失败-code = $code-desc = $desc",Toast.LENGTH_SHORT).show()
                        Log.e(TAG,"mBtnReject-code = $code")
                        Log.e(TAG,"mBtnReject-desc = $desc")
                    }
                })
            }
        }
    }
}