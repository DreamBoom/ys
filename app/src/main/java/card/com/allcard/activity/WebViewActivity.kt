package card.com.allcard.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.*
import android.webkit.*
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import card.com.allcard.R
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.tools.Tool
import card.com.allcard.utils.MyNetUtils
import com.just.agentweb.AgentWeb
import com.pawegio.kandroid.runDelayed
import kotlinx.android.synthetic.main.activity_web_view.*

/**
 * @author Created by Dream
 */
class WebViewActivity : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_web_view
    private var userId: String? = null
    private var url = ""
    var urlNow = ""
    private var agentWeb: AgentWeb? = null
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        MyApplication.instance.addActivity(this)
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        userId = mk.decodeString(Tool.USER_ID, "")
        url = intent.getStringExtra("url")
        urlNow = url
        assert(web_view != null)
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(web_view!!, LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(resources.getColor(R.color.transparent), 0)
                .setWebViewClient(mWebViewClient)
                .setMainFrameErrorView(R.layout.view_no_web, -1)
                .createAgentWeb()
                .go(url)
        agentWeb!!.webCreator.webView.scrollBarSize = 0
        agentWeb!!.jsInterfaceHolder.addJavaObject("ChangeIcon", ChangeIcon())
        val webSettings = agentWeb!!.agentWebSettings.webSettings
        //webSettings.textZoom = 100
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        if (!MyNetUtils.isNetworkConnected(this)) {
            utils.showToast("请检查是否连接网络,或连接的网络未登录")
        }
        no_web.setOnClickListener {
            agentWeb!!.urlLoader.loadUrl(urlNow)
        }
    }

    inner class ChangeIcon {
        @JavascriptInterface
        fun toLogin() {
            startActivity(Intent(this@WebViewActivity, ForgetPassword::class.java).putExtra("from", 0))
        }

        @JavascriptInterface
        fun close() {
            finish()
        }

        @JavascriptInterface
        fun realName() {
            mk.encode(Tool.IS_AUTH, "0")
        }

        @JavascriptInterface
        fun goWeb(url: String) {
            utils.startWeb(url)
            //打开系统浏览器
//            val uri = Uri.parse(url)
//            val intent = Intent(Intent.ACTION_VIEW, uri)
//            startActivity(intent)
        }
        //跳地图
        @JavascriptInterface
        fun goMap(name: String) {
            val split = name.split(",")
            //114.807316,34.838484,医院4,兰考县兰阳路,0391-8559489
            val intent = Intent(this@WebViewActivity,Map::class.java)
            intent.putExtra("name",split[2])
            intent.putExtra("phone",split[2])
            intent.putExtra("address",split[2])
            intent.putExtra("lat",split[2])
            intent.putExtra("Lng",split[2])
            startActivity(intent)
        }

        @JavascriptInterface
        fun goWebOther(url: String) {
            utils.startOtherWeb(url)
            //打开系统浏览器
//            val uri = Uri.parse(url)
//            val intent = Intent(Intent.ACTION_VIEW, uri)
//            startActivity(intent)
        }


        @JavascriptInterface
        fun getVersion(): String {
            return utils.version
        }
    }

    private val mWebViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            Log.i("WebViewActivity==>", url)
            runDelayed(1500) {
                no_web.visibility = View.GONE
            }
            f_view4.visibility = View.VISIBLE
        }

        override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
            super.onReceivedHttpError(view, request, errorResponse)
            f_view4.visibility = View.GONE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return
            }
            // 在这里显示自定义错误页
            no_web.visibility = View.VISIBLE
            utils.showToast("请检查是否连接网络,或连接的网络未登录")
        }

        @RequiresApi(api = 23)
        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
            f_view4.visibility = View.GONE
            val errorCode = error!!.errorCode
            // 断网或者网络连接超时
            if (errorCode == ERROR_HOST_LOOKUP || errorCode == ERROR_TIMEOUT) {
                //view.loadUrl("about:blank"); //避免出现默认的错误界面
                //下面为农行跳转放行
                if (!request!!.url.toString().contains("abchina")) {
                    no_web.visibility = View.VISIBLE
                    utils.showToast("请检查是否连接网络,或连接的网络未登录")
                }
            }
            if (request!!.isForMainFrame) {
                no_web.visibility = View.VISIBLE
                utils.showToast("请检查是否连接网络,或连接的网络未登录")
            }
        }

        override fun onPageFinished(view: WebView?, urltwo: String?) {
            super.onPageFinished(view, urltwo)
            urlNow = urltwo!!
            f_view4.visibility = View.GONE
            when {
                urltwo.contains("hospitalHome") && MyNetUtils.isNetworkConnected(this@WebViewActivity) -> {
                    utils.changeStatusBlack(true, window)
                    bar.setBackgroundDrawable(ContextCompat.getDrawable(this@WebViewActivity, R.drawable.cdl_top))
                }
                urltwo.contains("smzInfo.do") && MyNetUtils.isNetworkConnected(this@WebViewActivity) ->{
                    utils.changeStatusBlack(false, window)
                    bar.setBackgroundDrawable(ContextCompat.getDrawable(this@WebViewActivity, R.drawable.img_ztlbg))
                }

                else -> {
                    utils.changeStatusBlack(true, window)
                    bar.setBackgroundDrawable(ContextCompat.getDrawable(this@WebViewActivity, R.drawable.img_sy_top))
                }
            }
        }
    }

    override fun onPause() {
        agentWeb!!.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onResume() {
        agentWeb!!.webLifeCycle.onResume()
        agentWeb!!.clearWebCache()
        super.onResume()
    }

    override fun onDestroy() {
        agentWeb!!.webLifeCycle.onDestroy()
        super.onDestroy()
    }

    private var showPop: PopupWindow? = null
    private fun showPop() {
        val view = LayoutInflater.from(this).inflate(R.layout.exit_dialog, null)
        val sure = view.findViewById<TextView>(R.id.sure)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        sure.text = "确定"
        val title = view.findViewById<TextView>(R.id.title)
        title.text = "确定结束本次交易?"
        showPop = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true)
        showPop!!.isTouchable = true
        showPop!!.isOutsideTouchable = false
        val dw = ColorDrawable(0x00000000)
        showPop!!.setBackgroundDrawable(dw)
        sure.setOnClickListener {
            showPop!!.dismiss()
            finish()
        }
        cancel.setOnClickListener {
            showPop!!.dismiss()
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_BACK) {
            if (!urlNow.contains("wxinindex")) {
                if (!urlNow.contains("wxindex")) {
                    if (MyNetUtils.isNetworkConnected(this)) {
                        if (urlNow.contains("95516.com/pages/wap/reg.html")
                                || urlNow.contains("user.95516.com/pages/misc/newAgree")
                                || urlNow.contains("!result?r=")
                                || urlNow.contains("95516.com/pages/wap/findpwd")) {
                            //处理银联广告
                            agentWeb!!.back()
                        } else {
                            if (urlNow.contains("95516")
                                    ||urlNow.contains("render.alipay")) {
                                //处理银联支付高等无法返回
                                showPop()
                                showPop!!.showAtLocation(bar, Gravity.NO_GRAVITY, 0, 0)
                            } else {
                                agentWeb!!.jsAccessEntrace.quickCallJs("getUrl")
                            }
                        }
                    } else {
                        agentWeb!!.back()
                    }
                }
            }
            return false
        }
        return super.dispatchKeyEvent(event)
    }
}
