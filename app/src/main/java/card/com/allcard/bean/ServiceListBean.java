package card.com.allcard.bean;

import java.util.List;

public class ServiceListBean {

    /**
     * result : 0
     * img_httpurl : http://222.138.67.71:19533
     * message : 成功
     * list : [{"basedata":{"id":"11","img":"","in_date":"2019-10-10 08:50:15.0","is_enable":"","para_code":"","para_key":"热点问答","para_name":"","para_value":"","user_id":"1"},"content":"","id":"11","in_date":"2019-10-10 08:50:15.0","is_del":"1","serimg":"/uploads/20191010/9c4bdd5f-0a17-466c-982e-8c227b50f04b.jpg","title":"测试05","type":"4","user_id":"1"},{"basedata":{"id":"10","img":"","in_date":"2019-10-10 08:50:02.0","is_enable":"","para_code":"","para_key":"热点问答","para_name":"","para_value":"","user_id":"1"},"content":"","id":"10","in_date":"2019-10-10 08:50:02.0","is_del":"1","serimg":"/uploads/20191010/22024276-e4cf-4438-bd2f-fdf11d87f9fd.jpg","title":"测试04","type":"4","user_id":"1"},{"basedata":{"id":"9","img":"","in_date":"2019-10-10 08:49:36.0","is_enable":"","para_code":"","para_key":"政策法规","para_name":"","para_value":"","user_id":"1"},"content":"","id":"9","in_date":"2019-10-10 08:49:36.0","is_del":"1","serimg":"/uploads/20191010/5ac3d3f7-76b9-4a40-94fe-afe3cfc3da2a.jpg","title":"测试03","type":"3","user_id":"1"}]
     */

    private String result;
    private String img_httpurl;
    private String message;
    private List<ListBean> list;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getImg_httpurl() {
        return img_httpurl;
    }

    public void setImg_httpurl(String img_httpurl) {
        this.img_httpurl = img_httpurl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * basedata : {"id":"11","img":"","in_date":"2019-10-10 08:50:15.0","is_enable":"","para_code":"","para_key":"热点问答","para_name":"","para_value":"","user_id":"1"}
         * content :
         * id : 11
         * in_date : 2019-10-10 08:50:15.0
         * is_del : 1
         * serimg : /uploads/20191010/9c4bdd5f-0a17-466c-982e-8c227b50f04b.jpg
         * title : 测试05
         * type : 4
         * user_id : 1
         */

        private BasedataBean basedata;
        private String content;
        private String id;
        private String in_date;
        private String is_del;
        private String serimg;
        private String title;
        private String type;
        private String user_id;

        public BasedataBean getBasedata() {
            return basedata;
        }

        public void setBasedata(BasedataBean basedata) {
            this.basedata = basedata;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIn_date() {
            return in_date;
        }

        public void setIn_date(String in_date) {
            this.in_date = in_date;
        }

        public String getIs_del() {
            return is_del;
        }

        public void setIs_del(String is_del) {
            this.is_del = is_del;
        }

        public String getSerimg() {
            return serimg;
        }

        public void setSerimg(String serimg) {
            this.serimg = serimg;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public static class BasedataBean {
            /**
             * id : 11
             * img :
             * in_date : 2019-10-10 08:50:15.0
             * is_enable :
             * para_code :
             * para_key : 热点问答
             * para_name :
             * para_value :
             * user_id : 1
             */

            private String id;
            private String img;
            private String in_date;
            private String is_enable;
            private String para_code;
            private String para_key;
            private String para_name;
            private String para_value;
            private String user_id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getIn_date() {
                return in_date;
            }

            public void setIn_date(String in_date) {
                this.in_date = in_date;
            }

            public String getIs_enable() {
                return is_enable;
            }

            public void setIs_enable(String is_enable) {
                this.is_enable = is_enable;
            }

            public String getPara_code() {
                return para_code;
            }

            public void setPara_code(String para_code) {
                this.para_code = para_code;
            }

            public String getPara_key() {
                return para_key;
            }

            public void setPara_key(String para_key) {
                this.para_key = para_key;
            }

            public String getPara_name() {
                return para_name;
            }

            public void setPara_name(String para_name) {
                this.para_name = para_name;
            }

            public String getPara_value() {
                return para_value;
            }

            public void setPara_value(String para_value) {
                this.para_value = para_value;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }
        }
    }
}
