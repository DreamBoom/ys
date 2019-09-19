package card.com.allcard.receiver

import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import card.com.allcard.R
import card.com.allcard.activity.*
import card.com.allcard.bean.JpushBean
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.tools.Tool
import cn.jpush.android.api.JPushInterface
import cn.jpush.android.service.PushService
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.pawegio.kandroid.i
import com.pawegio.kandroid.startActivity

class JPushReceiver : BroadcastReceiver() {
    private var context: Context? = null
    override fun onReceive(context: Context, intent: Intent) {
        this.context = context
        val pushIntent = Intent(context, PushService::class.java)//启动极光推送的服务
        context.startService(pushIntent)
        val bundle = intent.extras
        //得到注册id
        when {
            JPushInterface.ACTION_REGISTRATION_ID == intent.action -> {
                //   val string = bundle!!.getString(JPushInterface.EXTRA_REGISTRATION_ID)
                // i { "得到注册id============>$string" }
            }
            JPushInterface.ACTION_MESSAGE_RECEIVED == intent.action -> {
                val message = bundle!!.getString(JPushInterface.EXTRA_MESSAGE)
                //接收后台推送过来的消息
                val message1 = bundle.getString(JPushInterface.EXTRA_MESSAGE) //字符串
                val extras = bundle.getString(JPushInterface.EXTRA_EXTRA) //json格式
                Log.i("在广播中收到了自定义消息", message1!! + "/" + extras)
                val bean = JSONObject.parseObject(extras, object : TypeReference<JpushBean>() {})
                when (bean.msgType) {
                    "login" -> {
                        pop(context, message!!, 0)
                    }
                    "updatePwd" -> {
                        val mk = BaseActivity.mk
                        val userId = mk.decodeString(Tool.USER_ID, "")
                        mk.clearAll()
                        SplashActivity.mkBD.encode(userId + "finger", "")
                        SplashActivity.mkBD.encode(userId + "sign", "")
                        jPush(context, "")
                        JPushInterface.clearAllNotifications(context)
                        pop(context, message!!, 1)
                    }
                    "modPhone" -> {
                        val mk = BaseActivity.mk
                        val userId = mk.decodeString(Tool.USER_ID, "")
                        mk.clearAll()
                        SplashActivity.mkBD.encode(userId + "finger", "")
                        SplashActivity.mkBD.encode(userId + "sign", "")
                        jPush(context, "")
                        JPushInterface.clearAllNotifications(context)
                        pop(context, message!!, 2)
                    }
                }
            }
            JPushInterface.ACTION_NOTIFICATION_RECEIVED == intent.action -> {//接收到通知
                i { "接收到通知===>" }
            }
            JPushInterface.ACTION_NOTIFICATION_OPENED == intent.action -> {
                //点开通知
                val intent = Intent()
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.setClass(context, MainActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    private fun pop(context: Context, mes: String, type: Int) {
        val am = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val cn = am.getRunningTasks(1)[0].topActivity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            for (act in MyApplication.instance.activitys!!) {
                if (".${act.localClassName}" == cn.shortClassName
                        || cn.shortClassName == ".unihome.UniHomeLauncher"
                        ||cn.shortClassName == ".launcher.Launcher") {
                    if (!act.isDestroyed) {
                        when (type) {
                            0 -> alert(act, "您的账号于 $mes 在另一台设备登录。如非本人操作，则密码可能已泄露，建议前往修改密码。")
                            1 -> alertLogin(act, "该账号于 $mes 在另一台设备修改了密码。请重新登录。")
                            2 -> alertLogin(act, "该账号于 $mes 在另一台设备修改了手机号。请重新登录。")
                        }
                    }
                }
                }
            }
        }


    private fun jPush(context: Context, alias: String) {
        JPushInterface.setAliasAndTags(context, alias, null) { i, s, set -> }
    }

    private fun alertLogin(context: Activity, str: String) {
        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.jpush_dialog, null)
        builder.setView(view)
        builder.setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val title = view.findViewById<TextView>(R.id.title)
        title.text = str
        val sure = view.findViewById<TextView>(R.id.sure)
        sure.setOnClickListener {
            context.startActivity<LoginActivity>()
        }
    }

    private fun alert(context: Activity, str: String) {
        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.jpush_login_dialog, null)
        builder.setView(view)
        builder.setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val title = view.findViewById<TextView>(R.id.title)
        title.text = str
        val sure = view.findViewById<TextView>(R.id.sure)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        sure.text = "修改密码"
        sure.setOnClickListener {
            alertDialog.dismiss()
            context.startActivity<ChangePasswordActivity>()
        }
        cancel.setOnClickListener {
            alertDialog.dismiss()
        }
    }
}

