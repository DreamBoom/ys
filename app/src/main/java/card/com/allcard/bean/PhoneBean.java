package card.com.allcard.bean;

import java.util.List;

public class PhoneBean {

    /**
     * result : 0
     * message : 成功
     * list : [{"id":"34","img":"","in_date":"2019-09-23 16:57:41.0","is_enable":"0","para_code":"telPhone","para_key":"1","para_name":"客服电话","para_value":"0391-6688317","user_id":"1"}]
     */

    private String result;
    private String message;
    private List<ListBean> list;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 34
         * img :
         * in_date : 2019-09-23 16:57:41.0
         * is_enable : 0
         * para_code : telPhone
         * para_key : 1
         * para_name : 客服电话
         * para_value : 0391-6688317
         * user_id : 1
         */

        private String id;
        private String img;
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

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
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
