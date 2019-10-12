package card.com.allcard.bean;

import java.util.List;

public class AnswerBean {

    /**
     * result : 0
     * answerlist : [{"answer":"哈哈","id":25,"inDate":{"date":11,"day":5,"hours":16,"minutes":32,"month":9,"seconds":45,"time":1570782765000,"timezoneOffset":-480,"year":119},"paraId":"5","remark":"你妈妈的名字？","userId":"186"},{"answer":"呵呵","id":26,"inDate":{"date":11,"day":5,"hours":16,"minutes":32,"month":9,"seconds":45,"time":1570782765000,"timezoneOffset":-480,"year":119},"paraId":"6","remark":"你爸爸的名字？","userId":"186"},{"answer":"嘿嘿","id":27,"inDate":{"date":11,"day":5,"hours":16,"minutes":32,"month":9,"seconds":45,"time":1570782765000,"timezoneOffset":-480,"year":119},"paraId":"7","remark":"高中学校的名字？","userId":"186"}]
     * message : 成功
     */

    private String result;
    private String message;
    private List<AnswerlistBean> answerlist;

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

    public List<AnswerlistBean> getAnswerlist() {
        return answerlist;
    }

    public void setAnswerlist(List<AnswerlistBean> answerlist) {
        this.answerlist = answerlist;
    }

    public static class AnswerlistBean {
        /**
         * answer : 哈哈
         * id : 25
         * inDate : {"date":11,"day":5,"hours":16,"minutes":32,"month":9,"seconds":45,"time":1570782765000,"timezoneOffset":-480,"year":119}
         * paraId : 5
         * remark : 你妈妈的名字？
         * userId : 186
         */

        private String answer;
        private int id;
        private InDateBean inDate;
        private String paraId;
        private String remark;
        private String userId;

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public InDateBean getInDate() {
            return inDate;
        }

        public void setInDate(InDateBean inDate) {
            this.inDate = inDate;
        }

        public String getParaId() {
            return paraId;
        }

        public void setParaId(String paraId) {
            this.paraId = paraId;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public static class InDateBean {
            /**
             * date : 11
             * day : 5
             * hours : 16
             * minutes : 32
             * month : 9
             * seconds : 45
             * time : 1570782765000
             * timezoneOffset : -480
             * year : 119
             */

            private int date;
            private int day;
            private int hours;
            private int minutes;
            private int month;
            private int seconds;
            private long time;
            private int timezoneOffset;
            private int year;

            public int getDate() {
                return date;
            }

            public void setDate(int date) {
                this.date = date;
            }

            public int getDay() {
                return day;
            }

            public void setDay(int day) {
                this.day = day;
            }

            public int getHours() {
                return hours;
            }

            public void setHours(int hours) {
                this.hours = hours;
            }

            public int getMinutes() {
                return minutes;
            }

            public void setMinutes(int minutes) {
                this.minutes = minutes;
            }

            public int getMonth() {
                return month;
            }

            public void setMonth(int month) {
                this.month = month;
            }

            public int getSeconds() {
                return seconds;
            }

            public void setSeconds(int seconds) {
                this.seconds = seconds;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public int getTimezoneOffset() {
                return timezoneOffset;
            }

            public void setTimezoneOffset(int timezoneOffset) {
                this.timezoneOffset = timezoneOffset;
            }

            public int getYear() {
                return year;
            }

            public void setYear(int year) {
                this.year = year;
            }
        }
    }
}
