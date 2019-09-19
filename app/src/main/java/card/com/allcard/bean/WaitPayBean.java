package card.com.allcard.bean;

import java.util.List;

public class WaitPayBean {

    /**
     * payrecordlist : []
     * message : 暂无数据
     * cardsList : []
     * status : 0
     */

    private String message;
    private String status;
    private List<String> payrecordlist;
    private List<String> cardsList;

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

    public List<String> getPayrecordlist() {
        return payrecordlist;
    }

    public void setPayrecordlist(List<String> payrecordlist) {
        this.payrecordlist = payrecordlist;
    }

    public List<String> getCardsList() {
        return cardsList;
    }

    public void setCardsList(List<String> cardsList) {
        this.cardsList = cardsList;
    }
}
