package card.com.allcard.bean;

import java.util.ArrayList;

public class HospitalList {
    private ArrayList<HospitalBean> hospital;

    public ArrayList<HospitalBean> getHospital() {
        return hospital;
    }

    public void setHospital(ArrayList<HospitalBean> hospital) {
        this.hospital = hospital;
    }

    public static class HospitalBean {
        /**
         * pass_strength : 112.604036,35.109838
         * id : 11
         * address : sdfd
         * telephone : 15936988963
         * hosp_name : sdf
         */

        private String pass_strength;
        private String id;
        private String address;
        private String telephone;
        private String hosp_name;

        public String getPass_strength() {
            return pass_strength;
        }

        public void setPass_strength(String pass_strength) {
            this.pass_strength = pass_strength;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getHosp_name() {
            return hosp_name;
        }

        public void setHosp_name(String hosp_name) {
            this.hosp_name = hosp_name;
        }
    }
}
