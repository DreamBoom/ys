package card.com.allcard.wxapi

import android.content.Intent
import android.support.v4.content.ContextCompat
import card.com.allcard.R
import card.com.allcard.activity.BaseActivity
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.tools.Tool
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.empty.*

class WXPayEntryActivity : BaseActivity(), IWXAPIEventHandler {
    private var api: IWXAPI? = null
    override fun layoutId(): Int = R.layout.empty
    override fun initView() {
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        MyApplication.instance.addActivity(this)
        utils.changeStatusBlack(true, window)
        api = WXAPIFactory.createWXAPI(this,"wx1467bf6a8cf0dffd")
        api!!.handleIntent(intent, this)
        sure.setOnClickListener {
            finish()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        api!!.handleIntent(intent, this)
    }

    override fun onReq(req: BaseReq) {
        payResult.text = "支付完成"
    }

    override fun onResp(resp: BaseResp) {
        if (resp.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            when(resp.errCode){
                0 -> {
                    mk.encode(Tool.WxResult,"0")
                    im.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.jycg_icon))
                    payResult.text = "支付成功"}
                -1 -> {
                    mk.encode(Tool.WxResult,"-1")
                    im.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.jysb_icon))
                    payResult.text = "支付失败"}
                -2 -> {
                    mk.encode(Tool.WxResult,"-2")
                    im.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.jysb_icon))
                    payResult.text = "支付取消"}
            }
        }
    }
}
