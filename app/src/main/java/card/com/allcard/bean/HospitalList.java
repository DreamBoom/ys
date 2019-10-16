package card.com.allcard.bean;

import java.util.List;

public class HospitalList {

    /**
     * result : 0
     * message : 成功
     * hospital : [{"address":"开封市兰考县城关镇陵园路6号","pass_strength":"114.828948,34.840202","telephone":"0371-26984932","hosp_tran":"开封市兰考县城关镇陵园路6号","id":"1","hosp_web":"http://","hosp_intro":"<p>测试，<\/p><p>测试，<\/p><p>测试，<\/p><p><img src=\"http://222.138.67.71:19533/uploads/ueditor/image/20191015/1571098652795003884.jpg\" title=\"1571098652795003884.jpg\" alt=\"30a58PICH7N.jpg\"/><\/p>","hosp_name":"兰考县中心医院"},{"address":"开封市兰考县城关镇中山北街99号","pass_strength":"114.825929,34.839328","telephone":"0371-26982120","hosp_tran":"(0371)26982120","id":"2","hosp_web":"http://","hosp_intro":"<p>1<\/p>","hosp_name":"兰考第一医院"},{"address":"河南省开封市兰考县考城路与中山北街交叉口 ","pass_strength":"114.828085,34.845535","telephone":"0371-26982120","hosp_tran":"河南省开封市兰考县考城路与中山北街交叉口 ","id":"3","hosp_web":"http://","hosp_intro":null,"hosp_name":"兰考县妇儿医院"},{"address":"开封市兰考县胜利路北段21号","pass_strength":"114.834769,34.834632","telephone":"0371-22241501","hosp_tran":"","id":"4","hosp_web":"http://","hosp_intro":"<p>1<\/p>","hosp_name":"兰考县中医院"},{"address":"河南省开封市兰考县振兴路与兰阳路交汇处向西100米路北 ","pass_strength":"114.8094,34.841387","telephone":"0371-26982120","hosp_tran":"","id":"5","hosp_web":"http://","hosp_intro":null,"hosp_name":"兰考县妇幼保健院"}]
     */

    private String result;
    private String message;
    private List<HospitalBean> hospital;

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

    public List<HospitalBean> getHospital() {
        return hospital;
    }

    public void setHospital(List<HospitalBean> hospital) {
        this.hospital = hospital;
    }

    public static class HospitalBean {
        /**
         * address : 开封市兰考县城关镇陵园路6号
         * pass_strength : 114.828948,34.840202
         * telephone : 0371-26984932
         * hosp_tran : 开封市兰考县城关镇陵园路6号
         * id : 1
         * hosp_web : http://
         * hosp_intro : <p>测试，</p><p>测试，</p><p>测试，</p><p><img src="http://222.138.67.71:19533/uploads/ueditor/image/20191015/1571098652795003884.jpg" title="1571098652795003884.jpg" alt="30a58PICH7N.jpg"/></p>
         * hosp_name : 兰考县中心医院
         */

        private String address;
        private String pass_strength;
        private String telephone;
        private String hosp_tran;
        private String id;
        private String hosp_web;
        private String hosp_intro;
        private String hosp_name;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPass_strength() {
            return pass_strength;
        }

        public void setPass_strength(String pass_strength) {
            this.pass_strength = pass_strength;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getHosp_tran() {
            return hosp_tran;
        }

        public void setHosp_tran(String hosp_tran) {
            this.hosp_tran = hosp_tran;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHosp_web() {
            return hosp_web;
        }

        public void setHosp_web(String hosp_web) {
            this.hosp_web = hosp_web;
        }

        public String getHosp_intro() {
            return hosp_intro;
        }

        public void setHosp_intro(String hosp_intro) {
            this.hosp_intro = hosp_intro;
        }

        public String getHosp_name() {
            return hosp_name;
        }

        public void setHosp_name(String hosp_name) {
            this.hosp_name = hosp_name;
        }
    }
}
