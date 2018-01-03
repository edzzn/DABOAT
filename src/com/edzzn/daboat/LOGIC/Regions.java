package com.edzzn.daboat.LOGIC;

public class Regions extends ObjetoTabla {

    private Integer region_id;
    private String region_name;

    public Regions(Integer region_id, String region_name) {
        this.setRegion_id(region_id);
        this.setRegion_name(region_name);
    }

    @Override
    public String toString() {
        return getRegion_id() + "\t" + getRegion_name();
    }

    @Override
    public Object getObjectID() {
        return getRegion_id();
    }

    public Integer getRegion_id() {
        return region_id;
    }

    public void setRegion_id(Integer region_id) {
        this.region_id = region_id;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }
}
