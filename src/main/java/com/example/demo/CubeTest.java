package com.example.demo;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class CubeTest extends Application {

    @Override
    public void start(Stage primaryStage) {

        //Create a Pane et graph scene root for the 3D content
        Group root3D = new Group();
        Pane pane3D = new Pane(root3D);

        //Create cube shape
        Box cube = new Box(1, 1, 1);

        //Create Material
        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.BLUE);
        blueMaterial.setSpecularColor(Color.BLUE);
        //Set it to the cube
        cube.setMaterial(blueMaterial);

        //Add the cube to this node
        root3D.getChildren().add(cube);

        //Create cube shape
        Box cube2 = new Box(1, 1, 1);
        cube2.setTranslateX(1.5);
        cube2.setTranslateZ(1.5);

        //Create Material
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.RED);
        redMaterial.setSpecularColor(Color.RED);
        //Set it to the cube
        cube2.setMaterial(redMaterial);

        //Add the cube to this node
        root3D.getChildren().add(cube2);

        //Create cube shape
        Box cube3 = new Box(1, 1, 1);
        cube3.setTranslateY(-1.5);

        //Create Material
        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.GREEN);
        greenMaterial.setSpecularColor(Color.GREEN);
        //Set it to the cube
        cube3.setMaterial(greenMaterial);
        cube3.setRotationAxis(Rotate.Y_AXIS);

        final long startNanoTime = System.nanoTime();
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                double t = (currentNanoTime - startNanoTime) / 1000000000.0;
                cube3.setRotate(t*50);
            }
        }.start();

        //Add the cube to this node
        root3D.getChildren().add(cube3);

        //Add a camera group
        PerspectiveCamera camera = new PerspectiveCamera(true);
        //Group cameraGroup = new Group(camera);
        //root3D.getChildren().add(cameraGroup);

        ////Rotate then move the camera
        //Rotate ry = new Rotate();
        //ry.setAxis(Rotate.Y_AXIS);
        //ry.setAngle(-15);

        //Translate tz = new Translate();
        //tz.setZ(-10);
        //tz.setY(-1);

        //cameraGroup.getTransforms().addAll(ry, tz);

        // Add point light
        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(-180);
        light.setTranslateY(-90);
        light.setTranslateZ(-120);
        light.getScope().addAll(root3D);
        root3D.getChildren().add(light);

        new CameraManager(camera, pane3D, root3D);

        // Create scene
        Scene scene = new Scene(pane3D, 600, 600, true);
        scene.setCamera(camera);
        scene.setFill(Color.GREY);

        //Add the scene to the stage and show it
        primaryStage.setTitle("Cubes Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
