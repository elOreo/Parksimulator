package imageprocessing;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.awt.Color;
import java.util.Arrays;

public class ColorDetection {

    // Die Matrix für das Bild und für die Shapes werden angelegt
    private Mat img;
    private Mat img_shapes;

    //Konstruktor dem der Bildpfad übergeben wird und der dann damit die Detection ausführt.
    public ColorDetection(String imagepath){
        detection(imagepath);
    }
    //Hauptmethode zur Farberkennung
    public void detection(String imagepath) {
        // Das Bild wird geladen und in der Matrix imagemat gespeichert
        Mat imagemat = Imgcodecs.imread(imagepath);

        //for Schleife Pixelabfrage
        for (int x = 0; x < imagemat.cols(); x++) {
            for (int y = 0; y < imagemat.rows(); y++) {
                //nimmt die Farbe des jetzigen Pixels
                double[] color = imagemat.get(y, x);

                //konvertiert RGB zu HSV
                float[] hsv = new float[3];
                Color.RGBtoHSB((int)color[2],(int)color[1],(int)color[0],hsv);
                hsv[0] = 359*hsv[0];

                //überprüft in welchem Farbbereich der Pixel liegt und weist ihm die dazu passende vordefinierte Farbe zu
                if (hsv[2]<0.3) {
                    //Schwarz
                    double[] newcolor = {0.0, 0.0, 0.0};
                    imagemat.put(y, x, newcolor);
                }else if (hsv[1]<0.1) {
                    //Weiß
                    double[] newcolor =  {0.0,255.0,0.0};
                    imagemat.put(y,x,newcolor);
                }else if (hsv[0]>20 && hsv[0]<50) {
                    //Braun
                    double[] newcolor =  {31.0,117.0,156.0};
                    imagemat.put(y,x,newcolor);
                }else if (hsv[0]>49 && hsv[0]<70){
                    //Gelb
                    double[] newcolor =  {31.0,255.0,251.0};
                    imagemat.put(y,x,newcolor);
                }else if (hsv[0]>160 && hsv[0]<260){
                    //Blau
                    double[] newcolor =  {255.0,0.0,0.0};
                    imagemat.put(y,x,newcolor);
                }else if (hsv[0]>340 || hsv[0]<21){
                    //Rot
                    double[] newcolor =  {150.0,150.0,150.0};
                    imagemat.put(y,x,newcolor);
                }else{
                    double[] newcolor =  {252.0,5.0,231.0};
                    imagemat.put(y,x,newcolor);
                }
            }

        }

        //erstellt eine Kopie von imagemat, damit wir es bearbeiten können
        Mat imagemat2 = imagemat.clone();

        // Variable in der immer die vorherige Farbe in der for Schleife gespeichert wird
        double[] lastcolor = {0.0,0.0,0.0};

        // for Schleife geht Pixel für Pixel ab
        for (int x = 0; x < imagemat.cols(); x++) {
            for (int y = 0; y < imagemat.rows(); y++) {
                // speichert aktuelle Farbe
                double[] color = imagemat.get(y, x);

                //überprüft ob Pixel entweder Schwarz(Farbe der Formen) oder rosa(Farbe der Pixel denen keine konkrete Farbe zugewiesen werden konnte) ist
                if ((color[0] == 252.0 && color[1] == 5.0 && color[2] == 231.0) || (color[0] == 0.0 && color[1] == 0.0 && color[2] == 0.0)) {
                    // ändert die Farbe des Pixels zu der des vorherigen
                    imagemat.put(y,x,lastcolor);

                }else {
                    // die vorherige Farbe wird mit der aktuellen überschrieben
                    lastcolor = color;
                }
            }
        }
     
        //das fertig bearbeitete Bild wird in der Matrix "img" gespeichert
        img = imagemat.clone();

        //geht die Kopie des Bildes Pixel für Pixel durch
        for (int x = 0; x < imagemat2.cols(); x++) {
            for (int y = 0; y < imagemat2.rows(); y++) {
                //speichert aktuelle Farbe
                double[] color = imagemat2.get(y, x);

                //falls die Farbe Schwarz ist wird nichts getan
                if (color[0] == 0.0 && color[1] == 0.0 && color[2] == 0.0) {

                }else{
                    //falls die Farbe nicht Schwarz ist wird sie durch Weiß ersetzt
                    double[] newcolor = {255.0, 255.0, 255.0};
                    imagemat2.put(y, x, newcolor);
                }
            }
        }
        //die bearbeitete Kopie wird in "img_shapes" gespeichert
        img_shapes = imagemat2.clone();

    }

    //Methode um das Bild mit passenden Farben und ohne den Formen zu bekommen
    public Mat getImg() {
        return img;
    }

    //Methode um das Bild mit nur den Formen zu bekommen
    public Mat getImg_shapes() {
        return img_shapes;
    }
}
