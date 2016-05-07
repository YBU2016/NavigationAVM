package com.example.models;

/**
 * Created by Toshıba on 8.5.2016.
 */
public class CoordinateViewModel
{
    public String zone = null;
    public int coordinateX = 0;
    public int coordinateY = 0;

    public CoordinateViewModel(String zone, int coordinateX, int coordinateY) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.zone = zone;
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(int coordinateX) {
        this.coordinateX = coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(int coordinateY) {
        this.coordinateY = coordinateY;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
