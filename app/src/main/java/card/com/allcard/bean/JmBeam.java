package card.com.allcard.bean;

import java.util.List;

public class JmBeam {

    /**
     * result : 0
     * message : 成功
     * cardsList : [{"trDate":"2019-09-20","amt":"1","cardState":"1","cardNo":"4753002019050103537"}]
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
         * trDate : 2019-09-20
         * amt : 1
         * cardState : 1
         * cardNo : 4753002019050103537
         */

        private String trDate;
        private String amt;
        private String cardState;
        private String cardNo;

        public String getTrDate() {
            return trDate;
        }

        public void setTrDate(String trDate) {
            this.trDate = trDate;
        }

        public String getAmt() {
            return amt;
        }

        public void setAmt(String amt) {
            this.amt = amt;
        }

        public String getCardState() {
            return cardState;
        }

        public void setCardState(String cardState) {
            this.cardState = cardState;
        }

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }
    }
}
