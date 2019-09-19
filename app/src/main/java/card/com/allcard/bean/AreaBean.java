package card.com.allcard.bean;

import java.util.List;

public class AreaBean {

    private List<ArealistBean> arealist;

    public List<ArealistBean> getArealist() {
        return arealist;
    }

    public void setArealist(List<ArealistBean> arealist) {
        this.arealist = arealist;
    }

    public static class ArealistBean {
        /**
         * area_name : 河南
         * up_area_id : 1111111111
         * area_id : 4716111147
         * area_level : 2
         */

        private String area_name;
        private String up_area_id;
        private String area_id;
        private String area_level;

        public String getArea_name() {
            return area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }

        public String getUp_area_id() {
            return up_area_id;
        }

        public void setUp_area_id(String up_area_id) {
            this.up_area_id = up_area_id;
        }

        public String getArea_id() {
            return area_id;
        }

        public void setArea_id(String area_id) {
            this.area_id = area_id;
        }

        public String getArea_level() {
            return area_level;
        }

        public void setArea_level(String area_level) {
            this.area_level = area_level;
        }
    }
}
