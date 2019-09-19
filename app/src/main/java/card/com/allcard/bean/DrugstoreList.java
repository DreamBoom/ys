package card.com.allcard.bean;

import java.util.ArrayList;

public class DrugstoreList {
    private ArrayList<PharmacyBean> pharmacy;

    public ArrayList<PharmacyBean> getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(ArrayList<PharmacyBean> pharmacy) {
        this.pharmacy = pharmacy;
    }

    public static class PharmacyBean {
        /**
         * pass_strength : 112.567817,35.116629
         * id : 6
         * address : 3
         * telephone : 13203967887
         * phar_name : 3
         */

        private String pass_strength;
        private String id;
        private String address;
        private String telephone;
        private String phar_name;

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

        public String getPhar_name() {
            return phar_name;
        }

        public void setPhar_name(String phar_name) {
            this.phar_name = phar_name;
        }
    }
}
