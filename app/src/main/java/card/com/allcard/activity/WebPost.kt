package card.com.allcard.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.webkit.*
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import card.com.allcard.R
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import card.com.allcard.utils.MyNetUtils
import com.example.caller.BankABCCaller
import com.just.agentweb.AgentWeb
import com.pawegio.kandroid.runDelayed
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.activity_web_post.*

class WebPost : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_web_post
    private var userId: String? = null
    private var url: String? = null
    private var urlNow = ""
    private var before = ""
    private var bytes: ByteArray? = null
    private var agentWeb: AgentWeb? = null
    var payNum1: List<String>? = null
    var api: IWXAPI? = null
    var wxPayNum = ""
    val five = "5"
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        MyApplication.instance.addActivity(this)
        bar!!.layoutParams.height = utils.getStatusBarHeight(this)
        userId = mk.decodeString(Tool.USER_ID, "")
        utils.changeStatusBlack(true, window)
        api = WXAPIFactory.createWXAPI(this, "wx1467bf6a8cf0dffd", true)
        url = intent.getStringExtra("url")
        before = url!!.substringBefore("?")
        val end = url!!.substringAfter("?")
        val end1 = if (TextUtils.isEmpty(end)) {
            "fixparam=android&user_id=$userId"
        } else {
            "$end&fixparam=android&user_id=$userId"
        }
        bytes = end1.toByteArray()
        assert(web_view != null)
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(web_view!!, LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(resources.getColor(R.color.transparent), 0)
                .setWebViewClient(mWebViewClient)
                .setMainFrameErrorView(R.layout.view_no_web, -1)
                .createAgentWeb()
                .go(null)
        agentWeb!!.jsInterfaceHolder.addJavaObject("ChangeIcon", ChangeIcon())
        val webSettings = agentWeb!!.agentWebSettings.webSettings

        //   webSettings.textZoom = 100
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        if (!MyNetUtils.isNetworkConnected(this)) {
            utils.showToast("请检查是否连接了网络,或连接的网络未登录")
        } else {
            //加载各服务网页
            agentWeb!!.urlLoader.postUrl(before, bytes)
        }
        no_web.setOnClickListener {
            agentWeb!!.urlLoader.postUrl(before, bytes)
        }
    }


    inner class ChangeIcon {
        /**
         * 定义JS需要调用的方法
         */
        @JavascriptInterface
        fun toLogin() {
            startActivity(Intent(this@WebPost, LoginActivity::class.java).putExtra("from", 1))
        }

        @JavascriptInterface
        fun close() {
            finish()
        }

        @JavascriptInterface
        fun goWeb(url:String) {
            utils.startWeb(url)
            //打开系统浏览器
//            val uri = Uri.parse(url)
//            val intent = Intent(Intent.ACTION_VIEW, uri)
//            startActivity(intent)
        }

        @JavascriptInterface
        fun realName() {
            mk.encode(Tool.IS_AUTH, "0")
        }
        @JavascriptInterface
        fun pay(payNum: String) {
            val i = "&"
            val android = "android"
            utils.hideSoftKeyboard()
            payNum1 = payNum.split(",")
            if (BankABCCaller.isBankABCAvaiable(this@WebPost)) {
                mk.encode(Tool.BY_LOGIN, "1")
                /**调起农行掌银*/
                Log.i("农行掌银==>",payNum1!![0])
                BankABCCaller.startBankABC(this@WebPost,
                        "card.com.allcard",
                        "card.com.allcard.activity.WebPost",
                        "pay", payNum1!![0])
            } else {//客户手机未安装农行掌银APP的处理逻辑，由第三方APP自行实现
                agentWeb!!.jsAccessEntrace.quickCallJs("payResult('2$i$userId$i$android$i$payNum1$i$five')")
                utils.showToast("未安装农行掌银，或已安装农行掌银版本不支持")
            }
        }


        @JavascriptInterface
        fun wxPay(result: String) {
            val split = result.split("@")
            val json = org.json.JSONObject(split[0])
            if (!TextUtils.isEmpty(split[1])) {
                wxPayNum = split[1]
                val req = PayReq()
                req.nonceStr = json.getString("noncestr")
                req.packageValue = json.getString("package")
                req.partnerId = json.getString("partnerid")
                req.prepayId = json.getString("prepayid")
                req.sign = json.getString("sign")
                req.appId = json.getString("sub_appid")
                req.timeStamp = json.getString("timestamp")
                api!!.sendReq(req)
            }
        }
    }
    private val mWebViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            runDelayed(1500){
                no_web.visibility = View.GONE
            }
            f_view5.visibility = View.VISIBLE
        }

        override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
            super.onReceivedHttpError(view, request, errorResponse)
            f_view5.visibility = View.GONE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return
            }
            // 在这里显示自定义错误页
            no_web.visibility = View.VISIBLE
            utils.showToast("请检查是否连接了网络,或连接的网络未登录")
        }

        @RequiresApi(api = 23)
        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
            f_view5.visibility = View.GONE
            val errorCode = error!!.errorCode
            // 断网或者网络连接超时
            if (errorCode == ERROR_HOST_LOOKUP || errorCode == ERROR_TIMEOUT) {
                //view.loadUrl("about:blank"); //避免出现默认的错误界面
                //下面为农行跳转放行
                if(!request!!.url.toString().contains("abchina")) {
                    no_web.visibility = View.VISIBLE
                    utils.showToast("请检查是否连接了网络,或连接的网络未登录")
                }
            }
            if (request!!.isForMainFrame) {
                no_web.visibility = View.VISIBLE
                utils.showToast("请检查是否连接了网络,或连接的网络未登录")
            }
        }

        override fun onPageFinished(view: WebView?, urltwo: String?) {
            super.onPageFinished(view, urltwo)
            Log.i("postWeb===>",urltwo)
            urlNow = urltwo!!
            f_view5.visibility = View.GONE
            if (urltwo.contains("WeiMemberuserController/smzInfo.do")) {
                bar.setBackgroundDrawable(ContextCompat.getDrawable(this@WebPost, R.drawable.img_ztlbg))
                utils.changeStatusBlack(false, window)
            } else {
                bar.setBackgroundDrawable(ContextCompat.getDrawable(this@WebPost, R.drawable.img_sy_top))
                utils.changeStatusBlack(true, window)
            }
        }
    }

    override fun onPause() {
        agentWeb!!.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        agentWeb!!.webLifeCycle.onResume()
        agentWeb!!.clearWebCache()
        val userId = mk!!.decodeString(Tool.USER_ID, "")
        val android = "android"
        val i = "&"
        val extra = intent.getStringExtra("from_bankabc_param")
        if (extra != null) {
            BaseActivity.mk.encode(Tool.BY_LOGIN, "0")
            val code = extra.split("&")[1].split("=")[1]
            val payNum1 = payNum1!![1]
            if ("0000" == code) {
                agentWeb!!.jsAccessEntrace.quickCallJs("payResult('0$i$userId$i$android$i$payNum1$i$five')")
            } else {
                when (code) {
                    "9999" -> utils.showToast("取消支付")
                    "PA500401" -> utils.showToast("已存在成功支付的订单")
                    else -> {
                        utils.showToast("支付失败")
                    }
                }
                agentWeb!!.jsAccessEntrace.quickCallJs("payResult('1$i$userId$i$android$i$payNum1$i$five')")
            }
        }
        //以下为微信支付回调处理
        val wx = mk.decodeString(Tool.WxResult, "")
        mk.encode(Tool.WxResult,"")
        if(wx == ""){
            return
        }
        if(wx == "0") {
            agentWeb!!.jsAccessEntrace.quickCallJs("payWxResult('0$i$userId$i$android$i$payNum1')")
        }else{
            agentWeb!!.jsAccessEntrace.quickCallJs("payWxResult('1$i$userId$i$android$i$payNum1')")
        }
    }

    override fun onDestroy() {
        agentWeb!!.webLifeCycle.onDestroy()
        super.onDestroy()
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_BACK) {
            when {
                urlNow.contains("cardBalance.jsp") -> {
                    agentWeb!!.clearWebCache()
                    finish()
                }
                urlNow.contains("certification/certifno.jsp") -> {
                    agentWeb!!.clearWebCache()
                    finish()
                }
                urlNow.contains("95516") -> {
                    showPop()
                    showPop!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0)
                }
                urlNow.contains("404") -> {
                    finish()
                }
                else -> agentWeb!!.jsAccessEntrace.quickCallJs("getUrl")

            }
            return false
        }
        return super.dispatchKeyEvent(event)
    }

    private var showPop: PopupWindow? = null
    fun showPop(){
        val view = LayoutInflater.from(this).inflate(R.layout.exit_dialog, null)
        val sure = view.findViewById<TextView>(R.id.sure)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        sure.text = "确定"
        val title = view.findViewById<TextView>(R.id.title)
        title.text = "确定放弃本次交易?"
        showPop = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true)
        showPop!!.isTouchable = true
        showPop!!.isOutsideTouchable = false
        val dw = ColorDrawable(0x00000000)
        showPop!!.setBackgroundDrawable(dw)
        sure.setOnClickListener {
            val userId = mk!!.decodeString(Tool.USER_ID, "")
            val postDate = "fixparam=android&user_id=$userId"
            val bytes = postDate.toByteArray()
            agentWeb!!.urlLoader.postUrl(HttpRequestPort.H5_BASE_URL + "weixin/accountRecharge/accountRecharge.jsp", bytes)
            showPop!!.dismiss()
        }
        cancel.setOnClickListener {
            showPop!!.dismiss()
        }
    }
}
