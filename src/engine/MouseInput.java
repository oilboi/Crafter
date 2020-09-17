package engine;

import org.joml.Vector2d;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {

    private final Vector2d previousPos;

    private final Vector2d currentPos;

    private final Vector2f displVec;

    private boolean inWindow = false;

    private boolean leftButtonPressed = false;

    private boolean rightButtonPressed = false;

    private boolean mouseLocked = true;

    private float scroll = 0;

    public MouseInput(){
        previousPos = new Vector2d(-1,-1);
        currentPos = new Vector2d(0,0);
        displVec = new Vector2f();
    }

    public void init(Window window){
        glfwSetCursorPosCallback(window.getWindowHandle(), (windowHandle, xpos, ypos) -> {
            currentPos.x = xpos;
            currentPos.y = ypos;
        });

        glfwSetCursorEnterCallback(window.getWindowHandle(), (windowHandle, entered) -> {
            inWindow = entered;
        });

        glfwSetMouseButtonCallback(window.getWindowHandle(), (windowHandle, button, action, mode) -> {
            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
        });

        glfwSetScrollCallback(window.getWindowHandle(), (windowHandle, xOffset, yOffset) -> {
            scroll = (float)yOffset;
        });

    }

    public Vector2f getDisplVec() {
        return displVec;
    }

    public void input(Window window){
        displVec.x = 0;
        displVec.y = 0;

        if (mouseLocked) {
            glfwSetCursorPos(window.getWindowHandle(), window.getWidth() / 2, window.getHeight() / 2);

            if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
                double deltax = currentPos.x - window.getWidth() / 2;//previousPos.x;
                double deltay = currentPos.y - window.getHeight() / 2;//previousPos.y;

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

    public boolean isLeftButtonPressed(){
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed(){
        return rightButtonPressed;
    }

    public void setMouseLocked(boolean lock){
        mouseLocked = lock;
    }

    public boolean isMouseLocked(){
        return mouseLocked;
    }

    public float getScroll(){
        float thisScroll = scroll;
        scroll = 0.0f;
        return thisScroll;
    }
}
