package card.com.allcard.bean;

import java.util.List;

public class TabTwoBean {

    /**
     * message : 成功
     * list : {"iconAll":[{"summarydetailList":[{"sumd_name":"临时挂失","is_web":"0","sumd_img":"http://192.168.12.11:8080//uploads/20190509/095957811.png","id":"12","summ_link":"http://192.168.12.10:8080//weixin/reportLoss/cardList.jsp?lssFlag=1","is_login":"0"}],"name":"社保卡管理"},{"summarydetailList":[{"sumd_name":"卡解挂","is_web":"0","sumd_img":"http://192.168.12.11:8080//uploads/20190509/081834730.png","id":"52","summ_link":"http://192.168.12.10:8080/weixin/temUnreportLoss/cardList.jsp","is_login":"0"}],"name":"临时卡管理"},{"summarydetailList":[{"sumd_name":"社保银行卡","is_web":"0","sumd_img":"http://192.168.12.11:8080//uploads/20190509/100440443.png","id":"19","summ_link":"http://192.168.12.10:8080/","is_login":"1"}],"name":"账户管理"},{"summarydetailList":[{"sumd_name":"就医记录","is_web":"0","sumd_img":"http://192.168.12.11:8080//uploads/20190509/165716435.png","id":"25","summ_link":"http://192.168.12.10:8080/weixin/hospital/medicalHospitalRecord.jsp","is_login":"0"}],"name":"医疗信息"},{"summarydetailList":[{"sumd_name":"居民享受待遇","is_web":"0","sumd_img":"http://192.168.12.11:8080//uploads/20190509/102650971.png","id":"42","summ_link":"http://192.168.12.10:8080/","is_login":"1"}],"name":"参保信息"}],"summarydetail":[{"sumd_name":"临时挂失","is_web":"0","sumd_img":"http://192.168.12.11:8080//uploads/20190509/095957811.png","id":"12","summ_link":"http://192.168.12.10:8080//weixin/reportLoss/cardList.jsp?lssFlag=1","is_login":"0"}]}
     * status : 0
     */

    private String message;
    private ListBean list;
    private String result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ListBean getList() {
        return list;
    }

    public void setList(ListBean list) {
        this.list = list;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public static class ListBean {
        private List<IconAllBean> iconAll;
        private List<SummarydetailBean> summarydetail;

        public List<IconAllBean> getIconAll() {
            return iconAll;
        }

        public void setIconAll(List<IconAllBean> iconAll) {
            this.iconAll = iconAll;
        }

        public List<SummarydetailBean> getSummarydetail() {
            return summarydetail;
        }

        public void setSummarydetail(List<SummarydetailBean> summarydetail) {
            this.summarydetail = summarydetail;
        }

        public static class IconAllBean {
            /**
             * summarydetailList : [{"sumd_name":"临时挂失","is_web":"0","sumd_img":"http://192.168.12.11:8080//uploads/20190509/095957811.png","id":"12","summ_link":"http://192.168.12.10:8080//weixin/reportLoss/cardList.jsp?lssFlag=1","is_login":"0"}]
             * name : 社保卡管理
             */

            private String name;
            private List<SummarydetailListBean> summarydetailList;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<SummarydetailListBean> getSummarydetailList() {
                return summarydetailList;
            }

            public void setSummarydetailList(List<SummarydetailListBean> summarydetailList) {
                this.summarydetailList = summarydetailList;
            }

            public static class SummarydetailListBean {
                /**
                 * sumd_name : 临时挂失
                 * is_web : 0
                 * sumd_img : http://192.168.12.11:8080//uploads/20190509/095957811.png
                 * id : 12
                 * summ_link : http://192.168.12.10:8080//weixin/reportLoss/cardList.jsp?lssFlag=1
                 * is_login : 0
                 */

                private String sumd_name;
                private String is_web;
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

                public String getIs_web() {
                    return is_web;
                }

                public void setIs_web(String is_web) {
                    this.is_web = is_web;
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

        public static class SummarydetailBean {
            /**
             * sumd_name : 临时挂失
             * is_web : 0
             * sumd_img : http://192.168.12.11:8080//uploads/20190509/095957811.png
             * id : 12
             * summ_link : http://192.168.12.10:8080//weixin/reportLoss/cardList.jsp?lssFlag=1
             * is_login : 0
             */

            private String sumd_name;
            private String is_web;
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

            public String getIs_web() {
                return is_web;
            }

            public void setIs_web(String is_web) {
                this.is_web = is_web;
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
}
