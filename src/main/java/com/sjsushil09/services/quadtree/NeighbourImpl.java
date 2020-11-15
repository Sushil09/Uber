package com.sjsushil09.services.quadtree;

public class NeighbourImpl implements Neighbour{

    private final long mId;
    private final double mLatitude;
    private final double mLongitude;

    public NeighbourImpl(long id, double latitude, double longitude) {
        mId = id;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    @Override
    public long getId() {
        return mId;
    }

    @Override
    public double getLatitude() {
        return mLatitude;
    }

    @Override
    public double getLongitude() {
        return mLongitude;
    }
}
