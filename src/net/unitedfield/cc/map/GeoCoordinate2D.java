/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.unitedfield.cc.map;

/**
 *
 * @author jiro
 */
public class GeoCoordinate2D {
    public double latitude;
    public double longitude;

    public GeoCoordinate2D(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GeoCoordinate2D(GeoCoordinate2D coordinate) {
        this.latitude = coordinate.latitude;
        this.longitude = coordinate.longitude;
    }

    public MapTileCoordinate toMapTileCoordinate(int zoom) {
        double rd = Math.PI / 180;
        double x = Math.pow(2, zoom) * (180 + this.longitude) / 360;
        double y = Math.pow(2, zoom) * (Math.log(Math.tan(Math.PI / 4 - this.latitude * rd / 2)) + Math.PI) / (2 * Math.PI);
        return new MapTileCoordinate((int) x, (int) y);
    }
}