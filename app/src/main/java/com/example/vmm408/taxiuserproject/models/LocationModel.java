package com.example.vmm408.taxiuserproject.models;

public class LocationModel {
    private String idUserLocation;
    private Double latitudeLocation;
    private Double longitudeLocation;

    public void setIdUserLocation(String idUserLocation) {
        this.idUserLocation = idUserLocation;
    }

    public String getIdUserLocation() {
        return idUserLocation;
    }

    public void setLatitudeLocation(Double latitudeLocation) {
        this.latitudeLocation = latitudeLocation;
    }

    public Double getLatitudeLocation() {
        return latitudeLocation;
    }

    public void setLongitudeLocation(Double longitudeLocation) {
        this.longitudeLocation = longitudeLocation;
    }

    public Double getLongitudeLocation() {
        return longitudeLocation;
    }
}
