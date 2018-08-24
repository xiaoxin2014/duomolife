package com.amkj.dmsh.address.bean;

public class DistrictModel {
    private String name;
    private int id;
    private String zip;

    public DistrictModel() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "DistrictModel [name=" + name + ", zipcode=" + zip + "]";
    }

}
