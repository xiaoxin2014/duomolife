package com.amkj.dmsh.address.bean;

import java.util.List;

public class CityModel {
    private String name;
    private int id;
    private String zip;

    private List<DistrictModel> districtList;

    public CityModel() {
        super();
    }

    public CityModel(String name, int id, List<DistrictModel> districtList) {
        super();
        this.name = name;
        this.districtList = districtList;
        this.id = id;
    }

    public CityModel(String name, int id, String zip, List<DistrictModel> districtList) {
        super();
        this.name = name;
        this.zip = zip;
        this.id = id;
        this.districtList = districtList;
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

    public List<DistrictModel> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<DistrictModel> districtList) {
        this.districtList = districtList;
    }

    @Override
    public String toString() {
        return "CityModel [name=" + name + ", districtList=" + districtList
                + "]";
    }

}
