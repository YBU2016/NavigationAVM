package com.example.Database.Entities;

/**
 * Created by Alparslan on 24.4.2016.
 */
public class DistancesEntity extends EntityBase
{
    private String Store_ID;
    private String BSSID;
    private String NearZones;
    private int ShortestDistance;
    private int FarthestDistance;

    public DistancesEntity(int id, String store_ID, String bssid, String nearZones,
                           int shortestDistance, int farthestDistance)
    {
        super(id);
        Store_ID = store_ID;
        BSSID = bssid;
        NearZones = nearZones;
        ShortestDistance = shortestDistance;
        FarthestDistance = farthestDistance;
    }

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }

    public int getFarthestDistance() {
        return FarthestDistance;
    }

    public void setFarthestDistance(int farthestDistance) {
        FarthestDistance = farthestDistance;
    }

    public String getNearZones() {
        return NearZones;
    }

    public void setNearZones(String nearZones) {
        NearZones = nearZones;
    }

    public int getShortestDistance() {
        return ShortestDistance;
    }

    public void setShortestDistance(int shortestDistance) {
        ShortestDistance = shortestDistance;
    }

    public String getStore_ID() {
        return Store_ID;
    }

    public void setStore_ID(String store_ID) {
        Store_ID = store_ID;
    }
}
