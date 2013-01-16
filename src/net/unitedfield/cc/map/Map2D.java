/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.unitedfield.cc.map;

/**
 *
 * @author jiro
 * 地図タイル表示
 * ToDo:
 * ズームレベルの動的な変更
 * LOD実装
 */

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Future;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;
import com.ning.http.client.AsyncHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.HttpResponseHeaders;
import com.ning.http.client.HttpResponseStatus;
import com.ning.http.client.Response;


public class Map2D extends Node {
    private float earthRadius = 6378150f;
    private float tileWidth = 1.0f;
    private Node node;
    private AsyncHttpClient asyncHttpClient;
    private AssetManager assetManager;
    private GeoCoordinate2D origin;
    private GeoCoordinate2D northWest;
    private GeoCoordinate2D southEast;
    public int zoom;
    private float scale;


    /**
     * Do not use this constructor. Serialization purposes only.
     */
    public Map2D(){
    }

    public Map2D(GeoCoordinate2D origin, GeoCoordinate2D northWest, GeoCoordinate2D southEast, int zoom, float scale, AssetManager assetMgr) {
        this.origin = origin;
        this.northWest = northWest;
        this.southEast = southEast;
        this.zoom = zoom;
        this.scale = scale;
        this.assetManager = assetMgr;
        asyncHttpClient = new AsyncHttpClient();
        node = new Node("Parent");
        updateGeometry();
    }

    public void setZoomLevel(int zoom) {
        if (zoom >= 0 && zoom <= 22) {
            this.zoom = zoom;
            try {
                node.removeFromParent();
                node = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            node = new Node("Parent");
            updateGeometry();
        }
    }

    private void getAndSetMapTile(int x, int y, int zoom, final Material mat) {
        String baseUrl = "http://mt.google.com/vt/lyrs=m&hl=ja&";

        StringBuilder url = new StringBuilder();
        url.append(baseUrl);
        url.append("x=");
        url.append(Integer.toString(x));
        url.append("&y=");
        url.append(Integer.toString(y));
        url.append("&z=");
        url.append(Integer.toString(zoom));

        final String urlString = url.toString();

        StringBuilder assetPath = new StringBuilder();
        //assetPath.append("Textures/Tiles/");
        assetPath.append("mapAssets/Textures/Tiles/");		// set assets directory for input under bin/
        assetPath.append(Integer.toString(zoom));
        assetPath.append("/");
        assetPath.append(Integer.toString(y));

        StringBuilder outputPath = new StringBuilder();
        //outputPath.append("assets/");
        //outputPath.append(assetPath.toString());
        outputPath.append("bin/mapAssets/Textures/Tiles/"); // set assets directory for output under bin/

        File outputPathFile = new File(outputPath.toString());
        outputPathFile.mkdirs();

        StringBuilder outputFileName = new StringBuilder();
        outputFileName.append(Integer.toString(x));
        outputFileName.append(".png");

        final StringBuilder assetFilePath = new StringBuilder();
        assetFilePath.append(assetPath.toString());
        assetFilePath.append("/");
        assetFilePath.append(outputFileName.toString());

        StringBuilder outputFilePath = new StringBuilder();
        outputFilePath.append(outputPath.toString());
        outputFilePath.append("/");
        outputFilePath.append(outputFileName.toString());

        File outputFile = new File(outputFilePath.toString());

        if (outputFile.exists()) {
        	System.out.println(assetFilePath.toString());
            //mat.setTexture("m_ColorMap", assetManager.loadTexture(assetFilePath.toString()));
        	mat.setTexture("ColorMap", assetManager.loadTexture(assetFilePath.toString()));            
        } else {
            try {
                final FileOutputStream outputStream = new FileOutputStream(outputFilePath.toString());                					    
                Future<Response> f = asyncHttpClient.prepareGet(urlString).execute(new AsyncHandler<Response>() {

                    private final Response.ResponseBuilder builder = new Response.ResponseBuilder();

                    public STATE onBodyPartReceived(final HttpResponseBodyPart content) throws Exception {
                        content.writeTo(outputStream);
                        return STATE.CONTINUE;
                    }

                    public STATE onStatusReceived(final HttpResponseStatus status) throws Exception {
                        builder.accumulate(status);
                        return STATE.CONTINUE;
                    }

                    public STATE onHeadersReceived(final HttpResponseHeaders headers) throws Exception {
                        builder.accumulate(headers);
                        return STATE.CONTINUE;
                    }

                    public Response onCompleted() throws Exception {
                        outputStream.close();
                        mat.setTexture("m_ColorMap", assetManager.loadTexture(assetFilePath.toString()));
                        //System.out.println(urlString);
                        return builder.build();
                    }

                    public void onThrowable(Throwable thrwbl) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void updateGeometry() {
        MapTileCoordinate nwXY = northWest.toMapTileCoordinate(zoom);
        int minX = nwXY.x;
        int minZ = nwXY.y;
        MapTileCoordinate seXY = southEast.toMapTileCoordinate(zoom);
        int maxX = seXY.x;
        int maxZ = seXY.y;
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {

                Mesh mesh = new Mesh();

                // Vertex positions in space
                Vector3f[] vertices = new Vector3f[4];
                vertices[0] = new Vector3f(0, 0, 0);
                vertices[1] = new Vector3f(tileWidth, 0, 0);
                vertices[2] = new Vector3f(0, 0, tileWidth);
                vertices[3] = new Vector3f(tileWidth, 0, tileWidth);

                // Texture coordinates
                Vector2f[] texCoord = new Vector2f[4];
                texCoord[0] = new Vector2f(0, 1);
                texCoord[1] = new Vector2f(1, 1);
                texCoord[2] = new Vector2f(0, 0);
                texCoord[3] = new Vector2f(1, 0);

                // Indexes. We define the order in which mesh should be constructed
                int[] indexes = {2, 1, 0, 3, 1, 2};

                // Setting buffers
                mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
                mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
                mesh.setBuffer(Type.Index, 1, BufferUtils.createIntBuffer(indexes));
                mesh.updateBound();

                // Creating a geometry
                Geometry geom = new Geometry("MapMesh", mesh);
                //Material mat = new Material(assetManager, "Common/MatDefs/Misc/SimpleTextured.j3md");
                Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                mat.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));            
                
                getAndSetMapTile(x, z, zoom, mat);

                geom.setMaterial(mat);
                geom.setLocalTranslation((x - minX) * tileWidth, 0, (z - minZ) * tileWidth);
                node.attachChild(geom);
            }
        }

        attachChild(node);

        GeoCoordinate2D nw = nwXY.toGeoCoordinate(zoom);
        seXY.x++;
        seXY.y++;
        GeoCoordinate2D se = seXY.toGeoCoordinate(zoom);
        double meterPerDegreeX = earthRadius * Math.cos(nw.latitude * Math.PI / 180) * 2 * Math.PI / 360;
        double meterPerDegreeZ = earthRadius * 2 * Math.PI / 360;
        double lengthX = (se.longitude - nw.longitude) * meterPerDegreeX;
        double lengthZ = (nw.latitude - se.latitude) * meterPerDegreeZ;
        double scaleX = (lengthX / (tileWidth * (maxX - minX + 1))) * scale;
        double scaleZ = (lengthZ / (tileWidth * (maxZ - minZ + 1))) * scale;
        node.setLocalScale((float) scaleX, 1.0f, (float) scaleZ);

        double offsetX = (origin.longitude - nw.longitude) * meterPerDegreeX * scale;
        double offsetZ = (nw.latitude - origin.latitude) * meterPerDegreeZ * scale;
        node.setLocalTranslation((float) -offsetX, 0.0f, (float) -offsetZ);
    }
}
