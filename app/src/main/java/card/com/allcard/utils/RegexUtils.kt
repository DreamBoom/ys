package card.com.allcard.utils

import java.util.regex.Pattern

/**
 * 正则表达式工具，用来校验账号和密码等是否符合基本规则
 * @author Created by Dream
 */
object RegexUtils {

    val VERIFY_SUCCESS = 0

    private val VERIFY_LENGTH_ERROR = 1

    private val VERIFY_TYPE_ERROR = 2


    /**
     *
     * @param account 账号
     * @return [.VERIFY_SUCCESS], [.VERIFY_TYPE_ERROR], [.VERIFY_LENGTH_ERROR]
     */
    fun verifyUsername(account: String): Int {
        val length = countLength(account)

        if (length != 11 || length < 1) {
            return VERIFY_LENGTH_ERROR
        }

        val regex = "^1[3|4|5|6|7|8|9]\\d{9}$"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(account)

        return if (!matcher.matches()) VERIFY_TYPE_ERROR else VERIFY_SUCCESS
    }

    /**
     * 长度在6~10之间，只能包含字符、数字和下划线
     *
     * @param password 密码
     * @return [.VERIFY_SUCCESS], [.VERIFY_TYPE_ERROR], [.VERIFY_LENGTH_ERROR]
     */
    fun verifyPassword(password: String): Int {

        val length = password.length
        if (length < 6 || length > 12) {
            return VERIFY_LENGTH_ERROR
        }

        val regex = "^\\w+$"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(password)
        return if (!matcher.matches()) VERIFY_TYPE_ERROR else VERIFY_SUCCESS
    }

    /**
     * 将字符串中所有的非标准字符（双字节字符）替换成两个标准字符（**）。
     * 然后再获取长度。
     */
     fun countLength(string: String): Int {
        var string = string
        string = string.replace("[^\\x00-\\xff]".toRegex(), "**")
        return string.length
    }
    fun isEmail(email:String):Boolean{
        if ("" == email) return false
        val p =  Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")
        val m = p.matcher(email)
        return m.matches()
    }

    /**
     * 身份证号校验
     *
     * @param idCard
     * @return
     */
    fun isNum(idCard: String): Boolean {
        val reg = "^\\d{15}$|^\\d{17}[0-9Xx]$"
        if (!idCard.matches(reg.toRegex())) {
            return false
        }
        return true
    }
}

