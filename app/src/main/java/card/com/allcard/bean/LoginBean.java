package card.com.allcard.bean;

import java.util.List;

public class LoginBean {

    /**
     * result : 0
     * message : 登录成功
     * token : [{"qq":null,"img":"http://222.138.67.71:19533/uploads/20190515/58d65733-01b2-4681-b19d-47fe2fad2d72.png","user_name":"13462439645","idCard":"410804199203180053","is_auth":"0","real_name":"王梦想","bindsecurityquestion":null,"sign_lock":"0","wechatnum":null,"isbindcard":"0","user_id":"2","phone":"13462439645","sign_lock_num":"","fingerprint":"0","email":null,"isExitDefaultDevice":"0"}]
     */

    private String result;
    private String message;
    private List<TokenBean> token;

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

    public List<TokenBean> getToken() {
        return token;
    }

    public void setToken(List<TokenBean> token) {
        this.token = token;
    }

    public static class TokenBean {
        /**
         * qq : null
         * img : http://222.138.67.71:19533/uploads/20190515/58d65733-01b2-4681-b19d-47fe2fad2d72.png
         * user_name : 13462439645
         * idCard : 410804199203180053
         * is_auth : 0
         * real_name : 王梦想
         * bindsecurityquestion : null
         * sign_lock : 0
         * wechatnum : null
         * isbindcard : 0
         * user_id : 2
         * phone : 13462439645
         * sign_lock_num :
         * fingerprint : 0
         * email : null
         * isExitDefaultDevice : 0
         */

        private String qq;
        private String img;
        private String user_name;
        private String idCard;
        private String is_auth;
        private String real_name;
        private String bindsecurityquestion;
        private String sign_lock;
        private String wechatnum;
        private String isbindcard;
        private String user_id;
        private String phone;
        private String sign_lock_num;
        private String fingerprint;
        private String email;
        private String isExitDefaultDevice;

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getBindsecurityquestion() {
            return bindsecurityquestion;
        }

        public void setBindsecurityquestion(String bindsecurityquestion) {
            this.bindsecurityquestion = bindsecurityquestion;
        }

        public String getWechatnum() {
            return wechatnum;
        }

        public void setWechatnum(String wechatnum) {
            this.wechatnum = wechatnum;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getIs_auth() {
            return is_auth;
        }

        public void setIs_auth(String is_auth) {
            this.is_auth = is_auth;
        }

        public String getReal_name() {
            return real_name;
        }

        public void setReal_name(String real_name) {
            this.real_name = real_name;
        }


        public String getSign_lock() {
            return sign_lock;
        }

        public void setSign_lock(String sign_lock) {
            this.sign_lock = sign_lock;
        }


        public String getIsbindcard() {
            return isbindcard;
        }

        public void setIsbindcard(String isbindcard) {
            this.isbindcard = isbindcard;
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

        public String getSign_lock_num() {
            return sign_lock_num;
        }

        public void setSign_lock_num(String sign_lock_num) {
            this.sign_lock_num = sign_lock_num;
        }

        public String getFingerprint() {
            return fingerprint;
        }

        public void setFingerprint(String fingerprint) {
            this.fingerprint = fingerprint;
        }


        public String getIsExitDefaultDevice() {
            return isExitDefaultDevice;
        }

        public void setIsExitDefaultDevice(String isExitDefaultDevice) {
            this.isExitDefaultDevice = isExitDefaultDevice;
        }
    }
}
