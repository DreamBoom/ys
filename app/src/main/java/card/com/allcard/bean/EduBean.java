package card.com.allcard.bean;

import java.util.List;

public class EduBean {

    /**
     * result : 0
     * message : 成功
     * cardsList : [{"upper_limit_recharge":"200","cumulative_withdrawal_cash":"2000","daily_consumption_amount":"5000","in_date":"Wed Jun 26 11:01:23 CST 2019","single_consumption_amount":"1000000","limit_single_withdrawal":"1000","lower_limit_cash_drawing":"100","number_withdrawals":"1000","account_balance_ceiling":"500000","accumulative_recharge_amount":"6000","oper_id":"admin","client_id":"0000000000"},{"upper_limit_recharge":"200","cumulative_withdrawal_cash":"2000","daily_consumption_amount":"5000","in_date":"Wed Jun 26 11:01:23 CST 2019","single_consumption_amount":"1000000","limit_single_withdrawal":"1000","lower_limit_cash_drawing":"100","number_withdrawals":"1000","account_balance_ceiling":"500000","accumulative_recharge_amount":"6000","oper_id":"admin","client_id":"0000000000"}]
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
         * upper_limit_recharge : 200
         * cumulative_withdrawal_cash : 2000
         * daily_consumption_amount : 5000
         * in_date : Wed Jun 26 11:01:23 CST 2019
         * single_consumption_amount : 1000000
         * limit_single_withdrawal : 1000
         * lower_limit_cash_drawing : 100
         * number_withdrawals : 1000
         * account_balance_ceiling : 500000
         * accumulative_recharge_amount : 6000
         * oper_id : admin
         * client_id : 0000000000
         */

        private String upper_limit_recharge;
        private String cumulative_withdrawal_cash;
        private String daily_consumption_amount;
        private String in_date;
        private String single_consumption_amount;
        private String limit_single_withdrawal;
        private String lower_limit_cash_drawing;
        private String number_withdrawals;
        private String account_balance_ceiling;
        private String accumulative_recharge_amount;
        private String oper_id;
        private String client_id;

        public String getUpper_limit_recharge() {
            return upper_limit_recharge;
        }

        public void setUpper_limit_recharge(String upper_limit_recharge) {
            this.upper_limit_recharge = upper_limit_recharge;
        }

        public String getCumulative_withdrawal_cash() {
            return cumulative_withdrawal_cash;
        }

        public void setCumulative_withdrawal_cash(String cumulative_withdrawal_cash) {
            this.cumulative_withdrawal_cash = cumulative_withdrawal_cash;
        }

        public String getDaily_consumption_amount() {
            return daily_consumption_amount;
        }

        public void setDaily_consumption_amount(String daily_consumption_amount) {
            this.daily_consumption_amount = daily_consumption_amount;
        }

        public String getIn_date() {
            return in_date;
        }

        public void setIn_date(String in_date) {
            this.in_date = in_date;
        }

        public String getSingle_consumption_amount() {
            return single_consumption_amount;
        }

        public void setSingle_consumption_amount(String single_consumption_amount) {
            this.single_consumption_amount = single_consumption_amount;
        }

        public String getLimit_single_withdrawal() {
            return limit_single_withdrawal;
        }

        public void setLimit_single_withdrawal(String limit_single_withdrawal) {
            this.limit_single_withdrawal = limit_single_withdrawal;
        }

        public String getLower_limit_cash_drawing() {
            return lower_limit_cash_drawing;
        }

        public void setLower_limit_cash_drawing(String lower_limit_cash_drawing) {
            this.lower_limit_cash_drawing = lower_limit_cash_drawing;
        }

        public String getNumber_withdrawals() {
            return number_withdrawals;
        }

        public void setNumber_withdrawals(String number_withdrawals) {
            this.number_withdrawals = number_withdrawals;
        }

        public String getAccount_balance_ceiling() {
            return account_balance_ceiling;
        }

        public void setAccount_balance_ceiling(String account_balance_ceiling) {
            this.account_balance_ceiling = account_balance_ceiling;
        }

        public String getAccumulative_recharge_amount() {
            return accumulative_recharge_amount;
        }

        public void setAccumulative_recharge_amount(String accumulative_recharge_amount) {
            this.accumulative_recharge_amount = accumulative_recharge_amount;
        }

        public String getOper_id() {
            return oper_id;
        }

        public void setOper_id(String oper_id) {
            this.oper_id = oper_id;
        }

        public String getClient_id() {
            return client_id;
        }

        public void setClient_id(String client_id) {
            this.client_id = client_id;
        }
    }
}
