package card.com.allcard.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import android.widget.LinearLayout
import card.com.allcard.R
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.utils.LogUtils
import card.com.allcard.utils.MyNetUtils
import com.just.agentweb.AgentWeb
import com.pawegio.kandroid.runDelayed
import kotlinx.android.synthetic.main.activity_web_other.*

class WebNew : BaseActivity() {
    private var agentWeb: AgentWeb? = null
    private var url = ""
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetJavaScriptEnabled")
    override fun layoutId(): Int = R.layout.activity_web_other
    private var before = ""
    private var bytes: ByteArray? = null
    override fun initView() {
        MyApplication.instance.addActivity(this)
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        url = intent.getStringExtra("url")
        assert(web_view != null)
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(web_view!!, LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(resources.getColor(R.color.transparent), 0)
                .setWebViewClient(mWebViewClient)
                .setMainFrameErrorView(R.layout.view_no_web, -1)
                .createAgentWeb()
                .go(url)
        //自适应屏幕
        val webSettings1 = agentWeb!!.agentWebSettings.webSettings
        webSettings1.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings1.loadWithOverviewMode = true // 缩放至屏幕的大小
        agentWeb!!.webCreator.webView.scrollBarSize = 0
        agentWeb!!.jsInterfaceHolder.addJavaObject("ChangeIcon", ChangeIcon())
        if (Build.VERSION.SDK_INT >= 21) {
            val webSettings = agentWeb!!.agentWebSettings.webSettings
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        if (!MyNetUtils.isNetworkConnected(this)) {
            utils.showToast("请检查是否连接网络,或连接的网络未登录")
        }
        no_web.setOnClickListener {
            agentWeb!!.urlLoader.loadUrl(url)
        }
    }

    inner class ChangeIcon {
        @JavascriptInterface
        fun goBack() {
            if (agentWeb!!.back()) {
                agentWeb!!.back()
            }
        }

        @JavascriptInterface
        fun goClose() {
            finish()
        }

        @JavascriptInterface
        fun goFour() {
            web_view!!.post { MainActivity.instance!!.tabHost!!.currentTab = 3 }
            runDelayed(500){
                finish()
            }
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_BACK) {
            if (agentWeb!!.back()) {
                agentWeb!!.back()
            } else {
                finish()
            }
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    private val mWebViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
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
                    no_web.visibility = View.VISIBLE
                    utils.showToast("请检查是否连接网络,或连接的网络未登录")
            }
            if (request!!.isForMainFrame) {
                no_web.visibility = View.VISIBLE
                utils.showToast("请检查是否连接网络,或连接的网络未登录")
            }
        }

        override fun onPageFinished(view: WebView?, urltwo: String?) {
            super.onPageFinished(view, urltwo)
            url = urltwo!!
            f_view4.visibility = View.GONE
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

}
