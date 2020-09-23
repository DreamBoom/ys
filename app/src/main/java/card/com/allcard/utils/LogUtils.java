package card.com.allcard.utils;

import android.util.Log;

public class LogUtils {
    /**
     * 截断输出日志
     * @param msg
     */
    public static void i(String msg) {
        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize ) {// 长度小于等于限制直接打印
            Log.i("======>", msg);
        }else {
            while (msg.length() > segmentSize ) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize );
                msg = msg.replace(logContent, "");
                Log.i("======>", logContent);
            }
            Log.i("======>", msg);// 打印剩余日志
        }
    }
}
