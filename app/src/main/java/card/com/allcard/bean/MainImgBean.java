package card.com.allcard.bean;

import java.util.List;

public class MainImgBean {

    /**
     * result : 0
     * image : ["http://222.138.67.71:19533/uploads/icon/banner@2x.png"]
     * summarydetail : [{"sumd_name":"充值","sumd_img":"http://222.138.67.71:19533//uploads/20190920/ccf69a3f-af3c-4273-9d2f-153f305fe259.png","id":"15","summ_link":"http://222.138.67.71:19534/weixin/accountRecharge/accountRecharge.jsp","is_login":"0"},{"sumd_name":"一卡通余额","sumd_img":"http://222.138.67.71:19533//uploads/20190920/88398a5c-3982-42db-9c94-e81067cb9c6e.png","id":"17","summ_link":"http://222.138.67.71:19534//weixin/cardBalance/cardBalance.jsp","is_login":"0"},{"sumd_name":"医生排班","sumd_img":"http://222.138.67.71:19533//uploads/20190920/80f912c8-c3ab-4f03-a9f6-749d37cbe314.png","id":"23","summ_link":"http://222.138.67.71:19534/weixin/hospital/medicalHospitalRecordyy.jsp","is_login":"1"},{"sumd_name":"门诊缴费","sumd_img":"http://222.138.67.71:19533//uploads/20190920/e052c783-6093-41a0-b91b-b1946a93ae0d.png","id":"57","summ_link":"http://222.138.67.71:19534/null","is_login":"1"},{"sumd_name":"社保卡服务","sumd_img":"http://222.138.67.71:19533//uploads/20190920/028e5979-05de-4bff-a12a-4a36f643896f.png","id":"59","summ_link":"http://222.138.67.71:19534/","is_login":"1"},{"sumd_name":"全部","sumd_img":"http://222.138.67.71:19533/uploads/icon/all.png","id":"","summ_link":"","is_login":"1"}]
     * message : 成功
     */

    private String result;
    private String message;
    private List<String> image;
    private List<SummarydetailBean> summarydetail;

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

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public List<SummarydetailBean> getSummarydetail() {
        return summarydetail;
    }

    public void setSummarydetail(List<SummarydetailBean> summarydetail) {
        this.summarydetail = summarydetail;
    }

    public static class SummarydetailBean {
        /**
         * sumd_name : 充值
         * sumd_img : http://222.138.67.71:19533//uploads/20190920/ccf69a3f-af3c-4273-9d2f-153f305fe259.png
         * id : 15
         * summ_link : http://222.138.67.71:19534/weixin/accountRecharge/accountRecharge.jsp
         * is_login : 0
         */

        private String sumd_name;
        private String sumd_img;
        private String id;
        private String summ_link;
        private String is_login;
        private String is_post;


        public String getSumd_name() {
            return sumd_name;
        }

        public void setSumd_name(String sumd_name) {
            this.sumd_name = sumd_name;
        }

        public String getSumd_img() {
            return sumd_img;
        }

        public void setSumd_img(String sumd_img) {
            this.sumd_img = sumd_img;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSumm_link() {
            return summ_link;
        }

        public void setSumm_link(String summ_link) {
            this.summ_link = summ_link;
        }

        public String getIs_login() {
            return is_login;
        }

        public void setIs_login(String is_login) {
            this.is_login = is_login;
        }

        public String getIs_post() {
            return is_post;
        }

        public void setIs_post(String is_post) {
            this.is_post = is_post;
        }
    }
}
