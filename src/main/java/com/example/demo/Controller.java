package com.example.demo;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Pane pane3D;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        //Create a Pane et graph scene root for the 3D content
        Group root3D = new Group();

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

        SubScene subScene = new SubScene(root3D, 600, 600, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setFill(Color.GREY);
        pane3D.getChildren().addAll(subScene);

    }
}
