package card.com.allcard.bean;

public class EduBean {

    /**
     * single_consumption_amount : 1000
     * account_balance_ceiling : 2000000
     * message : 查询成功
     * status : 0
     */

    private String single_consumption_amount;
    private String account_balance_ceiling;
    private String message;
    private String status;

    public String getSingle_consumption_amount() {
        return single_consumption_amount;
    }

    public void setSingle_consumption_amount(String single_consumption_amount) {
        this.single_consumption_amount = single_consumption_amount;
    }

    public String getAccount_balance_ceiling() {
        return account_balance_ceiling;
    }

    public void setAccount_balance_ceiling(String account_balance_ceiling) {
        this.account_balance_ceiling = account_balance_ceiling;
    }

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
