package card.com.allcard.bean;

public class AccBean {

    /**
     * result : 0
     * paymentURL : https://pay.abchina.com/ebus/NotCheckStatus/PaymentModeUnionPayAct.ebf?TOKEN=15697162900629808682
     * message : 请求成功
     * actionNo : 453691
     */

    private String result;
    private String paymentURL;
    private String message;
    private String actionNo;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getPaymentURL() {
        return paymentURL;
    }

    public void setPaymentURL(String paymentURL) {
        this.paymentURL = paymentURL;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getActionNo() {
        return actionNo;
    }

    public void setActionNo(String actionNo) {
        this.actionNo = actionNo;
    }
}
