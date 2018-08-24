package com.amkj.dmsh.address.bean;

import java.util.List;

/**
 * Created by atd48 on 2016/10/15.
 */
public class AddressInfo {

    /**
     * id : 1
     * name : 中国
     * parent_id : 0
     * type : 1
     * zip :
     */

    private List<AddressBean> area;

    public List<AddressBean> getArea() {
        return area;
    }

    public void setArea(List<AddressBean> area) {
        this.area = area;
    }

    public static class AddressBean {
        private int id;
        private String name;
        private int parent_id;
        private int type;
        private String zip;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getParent_id() {
            return parent_id;
        }

        public void setParent_id(int parent_id) {
            this.parent_id = parent_id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

    }
}
