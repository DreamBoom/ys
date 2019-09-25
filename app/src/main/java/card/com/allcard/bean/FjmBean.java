package card.com.allcard.bean;

import java.util.List;

public class FjmBean {

    /**
     * result : 0
     * cardList : [{"cardNo":"4753002019050103538","cardStateName":"","cardStatus":"0","cardType":210,"certNo":"13603698963","clientName":"陈怀海","createTime":"2019-09-21 10:28:46.0","familyserialNum":"","id":13,"isself":"0","nickName":"","selfSerialNum":"","updateTime":"2019-09-21 10:28:46.0","userId":13}]
     * message : 成功
     */

    private String result;
    private String message;
    private List<CardListBean> cardList;

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

    public List<CardListBean> getCardList() {
        return cardList;
    }

    public void setCardList(List<CardListBean> cardList) {
        this.cardList = cardList;
    }

    public static class CardListBean {
        /**
         * cardNo : 4753002019050103538
         * cardStateName :
         * cardStatus : 0
         * cardType : 210
         * certNo : 13603698963
         * clientName : 陈怀海
         * createTime : 2019-09-21 10:28:46.0
         * familyserialNum :
         * id : 13
         * isself : 0
         * nickName :
         * selfSerialNum :
         * updateTime : 2019-09-21 10:28:46.0
         * userId : 13
         */

        private String cardNo;
        private String cardStateName;
        private String cardStatus;
        private int cardType;
        private String certNo;
        private String clientName;
        private String createTime;
        private String familyserialNum;
        private int id;
        private String isself;
        private String nickName;
        private String selfSerialNum;
        private String updateTime;
        private int userId;

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }

        public String getCardStateName() {
            return cardStateName;
        }

        public void setCardStateName(String cardStateName) {
            this.cardStateName = cardStateName;
        }

        public String getCardStatus() {
            return cardStatus;
        }

        public void setCardStatus(String cardStatus) {
            this.cardStatus = cardStatus;
        }

        public int getCardType() {
            return cardType;
        }

        public void setCardType(int cardType) {
            this.cardType = cardType;
        }

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

        public String getIsself() {
            return isself;
        }

        public void setIsself(String isself) {
            this.isself = isself;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getSelfSerialNum() {
            return selfSerialNum;
        }

        public void setSelfSerialNum(String selfSerialNum) {
            this.selfSerialNum = selfSerialNum;
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
