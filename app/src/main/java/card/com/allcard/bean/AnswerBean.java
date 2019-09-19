package card.com.allcard.bean;

import java.util.List;

public class AnswerBean {
    /**
     * answerlist : [{"answer":"一","id":16,"inDate":{"date":27,"day":3,"hours":15,"minutes":56,"month":1,"seconds":27,"time":1551254187000,"timezoneOffset":-480,"year":119},"paraId":"5","remark":"你妈妈的名字？","userId":"39"},{"answer":"二","id":17,"inDate":{"date":27,"day":3,"hours":15,"minutes":56,"month":1,"seconds":27,"time":1551254187000,"timezoneOffset":-480,"year":119},"paraId":"6","remark":"你爸爸的名字？","userId":"39"},{"answer":"三","id":18,"inDate":{"date":27,"day":3,"hours":15,"minutes":56,"month":1,"seconds":27,"time":1551254187000,"timezoneOffset":-480,"year":119},"paraId":"7","remark":"高中学校的名字？","userId":"39"}]
     * message : 成功
     * status : 0
     */

    private String message;
    private String status;
    private List<AnswerlistBean> answerlist;

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

    public List<AnswerlistBean> getAnswerlist() {
        return answerlist;
    }

    public void setAnswerlist(List<AnswerlistBean> answerlist) {
        this.answerlist = answerlist;
    }

    public static class AnswerlistBean {
        /**
         * answer : 一
         * id : 16
         * inDate : {"date":27,"day":3,"hours":15,"minutes":56,"month":1,"seconds":27,"time":1551254187000,"timezoneOffset":-480,"year":119}
         * paraId : 5
         * remark : 你妈妈的名字？
         * userId : 39
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
             * date : 27
             * day : 3
             * hours : 15
             * minutes : 56
             * month : 1
             * seconds : 27
             * time : 1551254187000
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
