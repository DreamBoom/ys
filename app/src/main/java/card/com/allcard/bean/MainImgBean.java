package card.com.allcard.bean;

import java.util.List;

public class MainImgBean {

    private List<ServiceguideBean> serviceguide;
    private List<String> image;
    private List<SummarydetailBean> summarydetail;

    public List<ServiceguideBean> getServiceguide() {
        return serviceguide;
    }

    public void setServiceguide(List<ServiceguideBean> serviceguide) {
        this.serviceguide = serviceguide;
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

    public static class ServiceguideBean {
        /**
         * serimg : http://192.168.12.10:8080//uploads/20190505/100332059.jpg
         * in_date : 2019-04-25 14:32:25.0
         * id : 6
         * title : 《复联4》票房创纪录，中影、万达电影的概念股股价遭重挫
         * url : http://192.168.12.10:8080/weixin/wxservicede.jsp
         */

        private String serimg;
        private String in_date;
        private String id;
        private String title;
        private String url;

        public String getSerimg() {
            return serimg;
        }

        public void setSerimg(String serimg) {
            this.serimg = serimg;
        }

        public String getIn_date() {
            return in_date;
        }

        public void setIn_date(String in_date) {
            this.in_date = in_date;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class SummarydetailBean {
        /**
         * sumd_name : 卡状态
         * sumd_img : http://192.168.12.10:8080//uploads/20190128/095932182.png
         * id : 10
         * summ_link : /WeiMemberCard/list?cardType=200&flagType=0
         * is_login : 0
         */

        private String sumd_name;
        private String sumd_img;
        private String id;
        private String summ_link;
        private String is_login;

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
    }
}
