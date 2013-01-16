/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.unitedfield.cc.map;

/**
 *
 * @author jiro
 * 地図タイル用
 */
public class MapTileCoordinate {
    public int x;
    public int y;

    public MapTileCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public MapTileCoordinate(MapTileCoordinate xy) {
        this.x = xy.x;
        this.y = xy.y;
    }

    public GeoCoordinate2D toGeoCoordinate(int zoom) {
        double rd = Math.PI / 180;
        double lng = (this.x * 360) / Math.pow(2, zoom) - 180;
        double tmp = (2 * Math.PI * this.y) / Math.pow(2, zoom) - Math.PI;
        double lat = (Math.PI / 4 - Math.atan2(Math.exp(tmp), 1)) * 2 / rd;
        return new GeoCoordinate2D(lat, lng);
    }
}
