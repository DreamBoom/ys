package card.com.allcard.bean;

import java.util.List;

public class MoneyBean {

    /**
     * message : 成功
     * list : [{"trState":"0","accBalBef":"154","trDate":"2019-05-16 11:34:55","bizName":"运营中心网点","accKind":"5","accName":"刘建民","amt":"1","trCodeName":"联机账户银联充值"},{"trState":"0","accBalBef":"153","trDate":"2019-05-16 10:31:23","bizName":"运营中心网点","accKind":"5","accName":"刘建民","amt":"4","trCodeName":"联机账户银联充值"},{"trState":"0","accBalBef":"149","trDate":"2019-05-16 10:26:32","bizName":"运营中心网点","accKind":"5","accName":"刘建民","amt":"6","trCodeName":"联机账户银联充值"},{"trState":"0","accBalBef":"143","trDate":"2019-05-15 10:42:35","bizName":"运营中心网点","accKind":"5","accName":"刘建民","amt":"9","trCodeName":"联机账户银联充值"},{"trState":"0","accBalBef":"134","trDate":"2019-05-15 10:10:25","bizName":"运营中心网点","accKind":"5","accName":"刘建民","amt":"33","trCodeName":"联机账户银联充值"},{"trState":"0","accBalBef":"101","trDate":"2019-05-15 09:30:47","bizName":"运营中心网点","accKind":"5","accName":"刘建民","amt":"101","trCodeName":"联机账户银联充值"}]
     * status : 0
     */

    private String message;
    private String status;
    private List<ListBean> list;

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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * trState : 0
         * accBalBef : 154
         * trDate : 2019-05-16 11:34:55
         * bizName : 运营中心网点
         * accKind : 5
         * accName : 刘建民
         * amt : 1
         * trCodeName : 联机账户银联充值
         */

        private String trState;
        private String accBalBef;
        private String trDate;
        private String bizName;
        private String accKind;
        private String accName;
        private String amt;
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

        public String getTrDate() {
            return trDate;
        }

        public void setTrDate(String trDate) {
            this.trDate = trDate;
        }

        public String getBizName() {
            return bizName;
        }

        public void setBizName(String bizName) {
            this.bizName = bizName;
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

        public String getTrCodeName() {
            return trCodeName;
        }

        public void setTrCodeName(String trCodeName) {
            this.trCodeName = trCodeName;
        }
    }
}
