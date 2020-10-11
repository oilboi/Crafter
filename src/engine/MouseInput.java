package engine;

import org.joml.Vector2d;
import org.joml.Vector2f;

import static engine.Window.*;
import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {

    private static final Vector2d previousPos = new Vector2d(-1,-1);
    private static final Vector2d currentPos  = new Vector2d(0,0);
    private static final Vector2f displVec    = new Vector2f();
    private static boolean  inWindow    = false;
    private static boolean  leftButtonPressed  = false;
    private static boolean  rightButtonPressed = false;
    private static boolean  mouseLocked = true;
    private static float    scroll      = 0;

    public static void resetMousePosVector(){
        glfwSetCursorPos(getWindowHandle(),getWindowWidth() / 2f,getWindowHeight() / 2f );
        currentPos.x = getWindowWidth() / 2;
        currentPos.y = getWindowHeight() / 2;
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
    }

    public static void initMouseInput(){
        glfwSetCursorPosCallback(getWindowHandle(), (windowHandle, xpos, ypos) -> {
            currentPos.x = xpos;
            currentPos.y = ypos;
        });

        glfwSetCursorEnterCallback(getWindowHandle(), (windowHandle, entered) -> {
            inWindow = entered;
        });

        glfwSetMouseButtonCallback(getWindowHandle(), (windowHandle, button, action, mode) -> {
            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
        });

        glfwSetScrollCallback(getWindowHandle(), (windowHandle, xOffset, yOffset) -> {
            scroll = (float)yOffset;
        });
    }

    public static Vector2f getMouseDisplVec() {
        return displVec;
    }

    public static void mouseInput(){

        displVec.x = 0;
        displVec.y = 0;

        if (mouseLocked) {
            glfwSetCursorPos(getWindowHandle(), getWindowWidth() / 2, getWindowHeight() / 2);

            if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
                double deltax = currentPos.x - getWindowWidth() / 2;
                double deltay = currentPos.y - getWindowHeight() / 2;

                boolean rotateX = deltax != 0;
                boolean rotateY = deltay != 0;

                if (rotateX) {
                    displVec.y = (float) deltax;
                }

                if (rotateY) {
                    displVec.x = (float) deltay;
                }
            }
        }

        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
    }

    public static boolean isLeftButtonPressed(){
        return leftButtonPressed;
    }

    public static boolean isRightButtonPressed(){
        return rightButtonPressed;
    }

    public static void setMouseLocked(boolean lock){
        mouseLocked = lock;
    }

    public static boolean isMouseLocked(){
        return mouseLocked;
    }


    private static float thisScroll;

    public static float getMouseScroll(){
        thisScroll = scroll;
        scroll = 0.0f;
        return thisScroll;
    }

    public static Vector2d getMousePos(){
        return currentPos;
    }

    public static void toggleMouseLock(){
        setMouseLocked(!isMouseLocked());

        if(!isMouseLocked()) {
            glfwSetInputMode(getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        } else{
            glfwSetInputMode(getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
        }
        resetMousePosVector();
    }
}
