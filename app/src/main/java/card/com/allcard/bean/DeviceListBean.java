package card.com.allcard.bean;

import java.io.Serializable;
import java.util.List;

public class DeviceListBean implements Serializable {

    /**
     * safetyList : [{"deviceApi":"Android 8.1.0","deviceName":"xiaomi","deviceNum":"356695595655247","deviceTime":{"date":30,"day":6,"hours":15,"minutes":5,"month":2,"seconds":45,"time":1553929545000,"timezoneOffset":-480,"year":119},"deviceType":"0","id":385,"phone":"108","remark1":"","remark2":"","remark3":""}]
     * deviceList : [{"deviceApi":"iOS12.0.1","deviceName":"iPhone 6s","deviceNum":"CF6A35F86CFE4CBDBE57E34E47AC90AF","deviceTime":{"date":1,"day":1,"hours":10,"minutes":54,"month":3,"seconds":15,"time":1554087255000,"timezoneOffset":-480,"year":119},"deviceType":"1","id":409,"phone":"108","remark1":"","remark2":"","remark3":""},{"deviceApi":"Android 8.0.0","deviceName":"HONOR","deviceNum":"358598333688244","deviceTime":{"date":1,"day":1,"hours":10,"minutes":50,"month":3,"seconds":4,"time":1554087004000,"timezoneOffset":-480,"year":119},"deviceType":"1","id":408,"phone":"108","remark1":"","remark2":"","remark3":""},{"deviceApi":"Android 8.0.0","deviceName":"HONOR","deviceNum":"358598333688244","deviceTime":{"date":1,"day":1,"hours":10,"minutes":49,"month":3,"seconds":54,"time":1554086994000,"timezoneOffset":-480,"year":119},"deviceType":"1","id":407,"phone":"108","remark1":"","remark2":"","remark3":""},{"deviceApi":"Android 8.0.0","deviceName":"HONOR","deviceNum":"358598333688244","deviceTime":{"date":1,"day":1,"hours":10,"minutes":49,"month":3,"seconds":45,"time":1554086985000,"timezoneOffset":-480,"year":119},"deviceType":"1","id":406,"phone":"108","remark1":"","remark2":"","remark3":""},{"deviceApi":"Android 8.0.0","deviceName":"HONOR","deviceNum":"358598333688244","deviceTime":{"date":1,"day":1,"hours":10,"minutes":49,"month":3,"seconds":35,"time":1554086975000,"timezoneOffset":-480,"year":119},"deviceType":"1","id":405,"phone":"108","remark1":"","remark2":"","remark3":""},{"deviceApi":"Android 8.0.0","deviceName":"HONOR","deviceNum":"358598333688244","deviceTime":{"date":1,"day":1,"hours":10,"minutes":49,"month":3,"seconds":25,"time":1554086965000,"timezoneOffset":-480,"year":119},"deviceType":"1","id":404,"phone":"108","remark1":"","remark2":"","remark3":""},{"deviceApi":"Android 8.0.0","deviceName":"HONOR","deviceNum":"358598333688244","deviceTime":{"date":1,"day":1,"hours":10,"minutes":49,"month":3,"seconds":16,"time":1554086956000,"timezoneOffset":-480,"year":119},"deviceType":"1","id":403,"phone":"108","remark1":"","remark2":"","remark3":""},{"deviceApi":"Android 8.0.0","deviceName":"HONOR","deviceNum":"358598333688244","deviceTime":{"date":1,"day":1,"hours":10,"minutes":49,"month":3,"seconds":7,"time":1554086947000,"timezoneOffset":-480,"year":119},"deviceType":"1","id":402,"phone":"108","remark1":"","remark2":"","remark3":""},{"deviceApi":"Android 8.0.0","deviceName":"HONOR","deviceNum":"358598333688244","deviceTime":{"date":1,"day":1,"hours":10,"minutes":48,"month":3,"seconds":54,"time":1554086934000,"timezoneOffset":-480,"year":119},"deviceType":"1","id":401,"phone":"108","remark1":"","remark2":"","remark3":""},{"deviceApi":"Android 8.0.0","deviceName":"HONOR","deviceNum":"358598333688244","deviceTime":{"date":1,"day":1,"hours":10,"minutes":48,"month":3,"seconds":43,"time":1554086923000,"timezoneOffset":-480,"year":119},"deviceType":"1","id":400,"phone":"108","remark1":"","remark2":"","remark3":""}]
     * message : 成功
     * status : 0
     */

    private String message;
    private String status;
    private List<SafetyList> safetyList;
    private List<DeviceList> deviceList;

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

    public List<SafetyList> getSafetyList() {
        return safetyList;
    }

    public void setSafetyList(List<SafetyList> safetyList) {
        this.safetyList = safetyList;
    }

    public List<DeviceList> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<DeviceList> deviceList) {
        this.deviceList = deviceList;
    }

    public static class SafetyList implements Serializable{
        /**
         * deviceApi : Android 8.1.0
         * deviceName : xiaomi
         * deviceNum : 356695595655247
         * deviceTime : {"date":30,"day":6,"hours":15,"minutes":5,"month":2,"seconds":45,"time":1553929545000,"timezoneOffset":-480,"year":119}
         * deviceType : 0
         * id : 385
         * phone : 108
         * remark1 :
         * remark2 :
         * remark3 :
         */

        private String deviceApi;
        private String deviceName;
        private String deviceNum;
        private DeviceTimeBean deviceTime;
        private String deviceType;
        private int id;
        private String phone;
        private String remark1;
        private String remark2;
        private String remark3;

        public String getDeviceApi() {
            return deviceApi;
        }

        public void setDeviceApi(String deviceApi) {
            this.deviceApi = deviceApi;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getDeviceNum() {
            return deviceNum;
        }

        public void setDeviceNum(String deviceNum) {
            this.deviceNum = deviceNum;
        }

        public DeviceTimeBean getDeviceTime() {
            return deviceTime;
        }

        public void setDeviceTime(DeviceTimeBean deviceTime) {
            this.deviceTime = deviceTime;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRemark1() {
            return remark1;
        }

        public void setRemark1(String remark1) {
            this.remark1 = remark1;
        }

        public String getRemark2() {
            return remark2;
        }

        public void setRemark2(String remark2) {
            this.remark2 = remark2;
        }

        public String getRemark3() {
            return remark3;
        }

        public void setRemark3(String remark3) {
            this.remark3 = remark3;
        }

        public static class DeviceTimeBean implements Serializable{
            /**
             * date : 30
             * day : 6
             * hours : 15
             * minutes : 5
             * month : 2
             * seconds : 45
             * time : 1553929545000
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

    public static class DeviceList implements Serializable{
        /**
         * deviceApi : iOS12.0.1
         * deviceName : iPhone 6s
         * deviceNum : CF6A35F86CFE4CBDBE57E34E47AC90AF
         * deviceTime : {"date":1,"day":1,"hours":10,"minutes":54,"month":3,"seconds":15,"time":1554087255000,"timezoneOffset":-480,"year":119}
         * deviceType : 1
         * id : 409
         * phone : 108
         * remark1 :
         * remark2 :
         * remark3 :
         */

        private String deviceApi;
        private String deviceName;
        private String deviceNum;
        private DeviceTimeBeanX deviceTime;
        private String deviceType;
        private int id;
        private String phone;
        private String remark1;
        private String remark2;
        private String remark3;

        public String getDeviceApi() {
            return deviceApi;
        }

        public void setDeviceApi(String deviceApi) {
            this.deviceApi = deviceApi;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getDeviceNum() {
            return deviceNum;
        }

        public void setDeviceNum(String deviceNum) {
            this.deviceNum = deviceNum;
        }

        public DeviceTimeBeanX getDeviceTime() {
            return deviceTime;
        }

        public void setDeviceTime(DeviceTimeBeanX deviceTime) {
            this.deviceTime = deviceTime;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRemark1() {
            return remark1;
        }

        public void setRemark1(String remark1) {
            this.remark1 = remark1;
        }

        public String getRemark2() {
            return remark2;
        }

        public void setRemark2(String remark2) {
            this.remark2 = remark2;
        }

        public String getRemark3() {
            return remark3;
        }

        public void setRemark3(String remark3) {
            this.remark3 = remark3;
        }

        public static class DeviceTimeBeanX implements Serializable{
            /**
             * date : 1
             * day : 1
             * hours : 10
             * minutes : 54
             * month : 3
             * seconds : 15
             * time : 1554087255000
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
