package com.grsu.guideapp.models;

public class XYTileBounds {

    private XYTile northWestXYTile;
    private XYTile southEastXYTile;

    public XYTileBounds(XYTile northWestXYTile, XYTile southEastXYTile) {
        this.northWestXYTile = northWestXYTile;
        this.southEastXYTile = southEastXYTile;
    }

    public XYTile getNorthWestXYTile() {
        return northWestXYTile;
    }

    public XYTile getSouthEastXYTile() {
        return southEastXYTile;
    }
}
