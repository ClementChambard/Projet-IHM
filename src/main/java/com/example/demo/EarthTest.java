package com.example.demo;

import com.interactivemesh.jfx.importer.ImportException;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import java.net.URL;

public class EarthTest extends Application {

    private static final float TEXTURE_LAT_OFFSET = -0.2f;
    private static final float TEXTURE_LON_OFFSET = 2.8f;

    @Override
    public void start(Stage primaryStage) {

        //Create a Pane et graph scene root for the 3D content
        Group root3D = new Group();
        Pane pane3D = new Pane(root3D);

        // Load geometry
        ObjModelImporter objImporter = new ObjModelImporter();
        try {
            URL modeUrl = this.getClass().getResource("earth.obj");
            objImporter.read(modeUrl);
        } catch (ImportException e) {
            System.out.println(e.getMessage());
        }
        MeshView[] meshViews = objImporter.getImport();
        Group earth = new Group(meshViews);

        root3D.getChildren().add(earth);

        // Draw a line

        // Draw a helix

        // Draw city on the earth
        displayTown(root3D, "NYC", 40.639751f, -73.778925f);
        displayTown(root3D, "Brest", 48.390394f, -4.486076f);
        displayTown(root3D, "Marseille", 43.296482f, 5.369782f);
        displayTown(root3D, "Capetown", -33.924966f, 18.423300f);
        displayTown(root3D, "Istanbul", 41.008238f, 28.978359f);
        displayTown(root3D, "Reykjavik", 64.135321f, -21.817439f);
        displayTown(root3D, "Singapore", 1.352083f, 103.819839f);
        displayTown(root3D, "Seoul", 37.566535f, 126.977969f);


        DataReader dr = new DataReader("data.json");

        PhongMaterial redQuad = new PhongMaterial();
        redQuad.setDiffuseColor(new Color(1, 0, 0, 0.01));
        PhongMaterial greenQuad = new PhongMaterial();
        greenQuad.setDiffuseColor(new Color(0,1,0,0.01));
        for (int i = -90; i < 90; i+= 5)
        {
            for (int j = -180; j < 180; j+= 5)
            {
                Point3D tl = geoCoordTo3dCoord(i, j, 1.1f);
                Point3D tr = geoCoordTo3dCoord(i, j+5, 1.1f);
                Point3D bl = geoCoordTo3dCoord(i+5, j, 1.1f);
                Point3D br = geoCoordTo3dCoord(i+5, j+5, 1.1f);
                addQuadrilateral(root3D, tr, tl, bl, br,  ((((Math.abs(i)+Math.abs(j))/5))%2 == 1)?redQuad:greenQuad);
            }
        }


        // Add a camera group
        PerspectiveCamera camera = new PerspectiveCamera(true);
        new CameraManager(camera, pane3D, root3D);

        // Add point light
        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(-180);
        light.setTranslateY(-90);
        light.setTranslateZ(-120);
        light.getScope().addAll(root3D);
        root3D.getChildren().add(light);

        // Add ambient light
        AmbientLight ambientLight = new AmbientLight(Color.WHITE);
        ambientLight.getScope().addAll(root3D);
        root3D.getChildren().add(ambientLight);

        // Create scene
        Scene scene = new Scene(pane3D, 600, 600, true);
        scene.setCamera(camera);
        scene.setFill(Color.gray(0.2));

        Sphere sss = new Sphere(0.03);
        root3D.getChildren().add(sss);
        scene.addEventHandler(MouseEvent.ANY, event -> {
            if (event.getEventType() == MouseEvent.MOUSE_PRESSED && event.isAltDown()) {

                PickResult pickResult = event.getPickResult();
                Point3D spaceCoord = pickResult.getIntersectedPoint();
                // doesn't work for some reasons
                sss.setMaterial(redQuad);
                sss.setTranslateX(spaceCoord.getX());
                sss.setTranslateY(spaceCoord.getY());
                sss.setTranslateZ(spaceCoord.getZ());

                Point2D loc = SpaceCoordToGeoCoord(spaceCoord);
                // get geo-hash
                Location location = new Location("mouse", loc.getX(), loc.getY());
                System.out.println(GeoHashHelper.getGeohash(location));
            }
        });

        //Add the scene to the stage and show it
        primaryStage.setTitle("Earth Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


    // From Rahel Lüthy : https://netzwerg.ch/blog/2015/03/22/javafx-3d-line/
    public Cylinder createLine(Point3D origin, Point3D target) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude();

        Point3D mid = target.midpoint(origin);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Cylinder line = new Cylinder(0.01f, height);

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        return line;
    }

    public static Point3D geoCoordTo3dCoord(float lat, float lon) {
        return geoCoordTo3dCoord(lat, lon, 1);
    }

    public static Point3D geoCoordTo3dCoord(float lat, float lon, float h) {
        float lat_cor = lat + TEXTURE_LAT_OFFSET;
        float lon_cor = lon + TEXTURE_LON_OFFSET;
        return new Point3D(
                h * -java.lang.Math.sin(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)),
                h * -java.lang.Math.sin(java.lang.Math.toRadians(lat_cor)),
                h * java.lang.Math.cos(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)));
    }

    private static final double TEXTURE_OFFSET = 1.01;
    public static Point2D SpaceCoordToGeoCoord(Point3D p) {
        float lat = (float) (Math.asin(-p.getY()/ TEXTURE_OFFSET) * (180/Math.PI) - TEXTURE_LAT_OFFSET);
        float lon = (float)(Math.asin(-p.getX()/(TEXTURE_OFFSET * Math.cos((Math.PI/180) * (lat + TEXTURE_LAT_OFFSET)))) * 180 / Math.PI + TEXTURE_LON_OFFSET);
        if (p.getZ() < 0) lon = 180 - lon;
        return new Point2D(lat, lon);
    }

    public static PhongMaterial materialTown = new PhongMaterial(Color.GREEN);
    public static void displayTown(Group parent, String name, float latitude, float longitude)
    {
        System.out.println("added city" + name);
        Sphere sphere = new Sphere(0.01);
        sphere.setMaterial(materialTown);
        Point3D p = geoCoordTo3dCoord(latitude, longitude);
        sphere.setTranslateX(p.getX());
        sphere.setTranslateY(p.getY());
        sphere.setTranslateZ(p.getZ());
        parent.getChildren().add(sphere);
    }

    private static void addQuadrilateral(Group parent, Point3D topRight, Point3D topLeft, Point3D bottomLeft, Point3D bottomRight, PhongMaterial material)
    {
        final TriangleMesh triangleMesh = new TriangleMesh();

        final float[] points = {
                (float) topRight.getX(), (float) topRight.getY(), (float) topRight.getZ(),
                (float) topLeft.getX(), (float) topLeft.getY(), (float) topLeft.getZ(),
                (float) bottomLeft.getX(), (float) bottomLeft.getY(), (float) bottomLeft.getZ(),
                (float) bottomRight.getX(), (float) bottomRight.getY(), (float) bottomRight.getZ()
        };
        final float[] texCoords = {
                1, 1,
                1, 0,
                0, 1,
                0, 0
        };
        final int[] faces = {
                0, 0, 2, 2, 1, 1,
                0, 0, 3, 3, 2, 2
        };

        triangleMesh.getPoints().setAll(points);
        triangleMesh.getTexCoords().setAll(texCoords);
        triangleMesh.getFaces().setAll(faces);

        final MeshView meshView = new MeshView(triangleMesh);
        meshView.setMaterial(material);
        parent.getChildren().add(meshView);
    }

}
