package edu.cse.foodie;

public class SensorDataObject {
    private String authId;
    private double lat;
    private double lng;
    private double noiceLevel;
    private double lux;
    private boolean proximity;

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getNoiceLevel() {
        return noiceLevel;
    }

    public void setNoiceLevel(double noiceLevel) {
        this.noiceLevel = noiceLevel;
    }

    public double getLux() {
        return lux;
    }

    public void setLux(double lux) {
        this.lux = lux;
    }

    public boolean isProximity() {
        return proximity;
    }

    public void setProximity(boolean proximity) {
        this.proximity = proximity;
    }

    @Override
    public String toString() {
        return String.format("location : (%f, %f), noise : %f, lux: %f, proximity: %b", this.lat, this.lng, this.noiceLevel, this.lux, this.proximity);
    }
}
