package card.com.allcard.bean;

import java.util.List;

public class QuestionBean {

    /**
     * message : 成功
     * status : 0
     * baseList : [{"id":"5","in_date":"2019-02-26 11:31:40.0","is_enable":"0","para_code":"securityquestion","para_key":"你妈妈的名字？","para_name":"密保问题","para_value":"1","user_id":"1"},{"id":"6","in_date":"2019-02-26 11:32:15.0","is_enable":"0","para_code":"securityquestion","para_key":"你爸爸的名字？","para_name":"密保问题","para_value":"2","user_id":"1"},{"id":"7","in_date":"2019-02-26 11:32:15.0","is_enable":"0","para_code":"securityquestion","para_key":"高中学校的名字？","para_name":"密保问题","para_value":"3","user_id":"1"},{"id":"8","in_date":"2019-02-26 11:32:15.0","is_enable":"0","para_code":"securityquestion","para_key":"高中老师的名字？","para_name":"密保问题","para_value":"4","user_id":"1"},{"id":"9","in_date":"2019-02-26 11:32:15.0","is_enable":"0","para_code":"securityquestion","para_key":"初中老师的名字？","para_name":"密保问题","para_value":"5","user_id":"1"}]
     */

    private String message;
    private String status;
    private List<BaseListBean> baseList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BaseListBean> getBaseList() {
        return baseList;
    }

    public void setBaseList(List<BaseListBean> baseList) {
        this.baseList = baseList;
    }

    public static class BaseListBean {
        /**
         * id : 5
         * in_date : 2019-02-26 11:31:40.0
         * is_enable : 0
         * para_code : securityquestion
         * para_key : 你妈妈的名字？
         * para_name : 密保问题
         * para_value : 1
         * user_id : 1
         */

        private String id;
        private String in_date;
        private String is_enable;
        private String para_code;
        private String para_key;
        private String para_name;
        private String para_value;
        private String user_id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIn_date() {
            return in_date;
        }

        public void setIn_date(String in_date) {
            this.in_date = in_date;
        }

        public String getIs_enable() {
            return is_enable;
        }

        public void setIs_enable(String is_enable) {
            this.is_enable = is_enable;
        }

        public String getPara_code() {
            return para_code;
        }

        public void setPara_code(String para_code) {
            this.para_code = para_code;
        }

        public String getPara_key() {
            return para_key;
        }

        public void setPara_key(String para_key) {
            this.para_key = para_key;
        }

        public String getPara_name() {
            return para_name;
        }

        public void setPara_name(String para_name) {
            this.para_name = para_name;
        }

        public String getPara_value() {
            return para_value;
        }

        public void setPara_value(String para_value) {
            this.para_value = para_value;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }
}
