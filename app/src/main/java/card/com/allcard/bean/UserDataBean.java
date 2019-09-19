package card.com.allcard.bean;

import java.util.List;

public class UserDataBean {

    /**
     * result : 0
     * message : 查询成功
     * token : [{"phone":"13462439645","idCard":"410804199203180053","is_auth":"0","amt":"0","real_name":"王梦想","isbandcard":"1"}]
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
         * phone : 13462439645
         * idCard : 410804199203180053
         * is_auth : 0
         * amt : 0
         * real_name : 王梦想
         * isbandcard : 1
         */

        private String phone;
        private String idCard;
        private String is_auth;
        private String amt;
        private String real_name;
        private String isbandcard;
        private String img;
        private String frzFlag;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
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

        public String getAmt() {
            return amt;
        }

        public void setAmt(String amt) {
            this.amt = amt;
        }

        public String getReal_name() {
            return real_name;
        }

        public void setReal_name(String real_name) {
            this.real_name = real_name;
        }

        public String getIsbandcard() {
            return isbandcard;
        }

        public void setIsbandcard(String isbandcard) {
            this.isbandcard = isbandcard;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getFrzFlag() {
            return frzFlag;
        }

        public void setFrzFlag(String frzFlag) {
            this.frzFlag = frzFlag;
        }
    }
}
