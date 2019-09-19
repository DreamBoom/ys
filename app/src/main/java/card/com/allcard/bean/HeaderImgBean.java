package card.com.allcard.bean;

import java.util.List;

public class HeaderImgBean {

    private List<ImgurlBean> imgurl;
    private List<MessageBean> message;

    public List<ImgurlBean> getImgurl() {
        return imgurl;
    }

    public void setImgurl(List<ImgurlBean> imgurl) {
        this.imgurl = imgurl;
    }

    public List<MessageBean> getMessage() {
        return message;
    }

    public void setMessage(List<MessageBean> message) {
        this.message = message;
    }

    public static class ImgurlBean {
        /**
         * imgurl : /uploads/20190220/090528269.jpg
         */

        private String imgurl;

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }
    }

    public static class MessageBean {
        /**
         * message : 头像修改成功
         * status : 0
         */

        private String message;
        private String status;

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
}
