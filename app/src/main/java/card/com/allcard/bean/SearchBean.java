package card.com.allcard.bean;

import java.util.List;

public class SearchBean {

    /**
     * totalCount : 3
     * data : [{"amount":0.01,"consumeTime":"2020/9/11 17:32:01","balance":979.75,"cardNo":"0000001562","equipment":"000","studentId":"001562"},{"amount":1000,"consumeTime":"2020/6/18 13:57:56","balance":1000,"cardNo":"0000001562","equipment":"000","studentId":"001562"},{"amount":10,"consumeTime":"2020/6/18 13:57:55","balance":0,"cardNo":"0000001562","equipment":"000","studentId":"001562"}]
     * Code : 10000
     * Context : 成功
     */

    private int totalCount;
    private String Code;
    private String Context;
    private List<DataBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getContext() {
        return Context;
    }

    public void setContext(String Context) {
        this.Context = Context;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * amount : 0.01
         * consumeTime : 2020/9/11 17:32:01
         * balance : 979.75
         * cardNo : 0000001562
         * equipment : 000
         * studentId : 001562
         */

        private double amount;
        private String consumeTime;
        private double balance;
        private String cardNo;
        private String equipment;
        private String studentId;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getConsumeTime() {
            return consumeTime;
        }

        public void setConsumeTime(String consumeTime) {
            this.consumeTime = consumeTime;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }

        public String getEquipment() {
            return equipment;
        }

        public void setEquipment(String equipment) {
            this.equipment = equipment;
        }

        public String getStudentId() {
            return studentId;
        }

        public void setStudentId(String studentId) {
            this.studentId = studentId;
        }
    }
}
