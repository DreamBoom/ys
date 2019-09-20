package card.com.allcard.activity

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import card.com.allcard.R
import card.com.allcard.bean.HeaderImgBean
import card.com.allcard.getActivity.MyApplication
import card.com.allcard.net.BaseHttpCallBack
import card.com.allcard.net.HttpRequestPort
import card.com.allcard.tools.Tool
import card.com.allcard.utils.BitmapUtils
import card.com.allcard.view.MyGlideEngine
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.pawegio.kandroid.runDelayed
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.PermissionNo
import com.yanzhenjie.permission.PermissionYes
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.android.synthetic.main.title.*
import org.xutils.image.ImageOptions
import org.xutils.x

/**
 * @author Created by Dream
 */

class UserInfoActivity : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_user_info
    private var options: ImageOptions? = null
    var userId = ""
    var imHeader = ""
    var type = "0"
    override fun initView() {
        MyApplication.instance.addActivity(this)
        bar.layoutParams.height = utils.getStatusBarHeight(this)
        utils.changeStatusBlack(true, window)
        address!!.text = "设置头像"
        userId = mk.decodeString(Tool.USER_ID, "")
        imHeader = mk.decodeString(Tool.HEADER, "")
        options = ImageOptions.Builder()
                .setConfig(Bitmap.Config.ARGB_8888)
                .setSquare(true)
                .setCrop(false)
                .setFadeIn(true)
                .setCircular(false)
                .build()
        x.image().bind(im_photo, imHeader, options)
        close.setOnClickListener { finish() }
        take_photo.setOnClickListener {
            type = "0"
            AndPermission.with(this)
                    .requestCode(300)
                    .permission(Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    // .rationale(...)
                    .callback(this)
                    .start()
        }
    }

    private fun header(uri: Uri, i: Int) {
        val mt = android.os.Build.BRAND
        var bitm = BitmapUtils.uriToBitmap(this, uri)
        if (i == 0) {
            bitm = when (mt) {
                "xiaomi" -> utils.degree(bitm)
                "Xiaomi" -> utils.degree(bitm)
                else -> bitm
            }
        }
        val file = BitmapUtils.bitToFile(bitm)
        utils.getProgress(this)
        //上传头像
        HttpRequestPort.instance.upLoad(file!!, userId, imHeader, object : BaseHttpCallBack(this) {
            override fun success(data: String) {
                super.success(data)
                val headerImgBean = JSONObject.parseObject(data, object : TypeReference<HeaderImgBean>() {})
                utils.showToast(headerImgBean.message)
                val status = headerImgBean.result
                if (!TextUtils.isEmpty(status) && "0" == status) {
                    val imgUrl = headerImgBean.imgurl
                    mk.encode(Tool.HEADER, imgUrl)
                    runDelayed(1000){
                        x.image().bind(im_photo,imgUrl,options)
                        utils.hindProgress()
                    }
                }
            }

            override fun onError(throwable: Throwable, b: Boolean) {
                super.onError(throwable, b)
                utils.showToast("请求失败，请稍后重试")
                utils.hindProgress()
            }
        })

    }

    // 成功回调的方法，用注解即可，这里的300就是请求时的requestCode。
    @PermissionYes(300)
    private fun getPermissionYes(grantedPermissions: List<String>) {
        //申请权限成功
        //防止返回锁屏
        mk.encode(Tool.BY_LOGIN, "1")
//        val tempFile = File(Environment.getExternalStorageDirectory(), "test.jpg") //设置截图后的保存路径
//        val uri = Uri.fromFile(tempFile)
//        val options = UCrop.Options()//uCrop的参数设置
//        options.setCircleDimmedLayer(false)
//        options.setCropGridColumnCount(0)
//        options.setCropGridRowCount(0)
//        options.setShowCropFrame(true)//设置是否显示裁剪边框(true为方形边框)
//        options.setHideBottomControls(false)
//        //options.setMaxScaleMultiplier(1.1f)
//        options.setAllowedGestures(UCropActivity.NONE, UCropActivity.NONE, UCropActivity.NONE)
//        options.setToolbarColor(ContextCompat.getColor(this, R.color.blue))
//        options.setStatusBarColor(ContextCompat.getColor(this, R.color.blue))


//         options.setMaxScaleMultiplier(5);
//        options.setImageToCropBoundsAnimDuration(666);
//        options.setDimmedLayerColor(Color.CYAN);
//        options.setCircleDimmedLayer(true);
//        options.setShowCropFrame(false);
//        options.setCropGridStrokeWidth(20);
//        options.setCropGridColor(Color.GREEN);
//        options.setCropGridColumnCount(0);
//        options.setCropGridRowCount(0);
//        options.setToolbarCropDrawable(R.drawable.your_crop_icon);
//        options.setToolbarCancelDrawable(R.drawable.your_cancel_icon);
//        // Color palette
//        options.setToolbarColor(ContextCompat.getColor(this, R.color.your_color_res));
//        options.setStatusBarColor(ContextCompat.getColor(this, R.color.your_color_res));
//        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.your_color_res));
//        options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.your_color_res));
//        options.setRootViewBackgroundColor(ContextCompat.getColor(this, R.color.your_color_res));
//        // Aspect ratio options
//        options.setAspectRatioOptions(1,
//            new AspectRatio("WOW", 1, 2),
//            new AspectRatio("MUCH", 3, 4),
//            new AspectRatio("RATIO", CropImageView.DEFAULT_ASPECT_RATIO, CropImageView.DEFAULT_ASPECT_RATIO),
//            new AspectRatio("SO", 16, 9),
//            new AspectRatio("ASPECT", 1, 1));
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG)) // 选择 mime 的类型
                .countable(true)
                .crop(false)     //设置为true后，才会进入截图模式，默认为false，进入为知乎普通图片选择器
                // .cropOptions(options) //设置uCrop裁剪参数
                // .cropUri(uri)         //设置截图后的保存路径
                .maxSelectable(1) // 图片选择的最多数量
                .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(1f) // 缩略图的比例
                .imageEngine(MyGlideEngine()) // 使用的图片加载引擎
                .capture(true) //是否提供拍照功能
                .captureStrategy(CaptureStrategy(true, "com.allcard.fileprovider"))//存储到哪里
                .forResult(201) // 设置作为标记的请求码
    }

    @PermissionNo(300)
    private fun getPermissionNo(deniedPermissions: List<String>) {
        // 申请权限失败。
        // 是否有不再提示并拒绝的权限
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            // 第一种：用AndPermission默认的提示语。
            //AndPermission.defaultSettingDialog(this, 400).show()
            promess()
        }
    }

    //存储权限被拒弹窗
    private fun promess() {
        val v = utils.getView(this, R.layout.pop_prossmess)
        val promess = PopupWindow(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        promess.contentView = v
        promess.setBackgroundDrawable(ColorDrawable(0x00000000))
        promess.isClippingEnabled = false
        promess.showAsDropDown(bar)
        v.findViewById<TextView>(R.id.tv_2).text = "相机及存储权限被拒绝，请在设置中授权后重试"
        //存储权限被拒绝，请在设置中授权后重试
        v.findViewById<TextView>(R.id.tv_3).setOnClickListener {
            promess.dismiss()
        }
        v.findViewById<TextView>(R.id.tv_4).setOnClickListener {
            promess.dismiss()
            startActivity( Intent(Settings.ACTION_SETTINGS))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 201 && resultCode == RESULT_OK) {
            val mSelected = Matisse.obtainResult(data)
            // Log.i("===>", mSelected[0].path)
            mk.encode(Tool.BY_LOGIN, "0")
            if (mSelected[0].path!!.contains(".")) {
                header(mSelected[0], 0)
            } else {
                header(mSelected[0], 1)
            }
        }
    }
}
