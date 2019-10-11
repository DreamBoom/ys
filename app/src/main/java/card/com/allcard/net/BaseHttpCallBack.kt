package card.com.allcard.net

import android.app.Activity
import android.util.Log
import android.widget.Toast
import card.com.allcard.utils.LogUtils
import org.xutils.common.Callback

/**
 * @author Created by Dream
 */
open class BaseHttpCallBack(
        //private final int CODE_0 = 0;//请求成功
        // private final int CODE_102 = 102;//token已失效
        private val aty: Activity) : Callback.CommonCallback<String> {

    override fun onSuccess(s: String) {
        // utils.showToast(s)
        LogUtils.i("TAG请求成功========>",s)
        success(s)
    }

    open fun failed(desc: String) {
        Toast.makeText(aty, desc, Toast.LENGTH_SHORT).show()
    }

    open fun success(data: String) {}

    override fun onError(throwable: Throwable, b: Boolean) {
        Log.i("TAG加载失败了========>", throwable.message.toString())
      //  utils.showToast("加载失败，请重试")
    }


    override fun onCancelled(e: Callback.CancelledException) {}

    override fun onFinished() {}
}
