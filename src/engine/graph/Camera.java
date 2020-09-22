package engine.graph;

import org.joml.Vector3f;

public class Camera {

    private static Vector3f position = new Vector3f();

    private static Vector3f rotation = new Vector3f();

    public static Vector3f getCameraPosition(){
        return position;
    }

    public static void setCameraPosition(float x, float y, float z){
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public static void moveCameraPosition(float offsetX, float offsetY, float offsetZ){
        if ( offsetZ != 0){
            position.x += (float)Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += (float)Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }

        if ( offsetX != 0) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }

        position.y += offsetY;
    }

    public static Vector3f getCameraRotation(){
        return rotation;
    }

    public static void setCameraRotation(float x, float y, float z){
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    public static void moveCameraRotation(float offsetX, float offsetY, float offsetZ){
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.z += offsetZ;
    }

    public static Vector3f getCameraRotationVector(){
        Vector3f rotationVector = new Vector3f();
        float xzLen = (float)Math.cos(Math.toRadians(rotation.x + 180));
        rotationVector.z = xzLen * (float)Math.cos(Math.toRadians(rotation.y));
        rotationVector.y = (float)Math.sin(Math.toRadians(rotation.x + 180));
        rotationVector.x = xzLen * (float)Math.sin(Math.toRadians(-rotation.y));
        return rotationVector;
    }
}
