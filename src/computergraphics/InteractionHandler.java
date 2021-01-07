package computergraphics;
import java.awt.*;
import java.awt.event.*;

/**
 * Java class for handling the keyboard and mouse interaction.
 * Intented to be used for an OpenGL scene renderer.
 * @author Karsten Lehn
 * @version 23.8.2017, 10.9.2017, 22.9.2018
 */

public class InteractionHandler implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener{

    // Constant for debugging purposes
    private static final boolean VERBOSE = false;

    // Variables for camera distance
    private float eyeZ = 10f;
    private float eyeZInc = 0.1f;
    // Variables for scene rotation
    private float angleXaxis = 0f;
    private float angleYaxis = 0f;
    private float angleXaxisInc = 10f;
    private float angleYaxisInc = 10f;
    // Variables for scene translation
    private float xPosition = 0f;
    private float yPosition = 0f;
    private float xPositionInc = 5.0f;
    private float yPositionInc= 5.0f;
    // Variables for keyboard control
    private boolean ctrlKeyPressed = false;
    // Variables for mouse control
    private boolean leftMouseButtonPressed = false;
    private boolean rightMouseButtonPressed = false;
    private Point lastMouseLocation;
    // Taking care of the screen size (mapping of mouse coordinates to angle/translation)
    private final float mouseRotationFactor = 0.01f;
    private final float mouseTranslationFactor = 0.01f;
    private final float mouseWheelScrollFactor = 10f;
    private final float pressLeftKey = 0.3f;

    //Variablen zur Ausrichtung der Kamera
    float x;
    float z;

    private double deltaX = 0;
    private double deltaY = 0;
    /**
     * Standard constructor for creation of the interaction handler.
     */
    public InteractionHandler() {
    }

    public void setEyeZ(float eyeZ) {
        this.eyeZ = eyeZ;
    }

    public float getAngleXaxis() {
        return angleXaxis;
    }

    public float getAngleYaxis() {
        return angleYaxis;
    }

    public float getxPosition() {
        return xPosition;
    }

    public float getyPosition() {
        return yPosition;
    }

    @Override
    /**
     * Implements a method from the interface KeyListener
     * Handles all key input.
     */
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        //Berechnung der Sinus und Cosinus Werte zur Ausrichtung der Kamera
        x = (float) Math.sin(Math.toRadians(angleYaxis))*xPositionInc;
        z = (float) Math.cos(Math.toRadians(angleYaxis))*xPositionInc;
        switch (keyCode) {

            case KeyEvent.VK_W:
                //Kollisionsabfrage
                if (ShapesRendererPP.getRect1().x < ShapesRendererPP.getRect2().x + ShapesRendererPP.getRect2().width &&
                        ShapesRendererPP.getRect1().x + ShapesRendererPP.getRect1().width > ShapesRendererPP.getRect2().x &&
                        ShapesRendererPP.getRect1().y < ShapesRendererPP.getRect2().y + ShapesRendererPP.getRect2().height &&
                        ShapesRendererPP.getRect1().y + ShapesRendererPP.getRect1().height > ShapesRendererPP.getRect2().y) {
                    yPosition += -z*0;
                    xPosition += -x*0;
                    xPosition+= x;
                   }
                else { yPosition += -z;
                    xPosition += -x;}
                break;
            case KeyEvent.VK_A:
                //Kollisionsabfrage
                if (ShapesRendererPP.getRect1().x < ShapesRendererPP.getRect2().x + ShapesRendererPP.getRect2().width &&
                        ShapesRendererPP.getRect1().x + ShapesRendererPP.getRect1().width > ShapesRendererPP.getRect2().x &&
                        ShapesRendererPP.getRect1().y < ShapesRendererPP.getRect2().y + ShapesRendererPP.getRect2().height &&
                        ShapesRendererPP.getRect1().y + ShapesRendererPP.getRect1().height > ShapesRendererPP.getRect2().y) {
                    yPosition += -x*0;
                    xPosition += z*0;
                    xPosition += z;
                }
                else {    yPosition += -x;
                    xPosition += z;}
                break;
            case KeyEvent.VK_S:
                //Kollisionsabfrage
                if (ShapesRendererPP.getRect1().x < ShapesRendererPP.getRect2().x + ShapesRendererPP.getRect2().width &&
                        ShapesRendererPP.getRect1().x + ShapesRendererPP.getRect1().width > ShapesRendererPP.getRect2().x &&
                        ShapesRendererPP.getRect1().y < ShapesRendererPP.getRect2().y + ShapesRendererPP.getRect2().height &&
                        ShapesRendererPP.getRect1().y + ShapesRendererPP.getRect1().height > ShapesRendererPP.getRect2().y) {
                    yPosition += z*0;
                    xPosition += x*0;
                    xPosition += -x;
                }
                else {
                yPosition += z;
                xPosition += x;}
                break;
            case KeyEvent.VK_D:
                //Kollisionsabfrage
                if (ShapesRendererPP.getRect1().x < ShapesRendererPP.getRect2().x + ShapesRendererPP.getRect2().width &&
                        ShapesRendererPP.getRect1().x + ShapesRendererPP.getRect1().width > ShapesRendererPP.getRect2().x &&
                        ShapesRendererPP.getRect1().y < ShapesRendererPP.getRect2().y + ShapesRendererPP.getRect2().height &&
                        ShapesRendererPP.getRect1().y + ShapesRendererPP.getRect1().height > ShapesRendererPP.getRect2().y) {
                    yPosition += -x*0;
                    xPosition += z*0;
                }
                else {
                    yPosition += x;
                    xPosition += -z;
                }
                break;
            case KeyEvent.VK_LEFT:
                //Kollisionsabfrage
                if (ShapesRendererPP.getRect1().x < ShapesRendererPP.getRect2().x + ShapesRendererPP.getRect2().width &&
                        ShapesRendererPP.getRect1().x + ShapesRendererPP.getRect1().width > ShapesRendererPP.getRect2().x &&
                        ShapesRendererPP.getRect1().y < ShapesRendererPP.getRect2().y + ShapesRendererPP.getRect2().height &&
                        ShapesRendererPP.getRect1().y + ShapesRendererPP.getRect1().height > ShapesRendererPP.getRect2().y) {
                    yPosition += -x*0;
                    xPosition += z*0;
                    xPosition += z;
                }
                else {    yPosition += -x;
                    xPosition += z;}
                break;
            case KeyEvent.VK_UP:
                //Kollisionsabfrage
                if (ShapesRendererPP.getRect1().x < ShapesRendererPP.getRect2().x + ShapesRendererPP.getRect2().width &&
                        ShapesRendererPP.getRect1().x + ShapesRendererPP.getRect1().width > ShapesRendererPP.getRect2().x &&
                        ShapesRendererPP.getRect1().y < ShapesRendererPP.getRect2().y + ShapesRendererPP.getRect2().height &&
                        ShapesRendererPP.getRect1().y + ShapesRendererPP.getRect1().height > ShapesRendererPP.getRect2().y) {
                    yPosition += -z*0;
                    xPosition += -x*0;
                    xPosition+= x;
                }
                else { yPosition += -z;
                    xPosition += -x;}
                break;
            case KeyEvent.VK_RIGHT:
                //Kollisionsabfrage
                if (ShapesRendererPP.getRect1().x < ShapesRendererPP.getRect2().x + ShapesRendererPP.getRect2().width &&
                        ShapesRendererPP.getRect1().x + ShapesRendererPP.getRect1().width > ShapesRendererPP.getRect2().x &&
                        ShapesRendererPP.getRect1().y < ShapesRendererPP.getRect2().y + ShapesRendererPP.getRect2().height &&
                        ShapesRendererPP.getRect1().y + ShapesRendererPP.getRect1().height > ShapesRendererPP.getRect2().y) {

                    yPosition += -x*0;
                    xPosition += z*0;
                    xPosition +=-z;
                }
                else {

                    yPosition += x;
                    xPosition += -z;
                }
                break;
            case KeyEvent.VK_DOWN:
                //Kollisionsabfrage
                if (ShapesRendererPP.getRect1().x < ShapesRendererPP.getRect2().x + ShapesRendererPP.getRect2().width &&
                        ShapesRendererPP.getRect1().x + ShapesRendererPP.getRect1().width > ShapesRendererPP.getRect2().x &&
                        ShapesRendererPP.getRect1().y < ShapesRendererPP.getRect2().y + ShapesRendererPP.getRect2().height &&
                        ShapesRendererPP.getRect1().y + ShapesRendererPP.getRect1().height > ShapesRendererPP.getRect2().y) {
                    yPosition += z*0;
                    xPosition += x*0;
                    xPosition += -x;
                }
                else {
                    yPosition += z;
                    xPosition += x;}
                break;
        }
    }

    @Override
    /**
     * Implements one method of the interface KeyListener
     */
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            ctrlKeyPressed = false;
        }
    }

    @Override
    /**
     * Implements one method of the interface KeyListener
     */
    public void keyTyped(KeyEvent e) { }


    @Override
    /**
     * Implements one method of the interface MouseListener
     */
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    /**
     * Implements one method of the interface MouseListener
     */
    public void mousePressed(MouseEvent e) {
        int pressedButton = e.getButton();
        lastMouseLocation = e.getLocationOnScreen();
        if (VERBOSE) {
            System.out.print("Mouse pressed event. ");
            switch (pressedButton) {
                case MouseEvent.BUTTON1:
                    System.out.print("Left mouse button pressed.");
                    break;
                case MouseEvent.BUTTON2:
                    System.out.print("Mouse wheel or middle button pressed.");
                    break;
                case MouseEvent.BUTTON3:
                    System.out.print("Right mouse button pressed.");
                    break;
                case MouseEvent.NOBUTTON:
                    System.out.print(" No button detected.");
                    break;
                default:
                    System.out.print("Unknown button pressed.");
            }
            System.out.println(" At location: " + lastMouseLocation);
        }
        switch (pressedButton) {
            case MouseEvent.BUTTON1:
                leftMouseButtonPressed = true;
                break;
            case MouseEvent.BUTTON3:
                rightMouseButtonPressed = true;
                break;
        }
    }

    @Override
    /**
     * Implements one method of the interface MouseListener
     */
    public void mouseReleased(MouseEvent e) {
        int releasedButton = e.getButton();
        if (VERBOSE) {
            System.out.print("Mouse pressed event. ");
            switch (releasedButton) {
                case MouseEvent.BUTTON1:
                    System.out.println("Left mouse button released.");
                    break;
                case MouseEvent.BUTTON2:
                    System.out.println("Mouse wheel or middle button released.");
                    break;
                case MouseEvent.BUTTON3:
                    System.out.println("Right mouse button released.");
                    break;
                case MouseEvent.NOBUTTON:
                    System.out.println(" No button detected.");
                    break;
                default:
                    System.out.println("Unknow button pressed.");
            }
        }
        switch (releasedButton) {
            case MouseEvent.BUTTON1:
                leftMouseButtonPressed = false;
                break;
            case MouseEvent.BUTTON3:
                rightMouseButtonPressed = false;
                break;
        }
    }

    @Override
    /**
     * Implements one method of the interface MouseListener
     */
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    /**
     * Implements one method of the interface MouseListener
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Implements one method of the interface MouseMotionListener
     */
    @Override
    public void mouseDragged(MouseEvent e) {

        Point currentMouseLocation = e.getLocationOnScreen();
        if (VERBOSE) {
            System.out.print("Mouse dragged event.");
            System.out.println(" At mouse location: " + currentMouseLocation);
        }
        double deltaX = currentMouseLocation.getX() - lastMouseLocation.getX();
        double deltaY = currentMouseLocation.getY() - lastMouseLocation.getY();
        lastMouseLocation = currentMouseLocation;

        angleYaxis += angleYaxisInc * mouseRotationFactor * -deltaX;
        angleXaxis += angleXaxisInc * mouseRotationFactor * -deltaY;

    }

    /**
     * Implements one method of the interface MouseMotionListener
     */
    @Override
    public void mouseMoved(MouseEvent e) {
    }

    /**
     * Implements one method of the interface MouseMWheelMovedListener
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }
}
