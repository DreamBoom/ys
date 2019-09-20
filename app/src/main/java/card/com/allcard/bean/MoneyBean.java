package card.com.allcard.bean;

import java.util.List;

public class MoneyBean {

    /**
     * result : 0
     * message : 成功
     * cardsList : [{"trState":"0","accBalBef":"0","trCode":"2835","bizName":"兰考第一医院（集团）","trDate":"2019-09-04 11:22:16","trActionNo":"451666","accKind":"1","accName":"王梦想","amt":"1610","accUsableBal":"-89720543","trCodeName":"临时卡退现"},{"trState":"0","accBalBef":"1610","trCode":"2835","bizName":"兰考第一医院（集团）","trDate":"2019-09-04 11:21:58","trActionNo":"451665","accKind":"1","accName":"王梦想","amt":"390","accUsableBal":"-89720933","trCodeName":"临时卡退现"},{"trState":"0","accBalBef":"2000","trCode":"2150","bizName":"兰考第一医院（集团）","trDate":"2019-09-04 11:21:27","trActionNo":"451664","accKind":"1","accName":"王梦想","amt":"2000","accUsableBal":"0","trCodeName":"联机账户现金充值"},{"trState":"0","accBalBef":"0","trCode":"2835","bizName":"兰考第一医院（集团）","trDate":"2019-09-04 11:21:11","trActionNo":"451663","accKind":"1","accName":"王梦想","amt":"220","accUsableBal":"-89719153","trCodeName":"临时卡退现"},{"trState":"0","accBalBef":"220","trCode":"2835","bizName":"兰考第一医院（集团）","trDate":"2019-09-04 11:20:55","trActionNo":"451662","accKind":"1","accName":"王梦想","amt":"172","accUsableBal":"-89719325","trCodeName":"临时卡退现"},{"trState":"0","accBalBef":"392","trCode":"2835","bizName":"兰考第一医院（集团）","trDate":"2019-09-04 11:20:17","trActionNo":"451661","accKind":"1","accName":"王梦想","amt":"1610","accUsableBal":"-89720935","trCodeName":"临时卡退现"},{"trState":"0","accBalBef":"2002","trCode":"2150","bizName":"兰考第一医院（集团）","trDate":"2019-09-04 11:19:48","trActionNo":"451660","accKind":"1","accName":"王梦想","amt":"2000","accUsableBal":"2","trCodeName":"联机账户现金充值"},{"trState":"0","accBalBef":"2","trCode":"2156","bizName":"兰考县妇幼保健院","trDate":"2019-09-04 09:44:20","trActionNo":"451628","accKind":"1","accName":"王梦想","amt":"1","accUsableBal":"1","trCodeName":"联机账户快e付充值"},{"trState":"0","accBalBef":"1","trCode":"2156","bizName":"兰考第一医院（集团）","trDate":"2019-09-04 09:31:37","trActionNo":"451627","accKind":"1","accName":"王梦想","amt":"1","accUsableBal":"0","trCodeName":"联机账户快e付充值"}]
     */

    private String result;
    private String message;
    private List<CardsListBean> cardsList;

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

    public List<CardsListBean> getCardsList() {
        return cardsList;
    }

    public void setCardsList(List<CardsListBean> cardsList) {
        this.cardsList = cardsList;
    }

    public static class CardsListBean {
        /**
         * trState : 0
         * accBalBef : 0
         * trCode : 2835
         * bizName : 兰考第一医院（集团）
         * trDate : 2019-09-04 11:22:16
         * trActionNo : 451666
         * accKind : 1
         * accName : 王梦想
         * amt : 1610
         * accUsableBal : -89720543
         * trCodeName : 临时卡退现
         */

        private String trState;
        private String accBalBef;
        private String trCode;
        private String bizName;
        private String trDate;
        private String trActionNo;
        private String accKind;
        private String accName;
        private String amt;
        private String accUsableBal;
        private String trCodeName;

        public String getTrState() {
            return trState;
        }

        public void setTrState(String trState) {
            this.trState = trState;
        }

        public String getAccBalBef() {
            return accBalBef;
        }

        public void setAccBalBef(String accBalBef) {
            this.accBalBef = accBalBef;
        }

        public String getTrCode() {
            return trCode;
        }

        public void setTrCode(String trCode) {
            this.trCode = trCode;
        }

        public String getBizName() {
            return bizName;
        }

        public void setBizName(String bizName) {
            this.bizName = bizName;
        }

        public String getTrDate() {
            return trDate;
        }

        public void setTrDate(String trDate) {
            this.trDate = trDate;
        }

        public String getTrActionNo() {
            return trActionNo;
        }

        public void setTrActionNo(String trActionNo) {
            this.trActionNo = trActionNo;
        }

        public String getAccKind() {
            return accKind;
        }

        public void setAccKind(String accKind) {
            this.accKind = accKind;
        }

        public String getAccName() {
            return accName;
        }

        public void setAccName(String accName) {
            this.accName = accName;
        }

        public String getAmt() {
            return amt;
        }

        public void setAmt(String amt) {
            this.amt = amt;
        }

        public String getAccUsableBal() {
            return accUsableBal;
        }

        public void setAccUsableBal(String accUsableBal) {
            this.accUsableBal = accUsableBal;
        }

        public String getTrCodeName() {
            return trCodeName;
        }

        public void setTrCodeName(String trCodeName) {
            this.trCodeName = trCodeName;
        }
    }
}
