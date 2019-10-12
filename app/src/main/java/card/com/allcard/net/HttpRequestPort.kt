package card.com.allcard.net

import android.util.Log
import java.io.File
import java.util.*

/**
 * @author Created by Dream
 * 网络接口
 */
class HttpRequestPort {
    companion object {
        /** 端口 + 头像端口  */

//        const val BASE_URL = "http://192.168.12.9:8080/app/"//留款
//        const val BASEURL = "http://192.168.12.9:8080"
//        const val H5_BASE_URL = "http://192.168.12.9:8080/"

//        const val BASE_URL = "http://192.168.12.10:8080/app/" //艳峰
//        const val BASEURL = "http://192.168.12.10:8080"
//        const val H5_BASE_URL = "http://192.168.12.10:8080/"

//        const val BASE_URL = "http://192.168.12.11:8080/app/" //军红
//        const val BASEURL = "http://192.168.12.11:8080"
//        const val H5_BASE_URL = "http://192.168.12.11:8080/"

//        const val BASE_URL = "http://192.168.12.8:8080/app/" //健民
//        const val BASEURL = "http://192.168.12.8:8080"
//        const val H5_BASE_URL = "http://192.168.12.8:8080/"

//        const val BASE_URL = "http://192.168.12.18:8081/app/" //18
//        const val BASEURL = "http://192.168.12.18:8081"
//        const val H5_BASE_URL = "http://192.168.12.18:8081/"

//        const val BASE_URL = "http://192.168.12.5:8080/app/"  //霍霍
//        const val BASEURL = "http://192.168.12.5:8080"
//        const val H5_BASE_URL = "http://192.168.12.5:8080/"

//        const val BASE_URL = "http://222.138.67.71:19520/app/" //兰考正式
//        const val BASEURL = "http://222.138.67.71:19520"
//        const val H5_BASE_URL = "http://222.138.67.71:19520/"

        const val BASE_URL = "http://222.138.67.71:19534/app/" //测试
        const val BASEURL = "http://222.138.67.71:19534"
        const val H5_BASE_URL = "http://222.138.67.71:19534/"

//       const val BASE_URL = "http://117.158.105.88:8887/app/" //测试
//        const val BASEURL = "http://117.158.105.88:8887"
//        const val H5_BASE_URL = "http://117.158.105.88:8887/"

        private var httpRequestPort: HttpRequestPort? = null

        val instance: HttpRequestPort
            get() {
                if (httpRequestPort == null) {
                    httpRequestPort = HttpRequestPort()
                }
                return httpRequestPort as HttpRequestPort
            }
    }

    private val LOGIN = "AppLoginController/loginuser.do"
    private val CHECKPHONE = "AppLoginController/verifyphone.do"
    private val SANDMESSAGE = "AppLoginController/sendmessage.do"
    private val REGISTER = "AppLoginController/register.do"
    private val FOEGETPASSWORD = "AppLoginController/forgotpasswd.do"
    private val HEADER = "AppMemberuserController/changeavatar.do"
    private val DEVICEBIND = "AppLoginController/deviceBind.do"
    private val deviceList = "AppLoginController/deviceList.do"
    private val changeDevice = "AppLoginController/deletedevice.do"
    private val searchData = "AppLoginController/searchData.do"
    private val sendEmail = "AppLoginController/sendEmail.do"
    private val upEmail = "AppLoginController/updateEmail.do"
    private val checkEamilcode = "AppLoginController/checkEamilcode.do"
    private val eamilBind = "AppLoginController/eamilBind.do"
    private val questionList = "AppLoginController/securityQuestionList.do"
    private val setQuestion = "AppLoginController/setsecurityQuestion.do"
    private val upQuestion = "AppLoginController/updatesecurityQuestion.do"
    private val selectAnswer = "AppLoginController/selectsecurityAnswer.do"
    private val checksmscode = "AppLoginController/checksmscode.do"
    private val upPhone = "AppLoginController/updateLoginPhone.do"
    private val upPasswd = "AppLoginController/updatepasswd.do"
    private val getVersion = "AppLoginController/latestVersion.do"
    private val mainImg = "AppLoginController/wxindexjson.do"
    private val tabTwo = "AppLoginController/wxindexdd.do"
    private val serviceList = "AppLoginController/serverQuery.do"
    //private val serviceList = "AppLoginController/serviceList.do"
    private val hospitalList = "AppLoginController/hospitalList.do"
    private val drugstoreList = "AppLoginController/pharmacyList.do"
    private val area = "AppLoginController/getArea.do"
    private val searchArea = "AppLoginController/searchArea.do"
    private val iconSave = "AppLoginController/iconSave.do"
    private val cardBind = "AppLoginController/cardBind.do"
    private val realName = "AppLoginController/smrz.do"
    private val searchYj = "AppLoginController/searchYj.do"
    private val cardDeposit = "AppLoginController/cardDeposit.do"
    private val searchYuE = "AppLoginController/searchYuE.do"
    private val yuEDetail = "AppLoginController/searchYuEDetail.do"
    private val order = "AppLoginController/searchExceptionOrder.do"
    private val quotaSelect = "AppLoginController/quotaSelect.do"
    private val quotaSet = "AppLoginController/quotaSetting.do"
    private val accountFrozen = "AppLoginController/accountFrozen.do"
    private val cardParamList = "AppLoginController/cardParamList.do"
    private val setCardParam = "AppLoginController/setCardParam.do"
    private val getAccState = "AppLoginController/getAccState.do"
    private val smzInfo = "AppLoginController/smzInfo.do"
    private val CardProgress = "AppLoginController/selectMakeCardProgress.do"
    private val cardInfo = "AppLoginController/socialCardInfo.do"
    private val smCardList = "AppLoginController/smCardList.do"
    private val unsmCardList = "AppLoginController/unsmCardList.do"
    private val reportLoss = "AppLoginController/reportLoss.do"
    private val unreportLoss = "AppLoginController/unreportLoss.do"
    private val getCardStatus = "AppLoginController/getCardStatus.do"
    private val getmemberLinkList = "AppLoginController/getmemberLinkList.do"
    private val smrz = "AppLoginController/smrz.do"
    private val updateNickName = "AppLoginController/updateNickName.do"
    private val deleteNickName = "AppLoginController/deleteNickName.do"
    private val manageType = "AppLoginController/manageType.do"
    private val ResPwd = "AppLoginController/ResPwd.do"
    private val fsmResPwd = "AppLoginController/fsmResPwd.do"
    private val searchYuEOther = "AppLoginController/searchYuEOther.do"
    private val deleteCard = "AppLoginController/deleteCard.do"
    private val baseData = "AppLoginController/baseData.do"
    private val acc = "AppLoginController/accountrecharge.do"
    private val accother = "AppLoginController/accountrechargeForother.do"
    private val isSuccess = "AppLoginController/is_success.do"

    private val httpUtil: HttpUtil = HttpUtil()
    private var map: MutableMap<String, String>? = null

    /**首页图片 */
    fun mainImg(userId: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = userId
        httpUtil[BASE_URL + mainImg, map, callBack]
    }

    /**服务页 */
    fun tabTwo(user_id: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        httpUtil[BASE_URL + tabTwo, map, callBack]
    }

    /**储存图标 */
    fun iconSave(user_id: String, icon: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        map!!["icon"] = icon
        httpUtil[BASE_URL + iconSave, map, callBack]
    }

    /**判断手机号是否注册 */
    fun checkPhone(phone: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["phone"] = phone
        httpUtil[BASE_URL + CHECKPHONE, map, callBack]
    }

    /**获取验证码 */
    fun sandMessage(phone: String, cpType: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["phone"] = phone
        map!!["cp_type"] = cpType
        httpUtil[BASE_URL + SANDMESSAGE, map, callBack]
    }

    /**注册 */
    fun register(phone: String, type: String, passwd: String,
                 checkcode: String, weiBind: String, qqBind: String,
                 sign_lock: String, fingerprint: String, sign_lock_num: String,
                 callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["phone"] = phone
        map!!["type"] = type
        map!!["passwd"] = passwd
        map!!["checkcode"] = checkcode
        map!!["wei_bind"] = weiBind
        map!!["qq_bind"] = qqBind
        map!!["sign_lock"] = sign_lock
        map!!["fingerprint"] = fingerprint
        map!!["sign_lock_num"] = sign_lock_num
        httpUtil[BASE_URL + REGISTER, map, callBack]
    }

    /**忘记密码 */
    fun forgotpasswd(device_num: String, phone: String, passwd: String, checkcode: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["phone"] = phone
        map!!["device_num"] = device_num
        map!!["passwd"] = passwd
        map!!["checkcode"] = checkcode
        httpUtil[BASE_URL + FOEGETPASSWORD, map, callBack]
    }

    /**修改密码 */
    fun upPasswd(phone: String, oldpasswd: String, passwd: String, device_num: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["phone"] = phone
        map!!["oldpasswd"] = oldpasswd
        map!!["passwd"] = passwd
        map!!["device_num"] = device_num
        httpUtil[BASE_URL + upPasswd, map, callBack]
    }

    /**设备绑定*/
    fun deviceBind(user_name: String, device_num: String, device_name: String,
                   device_type: String, device_api: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_name"] = user_name
        map!!["device_num"] = device_num
        map!!["device_name"] = device_name
        map!!["device_type"] = device_type
        map!!["device_api"] = device_api
        httpUtil[BASE_URL + DEVICEBIND, map, callBack]
    }

    /**设备变更*/
    fun changeDevice(user_id: String, device_num: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        map!!["device_num"] = device_num
        httpUtil[BASE_URL + changeDevice, map, callBack]
    }

    /**设备列表*/
    fun deviceList(user_name: String, order_page: String, page_size: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_name"] = user_name
        map!!["order_page"] = order_page
        map!!["page_size"] = page_size
        httpUtil[BASE_URL + deviceList, map, callBack]
    }

    /**登录 */
    fun login(user_name: String, passwd: String, device_num: String, device_name: String,
              device_api: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_name"] = user_name
        map!!["passwd"] = passwd
        map!!["fixparam"] = "android"
        map!!["device_num"] = device_num
        map!!["device_name"] = device_name
        map!!["device_api"] = device_api
        httpUtil[BASE_URL + LOGIN, map, callBack]
    }


    /**上传头像 */
    fun upLoad(filename: File, user_id: String, old_img: String, callBack: BaseHttpCallBack) {
        httpUtil.upload(BASE_URL + HEADER, filename, user_id, old_img, callBack)
    }

    /**拉取个人中心信息 */
    fun searchData(userId: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = userId
        httpUtil[BASE_URL + searchData, map, callBack]
    }

    /**发送邮件*/
    fun sendEmail(email: String, cp_type: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["email"] = email
        map!!["cp_type"] = cp_type
        httpUtil[BASE_URL + sendEmail, map, callBack]
    }

    /**验证邮箱验证码*/
    fun checkEamilcode(email: String, cp_type: String, code: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["email"] = email
        map!!["checkcode"] = code
        map!!["cp_type"] = cp_type
        httpUtil[BASE_URL + checkEamilcode, map, callBack]
    }

    /**邮箱绑定*/
    fun eamilBind(phone: String, email: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_name"] = phone
        map!!["email"] = email
        httpUtil[BASE_URL + eamilBind, map, callBack]
    }

    /**修改邮箱*/
    fun upEmail(userId: String, email: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = userId
        map!!["email"] = email
        httpUtil[BASE_URL + upEmail, map, callBack]
    }

    /**密保问题列表*/
    fun questionList(callBack: BaseHttpCallBack) {
        map = HashMap()
        httpUtil[BASE_URL + questionList, map, callBack]
    }

    /**设置密保问题*/
    fun setQuestion(userId: String, securityQuestion: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = userId
        map!!["securityQuestion"] = securityQuestion
        httpUtil.post(BASE_URL + setQuestion, map, callBack)
    }

    /**修改密保问题*/
    fun upQuestion(userId: String, securityQuestion: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = userId
        map!!["securityQuestion"] = securityQuestion
        httpUtil.post(BASE_URL + upQuestion, map, callBack)
    }

    /**查询密保问题*/
    fun selectAnswer(userId: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = userId
        httpUtil[BASE_URL + selectAnswer, map, callBack]
    }

    /**验证短信验证码*/
    fun checkCode(phone: String, checkcode: String, cp_type: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["phone"] = phone
        map!!["checkcode"] = checkcode
        map!!["cp_type"] = cp_type
        httpUtil[BASE_URL + checksmscode, map, callBack]
    }

    /**修改登录手机号*/
    fun upPhone(device_num: String, userId: String, phone: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["device_num"] = device_num
        map!!["phone"] = phone
        map!!["user_id"] = userId
        httpUtil[BASE_URL + upPhone, map, callBack]
    }

    /**获取最新版本号*/
    fun getVersion(callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["appType"] = "android"
        httpUtil[BASE_URL + getVersion, map, callBack]
    }

    /**首页服务指南列表 */
    fun serviceList1(type: String,callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["type"] = type
        map!!["order_page"] = "1"
        map!!["page_size"] = "3"
        httpUtil[BASE_URL + serviceList, map, callBack]
    }

    /**服务指南列表 */
    fun serviceList(type: String,callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["type"] = type
        map!!["order_page"] = "1"
        map!!["page_size"] = "100"
        httpUtil[BASE_URL + serviceList, map, callBack]
    }

    /**定点医院列表 */
    fun hospitalList(order_page: String, page_size: String, area_attr: String, searhname: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["order_page"] = order_page
        map!!["page_size"] = page_size
        map!!["area_attr"] = area_attr
        map!!["searhname"] = searhname
        Log.i("==>", BASE_URL + hospitalList)
        httpUtil[BASE_URL + hospitalList, map, callBack]
    }

    /**定点药店列表 */
    fun drugstoreList(order_page: String, page_size: String, area_attr: String, searhname: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["order_page"] = order_page
        map!!["page_size"] = page_size
        map!!["area_attr"] = area_attr
        map!!["searhname"] = searhname
        httpUtil[BASE_URL + drugstoreList, map, callBack]
    }

    /**区域 */
    fun getArea(up_area_id: String, area_level: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["up_area_id"] = up_area_id
        map!!["area_level"] = area_level
        httpUtil[BASE_URL + area, map, callBack]
    }

    /**查询区域 */
    fun searchArea(up_area_id: String, area_level: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["up_area_id"] = up_area_id
        map!!["area_level"] = area_level
        httpUtil[BASE_URL + searchArea, map, callBack]
    }

    /**卡绑定 */
    fun cardBind(user_id: String,user_name: String,  phone: String,cardNo: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        map!!["user_name"] = user_name
        map!!["cardNo"] = cardNo
        map!!["phone"] = phone
        map!!["cardType"] = "210"
        httpUtil[BASE_URL + cardBind, map, callBack]
    }

    /**实名认证 */
    fun realName(user_id: String, user_name: String, userNo: String, phone: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        map!!["clientName"] = user_name
        map!!["certNo"] = userNo
        map!!["phone"] = phone
        httpUtil[BASE_URL + realName, map, callBack]
    }

    /**实名认证信息*/
    fun smzInfo(user_id: String,callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        httpUtil[BASE_URL + smzInfo, map, callBack]
    }

    /**押金 */
    fun searchYj(user_id: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        httpUtil[BASE_URL + searchYj, map, callBack]
    }
    /**余额 */
    fun searchYuE(user_id: String, cardNo: String, flag: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        map!!["cardNo"] = cardNo
        map!!["flag"] = flag
        httpUtil[BASE_URL + searchYuE, map, callBack]
    }

    /**余额明细查询 */
    fun yuEDetail(user_id: String, month: String, order_page: String, page_size: String,
                  cardNo: String,tr_code: String,is_other: String,nickName: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        map!!["month"] = month
        map!!["order_page"] = order_page
        map!!["page_size"] = page_size
        map!!["cardNo"] = cardNo
        map!!["tr_code"] = tr_code
        map!!["is_other"] = is_other
        map!!["nickName"] = nickName
        httpUtil[BASE_URL + yuEDetail, map, callBack]
    }

    /**押金明细查询 */
    fun cardDeposit(user_id: String,cardNo: String,flag: String, nickName:String,callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        map!!["cardNo"] = cardNo
        map!!["nickName"] = nickName
        map!!["flag"] = flag
        httpUtil[BASE_URL + cardDeposit, map, callBack]
    }

    /**待处理订单 */
    fun order(user_id: String, month: String, order_page: String, page_size: String, tr_code: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        map!!["month"] = month
        map!!["order_page"] = order_page
        map!!["page_size"] = page_size
        map!!["tr_code"] = tr_code
        httpUtil[BASE_URL + order, map, callBack]
    }

    /**额度查询 */
    fun quotaSelect(user_id: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        httpUtil[BASE_URL + quotaSelect, map, callBack]
    }

    /**额度设置 */
    fun quotaSet(user_id: String, amount: String, ceiling: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        map!!["single_consumption_amount"] = amount
        map!!["account_balance_ceiling"] = ceiling
        httpUtil[BASE_URL + quotaSet, map, callBack]
    }

    /**账户冻结解冻*/
    fun accountFrozen(user_id: String, phone: String, type: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        map!!["phone"] = phone
        map!!["data"] = type
        httpUtil[BASE_URL + accountFrozen, map, callBack]
    }

    /**查询账户状态*/
    fun getAccState(user_id: String,callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        httpUtil[BASE_URL + getAccState, map, callBack]
    }

    /**交易参数查询*/
    fun cardParamList(user_id: String,flag: String,cardNo: String,nickName: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        map!!["cardNo"] = cardNo
        map!!["flag"] = flag
        map!!["nickName"] = nickName
        httpUtil[BASE_URL + cardParamList, map, callBack]
    }

    /**设置交易参数*/
    fun setCardParam(user_id: String,m1: String,m2: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        map!!["single_consumption_amount"] = m1
        map!!["account_balance_ceiling"] = m2
        httpUtil[BASE_URL + setCardParam, map, callBack]
    }

    /**社保卡制卡进度查询*/
    fun CardProgress(name: String,num: String, callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["clientName"] = name
        map!!["certNo"] = num
        httpUtil[BASE_URL + CardProgress, map, callBack]
    }

    /**社保卡查询*/
    fun CardInfo(user_id: String,callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        httpUtil[BASE_URL + cardInfo, map, callBack]
    }

    /**社保卡状态查询*/
    fun getCardStatus(user_id: String,user_name: String,cardNo: String,certNo: String,callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        map!!["user_name"] = user_name
        map!!["cardNo"] = cardNo
        map!!["certNo"] = certNo
        map!!["cardType"] = "200"
        httpUtil[BASE_URL + getCardStatus, map, callBack]
    }

    /**实名卡查询*/
    fun smCardList(user_id: String,callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        map!!["order_page"] = "1"
        map!!["page_size"] = "100"
        httpUtil[BASE_URL + smCardList, map, callBack]
    }


    /**非实名卡查询*/
    fun unsmCardList(user_id: String,callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        map!!["cardType"] = "210"
        httpUtil[BASE_URL + unsmCardList, map, callBack]
    }


    /**卡挂失*/
    fun gs(user_id: String,cardNo: String,lssFlag: String,callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        map!!["cardNo"] = cardNo
        map!!["lssFlag"] = lssFlag
        httpUtil[BASE_URL + reportLoss, map, callBack]
    }


    /**卡解挂*/
    fun jg(user_id: String,cardNo: String,callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        map!!["cardNo"] = cardNo
        httpUtil[BASE_URL + unreportLoss, map, callBack]
    }

    /**查询家庭成员*/
    fun linkList(user_id: String,callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        httpUtil[BASE_URL + getmemberLinkList, map, callBack]
    }

    /**家庭管理实名认证*/
    fun smrz(user_id: String,realName:String,nickName: String,certNo: String,phone: String,
             callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        map!!["realName"] = realName
        map!!["certNo"] = certNo
        map!!["nickName"] = nickName
        map!!["phone"] = phone
        map!!["familyserialNum"] = ""
        httpUtil[BASE_URL + smrz, map, callBack]
    }

    /**家庭修改昵称*/
    fun upName(user_id: String,nickName: String,newName: String,flag: String,
               callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        map!!["nickName"] = nickName
        map!!["updateNickName"] = newName
        map!!["flag"] = flag
        map!!["familyserialNum"] = ""
        httpUtil[BASE_URL + updateNickName, map, callBack]
    }

    /**家庭删除昵称*/
    fun deleteName(user_id: String,nickName: String,callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        map!!["nickName"] = nickName
        httpUtil[BASE_URL + deleteNickName, map, callBack]
    }

    /**家庭管理余额查询*/
    fun searchYuEOther(user_id: String,nickName: String,callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        map!!["nickName"] = nickName
        httpUtil[BASE_URL + searchYuEOther, map, callBack]
    }

    /**服务指南分类*/
    fun manageType(callBack: BaseHttpCallBack) {
        map = HashMap()
        httpUtil[BASE_URL + manageType, map, callBack]
    }


    /**设置交易密码 重置交易密码*/
    fun resPwd(user_id: String,pwd: String,callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        map!!["pwd"] = pwd
        httpUtil[BASE_URL + ResPwd, map, callBack]
    }

    /**设置交易密码 重置交易密码*/
    fun fsmResPwd(cardNo: String,pwd: String,callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["cardNo"] = cardNo
        map!!["pwd"] = pwd
        httpUtil[BASE_URL + fsmResPwd, map, callBack]
    }

    /**设置交易密码 重置交易密码*/
    fun deleteCard(user_id: String,cardNo: String,callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        map!!["cardNo"] = cardNo
        map!!["cardType"] = "210"
        httpUtil[BASE_URL + deleteCard, map, callBack]
    }

    /**基础数据接口*/
    fun baseData(param: String,callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["param"] = param
        httpUtil[BASE_URL + baseData, map, callBack]
    }
    /**账户充值 本人 非实名卡*/
    fun acc(user_id: String,cardNo: String,acptType: String,payWay: String,
            amt: String,flag: String,callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        map!!["cardNo"] = cardNo
        map!!["acptType"] = acptType
        map!!["payWay"] = payWay
        map!!["amt"] = amt
        map!!["flag"] = flag
        httpUtil[BASE_URL + acc, map, callBack]
    }
    /**账户充值 家庭管理*/
    fun accOther(user_id: String,acptType: String,payWay: String,amt: String,
                 flag: String,nickName: String,callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["user_id"] = user_id
        map!!["acptType"] = acptType
        map!!["payWay"] = payWay
        map!!["amt"] = amt
        map!!["flag"] = flag
        map!!["nickName"] = nickName
        httpUtil[BASE_URL + accother, map, callBack]
    }

    /**账户充值 家庭管理*/
    fun isSuccess(actionNo: String,callBack: BaseHttpCallBack) {
        map = HashMap()
        map!!["actionNo"] = actionNo
        httpUtil[BASE_URL + isSuccess, map, callBack]
    }

}