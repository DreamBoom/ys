package card.com.allcard.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.LinearLayout
import card.com.allcard.R
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import card.com.allcard.utils.MyNetUtils
import com.example.caller.BankABCCaller
import com.just.agentweb.AgentWeb
import com.pawegio.kandroid.runDelayed
import kotlinx.android.synthetic.main.activity_web2.*

class WebActivity2 : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_web2

    companion object {
        @SuppressLint("StaticFieldLeak")
        var mAgentWeb: AgentWeb? = null
        var urlNow = ""
        var payNum1: List<String>? = null
    }

    var android = "android"
    private var userId: String? = null
    private var url = ""
    private var i = "&"
    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        bar!!.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        userId = mk.decodeString(Tool.USER_ID, "")
        url = HttpRequestPort.H5_BASE_URL + "weixin/wxindexdd.jsp?fixparam=android&user_id=" + userId!!
        //拦截到js交互，延迟一秒展示网页，遮住闪屏
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(web_view!!, LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(resources.getColor(R.color.transparent), 0)
                .setWebViewClient(mWebViewClient)
                .setMainFrameErrorView(R.layout.view_no_web, -1)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                .createAgentWeb()
                .go(url)
        refresh.setEnableOverScrollDrag(false)
        mAgentWeb!!.jsInterfaceHolder.addJavaObject("ChangeIcon", ChangeIcon())
        val webSettings = mAgentWeb!!.agentWebSettings.webSettings
        // webSettings.textZoom = 100
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        /*下拉刷新加载网页*/
        refresh!!.setOnRefreshListener { refresh ->
            refresh!!.finishRefresh()
            //重新刷新页面
            if (MyNetUtils.isNetworkConnected(this@WebActivity2)) {
                userId = mk.decodeString(Tool.USER_ID, "")
                url = HttpRequestPort.H5_BASE_URL + "weixin/wxindexdd.jsp?fixparam=android&user_id=" + userId!!
                mAgentWeb!!.urlLoader.loadUrl(url)
            } else {
                utils.showToast("请检查是否连接了网络,或连接的网络未登录")
            }
        }
        no_web.setOnClickListener {
            userId = mk.decodeString(Tool.USER_ID, "")
            url = HttpRequestPort.H5_BASE_URL + "weixin/wxindexdd.jsp?fixparam=android&user_id=" + userId!!
            mAgentWeb!!.urlLoader.loadUrl(url)
        }
    }

    inner class ChangeIcon {
        @JavascriptInterface
        fun getUserId(): String {
            return mk.decodeString(Tool.USER_ID, "")
        }

        //跳地图
        @JavascriptInterface
        fun goMap(name: String) {
            val split = name.split(",")
            //114.807316,34.838484,医院4,兰考县兰阳路,0391-8559489
            val intent = Intent(this@WebActivity2,Map::class.java)
            intent.putExtra("name",split[2])
            intent.putExtra("phone",split[2])
            intent.putExtra("address",split[2])
            intent.putExtra("lat",split[2])
            intent.putExtra("Lng",split[2])
            startActivity(intent)
        }

        //账户冻结解冻
        @JavascriptInterface
        fun goWebPost(url: String) {
            utils.startOtherWeb("${HttpRequestPort.H5_BASE_URL}$url&fixparam=android&user_id=$userId")
        }

        @JavascriptInterface
        fun goWeb(url: String) {
            utils.startWeb(url)
            //打开系统浏览器
//            val uri = Uri.parse(url)
//            val intent = Intent(Intent.ACTION_VIEW, uri)
//            startActivity(intent)
        }

        @JavascriptInterface
        fun goWebOther(url: String) {
            utils.startOtherWeb(url)
        }

        @JavascriptInterface
        fun toLogin() {
            startActivity(Intent(this@WebActivity2, LoginActivity::class.java).putExtra("from", 1))
        }

        @JavascriptInterface
        fun pay(payNum: String) {
            utils.hideSoftKeyboard()
            Log.i("payNum======>", payNum)
            payNum1 = payNum.split(",")
            if (BankABCCaller.isBankABCAvaiable(this@WebActivity2)) {
                mk.encode(Tool.BY_LOGIN, "1")
                /**调起农行掌银*/
                BankABCCaller.startBankABC(this@WebActivity2,
                        "card.com.allcard",
                        "card.com.allcard.activity.MainActivity",
                        "pay", payNum1!![0])
            } else {//客户手机未安装农行掌银APP的处理逻辑，由第三方APP自行实现
                mAgentWeb!!.jsAccessEntrace.quickCallJs("payResult('2$i$userId$i$android$i$payNum1')")
                utils.showToast("未安装农行掌银，或已安装农行掌银版本不支持")
                payNum1 = null
            }
        }
    }

    private val mWebViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            Log.i("WebActivity2==>", url!!)
            refresh.finishRefresh()
            refresh.isEnableRefresh = false
            f_view2.visibility = View.VISIBLE
            urlNow = url!!
            runDelayed(1500) {
                no_web.visibility = View.GONE
            }
        }

        override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
            super.onReceivedHttpError(view, request, errorResponse)
            Log.i("HttpError===>", "" + errorResponse!!.statusCode)
            f_view2.visibility = View.GONE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return
            }
            // 在这里显示自定义错误页
            no_web.visibility = View.VISIBLE
            utils.showToast("网络连接失败")
        }

        @RequiresApi(api = 23)
        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
            f_view2.visibility = View.GONE
            val errorCode = error!!.errorCode
            Log.i("ReceivedError321>", "" + errorCode)
            // 断网或者网络连接超时
            if (errorCode == ERROR_HOST_LOOKUP || errorCode == ERROR_TIMEOUT) {
                //view.loadUrl("about:blank"); //避免出现默认的错误界面
                //下面为农行跳转放行
                if (!request!!.url.toString().contains("abchina")) {
                    no_web.visibility = View.VISIBLE
                    utils.showToast("网络连接失败")
                }
            }
            if (request!!.isForMainFrame) {
                no_web.visibility = View.VISIBLE
                utils.showToast("网络连接失败")
            }
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            urlNow = url!!
            f_view2.visibility = View.GONE
            when {
                url.contains("wxindexdd") -> {
                    refresh.isEnableRefresh = true
                    bar.setBackgroundDrawable(ContextCompat.getDrawable(this@WebActivity2, R.drawable.img_sy_top))
                }
                url.contains("hospitalHome") && MyNetUtils.isNetworkConnected(this@WebActivity2) -> {
                    utils.changeStatusBlack(true, window)
                    bar.setBackgroundDrawable(ContextCompat.getDrawable(this@WebActivity2, R.drawable.cdl_top))
                }
                url.contains("smzInfo.do") && MyNetUtils.isNetworkConnected(this@WebActivity2) -> {
                    utils.changeStatusBlack(false, window)
                    bar.setBackgroundDrawable(ContextCompat.getDrawable(this@WebActivity2, R.drawable.img_ztlbg))
                }
                else -> {
                    refresh.finishRefresh()
                    refresh.isEnableRefresh = false
                    bar.setBackgroundDrawable(ContextCompat.getDrawable(this@WebActivity2, R.drawable.img_sy_top))
                }
            }
        }
    }

    override fun onPause() {
        mAgentWeb!!.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb!!.webLifeCycle.onResume()
        super.onResume()
    }

    @JavascriptInterface
    fun getDeviceId(): String {
        return utils.getDeviceId(this@WebActivity2)
    }

    override fun onDestroy() {
        mAgentWeb!!.webLifeCycle.onDestroy()
        super.onDestroy()
    }
}
