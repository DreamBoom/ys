package card.com.allcard.bean;

import java.util.List;

public class ServiceTypeBean {

    /**
     * resutl : 0
     * message : 成功
     * list : [{"id":"1","img":"","in_date":"2017-09-26 15:25:05.0","is_enable":"0","para_code":"sergu","para_key":"社保卡办理流程","para_name":"服务指南","para_value":"1","user_id":"1"},{"id":"2","img":"","in_date":"2017-09-26 15:25:05.0","is_enable":"0","para_code":"sergu","para_key":"使用说明","para_name":"服务指南","para_value":"2","user_id":"1"},{"id":"3","img":"","in_date":"2017-09-26 15:25:05.0","is_enable":"0","para_code":"sergu","para_key":"政策法规","para_name":"服务指南","para_value":"3","user_id":"1"},{"id":"4","img":"","in_date":"2017-09-26 15:25:05.0","is_enable":"0","para_code":"sergu","para_key":"热点问答","para_name":"服务指南","para_value":"4","user_id":"1"}]
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
         * id : 1
         * img :
         * in_date : 2017-09-26 15:25:05.0
         * is_enable : 0
         * para_code : sergu
         * para_key : 社保卡办理流程
         * para_name : 服务指南
         * para_value : 1
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
