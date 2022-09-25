package com.ak.covid19tracker;

public class District {
    private String district;
    private String cases;
    public District(){}
    public District(String district,String cases){
        this.district=district;
        this.cases=cases;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }
}
