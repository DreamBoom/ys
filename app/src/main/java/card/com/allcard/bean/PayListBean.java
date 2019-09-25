package card.com.allcard.bean;

import java.util.List;

public class PayListBean {

    /**
     * result : 0
     * message : 成功
     * memberlinkList : [{"certNo":"","clientName":"","createTime":"2019-09-23 15:35:12","familyserialNum":"4","id":9,"nickName":"滴","phone":"","remark":"","updateTime":"2019-09-23 15:35:12","userId":14},{"certNo":"","clientName":"","createTime":"2019-09-23 15:34:37","familyserialNum":"4","id":8,"nickName":"啦啦1","phone":"","remark":"","updateTime":"2019-09-23 15:34:37","userId":14},{"certNo":"","clientName":"","createTime":"2019-09-23 15:04:02","familyserialNum":"","id":7,"nickName":"啦啦","phone":"","remark":"","updateTime":"2019-09-23 15:34:32","userId":14}]
     */

    private String result;
    private String message;
    private List<MemberlinkListBean> memberlinkList;

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

    public List<MemberlinkListBean> getMemberlinkList() {
        return memberlinkList;
    }

    public void setMemberlinkList(List<MemberlinkListBean> memberlinkList) {
        this.memberlinkList = memberlinkList;
    }

    public static class MemberlinkListBean {
        /**
         * certNo :
         * clientName :
         * createTime : 2019-09-23 15:35:12
         * familyserialNum : 4
         * id : 9
         * nickName : 滴
         * phone :
         * remark :
         * updateTime : 2019-09-23 15:35:12
         * userId : 14
         */

        private String certNo;
        private String clientName;
        private String createTime;
        private String familyserialNum;
        private int id;
        private String nickName;
        private String phone;
        private String remark;
        private String updateTime;
        private int userId;

        public String getCertNo() {
            return certNo;
        }

        public void setCertNo(String certNo) {
            this.certNo = certNo;
        }

        public String getClientName() {
            return clientName;
        }

        public void setClientName(String clientName) {
            this.clientName = clientName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getFamilyserialNum() {
            return familyserialNum;
        }

        public void setFamilyserialNum(String familyserialNum) {
            this.familyserialNum = familyserialNum;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
