package card.com.allcard.bean;

import java.util.List;

public class RegistBean {
    private List<MessageBean> message;
    private List<TokenBean> token;

    public List<MessageBean> getMessage() {
        return message;
    }

    public void setMessage(List<MessageBean> message) {
        this.message = message;
    }

    public List<TokenBean> getToken() {
        return token;
    }

    public void setToken(List<TokenBean> token) {
        this.token = token;
    }

    public static class MessageBean {
        /**
         * message : 用户注册成功！
         * status : 0
         */

        private String message;
        private String status;

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
    }

    public static class TokenBean {
        /**
         * qq : null
         * img : null
         * wechatnum : null
         * user_id : 35
         * phone : 13462439641
         * user_name : 13462439641
         * idCard : null
         * is_auth : 1
         * real_name : null
         */

        private Object qq;
        private Object img;
        private Object wechatnum;
        private String user_id;
        private String phone;
        private String user_name;
        private Object idCard;
        private String is_auth;
        private Object real_name;

        public Object getQq() {
            return qq;
        }

        public void setQq(Object qq) {
            this.qq = qq;
        }

        public Object getImg() {
            return img;
        }

        public void setImg(Object img) {
            this.img = img;
        }

        public Object getWechatnum() {
            return wechatnum;
        }

        public void setWechatnum(Object wechatnum) {
            this.wechatnum = wechatnum;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public Object getIdCard() {
            return idCard;
        }

        public void setIdCard(Object idCard) {
            this.idCard = idCard;
        }

        public String getIs_auth() {
            return is_auth;
        }

        public void setIs_auth(String is_auth) {
            this.is_auth = is_auth;
        }

        public Object getReal_name() {
            return real_name;
        }

        public void setReal_name(Object real_name) {
            this.real_name = real_name;
        }
    }
}
