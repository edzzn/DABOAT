package com.edzzn.daboat.LOGIC;

public class Locations extends ObjetoTabla {
    private Integer location_id;
    private String street_address;
    private String postal_code;
    private String city;
    private String state_province;
    private String country_id;

    public Locations(Integer location_id, String streetAddress, String postalCode, String city, String stateProvince, String country_id) {
        this.setLocation_id(location_id);
        this.setStreet_address(streetAddress);
        this.setPostal_code(postalCode);
        this.setCity(city);
        this.setState_province(stateProvince);
        this.setCountry_id(country_id);
    }

    @Override
    public String toString() {
        return getLocation_id() + "\t" + getStreet_address() + "\t" + getPostal_code() + "\t" + getCity() + "\t" + getState_province() + "\t" + getCountry_id();
    }

    @Override
    public Object getObjectID() {
        return getLocation_id();
    }

    public Integer getLocation_id() {
        return location_id;
    }

    public void setLocation_id(Integer location_id) {
        this.location_id = location_id;
    }

    public String getStreet_address() {
        return street_address;
    }

    public void setStreet_address(String street_address) {
        this.street_address = street_address;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState_province() {
        return state_province;
    }

    public void setState_province(String state_province) {
        this.state_province = state_province;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }
}