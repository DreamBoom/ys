package card.com.allcard.bean;

public class RealNameBean {

    /**
     * message : 查询成功
     * list : {"birthday":"1992-03-18","name":"王梦想","resideAddr":"河南省焦作市马村区","sex":"1"}
     * status : 0
     */

    private String message;
    private ListBean list;
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ListBean getList() {
        return list;
    }

    public void setList(ListBean list) {
        this.list = list;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class ListBean {
        /**
         * birthday : 1992-03-18
         * name : 王梦想
         * resideAddr : 河南省焦作市马村区
         * sex : 1
         */

        private String birthday;
        private String name;
        private String resideAddr;
        private String sex;
        private String user_cert;
        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getResideAddr() {
            return resideAddr;
        }

        public void setResideAddr(String resideAddr) {
            this.resideAddr = resideAddr;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getUser_cert() {
            return user_cert;
        }

        public void setUser_cert(String user_cert) {
            this.user_cert = user_cert;
        }
    }
}
