package imageprocessing;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import java.util.ArrayList;

public class ShapeDetection {

    //Liste zum speichern aller Formen. Wird an Bildverarbeitung weiter gegeben.
    private ArrayList<ObjectInfo> allShapeInfos = new ArrayList<>();

    //Matrix des Bildes, welches nachher in der GUI ausgegeben wird.
    private Mat img;

    //Einstellungen für CannyEdge Filter.
    private static final int RATIO = 3;
    private static final int KERNEL_SIZE = 3;
    private int lowThresh = 10;

    //Getter für Mat img
    public Mat getImg() {
        return img;
    }

    //Getter für allShapeInfos
    public ArrayList<ObjectInfo> getAllShapeInfos() {
        return allShapeInfos;
    }

    //Konstruktor für ShapeDetection. Erstellt man eine ShapeDetection wird automatisch die Formerkennung auf ein Bild, welches man mit einem Pfad angibt, angewendet.
    public ShapeDetection(boolean useCanny, String imgPath){

        // Es wird zuerst eine Colordetection ausgeführt, damit man ein Bild zurück bekommt, welches nur die Formen beinhaltet
        ColorDetection cd = new ColorDetection(imgPath);
        Mat imgMat = cd.getImg_shapes();


        //Bild in Graustufenbild umwandeln (erleichtert die Formenerkennung)

        Mat imgGrey = new Mat();
        Imgproc.cvtColor(imgMat, imgGrey, Imgproc.COLOR_BGR2GRAY);

        //Für die Hough Circle Detection wird ein Blur über das Bild gelegt. Hier in zwei verschiedenen Versionen.
        Mat greyBlur = new Mat();
        Imgproc.GaussianBlur(imgGrey,greyBlur,new Size(1,1), 0);
        Mat medianGreyBlur = new Mat();
        Imgproc.medianBlur(imgGrey, medianGreyBlur, 1);

        //Matrix circles zum abspeichern der erkannten Kreise.
        Mat circles = new Mat();

        //Houghcircle detection wird hier ausgeführt
        Imgproc.HoughCircles(medianGreyBlur, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                (double)medianGreyBlur.rows()/16, // change this value to detect circles with different distances to each other
                100.0, 30.0, 10, 40); // change the last two parameters
        // (min_radius & max_radius) to detect larger circles

        //Kreise zur Liste mit den Formen hinzufügen
        for (int x = 0; x < circles.cols(); x++) {
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            Imgproc.putText(imgMat, "Circle", center, Imgproc.FONT_HERSHEY_COMPLEX, 0.4, new Scalar(255,0,255));
            allShapeInfos.add(new ObjectInfo("circle", (float) center.x,(float) center.y));

        }


        //Um geringe Grauwertunterscheidungen (welche zu Fehlern führen können) auszuschließen wird hier eine Histogram Equalization ausgeführt.

        Mat eqHisMat = new Mat();
        Imgproc.equalizeHist(imgGrey, eqHisMat);


        //Erstellung des Binärbildes mit threshold Methode.
        Mat imgThresh = new Mat();
        Imgproc.threshold(eqHisMat, imgThresh, 200, 255, Imgproc.THRESH_BINARY);

        //Erstellung des Binärbildes mit cannyEdge Methode.
        Mat cannyEdge = new Mat();
        Imgproc.Canny(greyBlur, cannyEdge, lowThresh, lowThresh* RATIO, KERNEL_SIZE, false);

        //Liste für alle Konturen.
        ArrayList<MatOfPoint> contours = new ArrayList<>();

        Mat hierarchy = new Mat();

        // Im Konstruktor wird festgelegt, ob man CannyEdge oder Threshhold benutzen möchte. Erfahrungsgemäß funktioniert Threshold besser.
        if (useCanny == false) {
            Imgproc.findContours(imgThresh, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        }
        else {
            Imgproc.findContours(cannyEdge, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        }

        //Für Entwicklung zur Überprüfung von Fehlern
        int undefinedCounter = 0;


        //Schleife durch alle Konturen, um herauszufinden um welche Form es sich handelt.
        for(MatOfPoint cnt : contours){
            //Length of Approx = Count of Vertices in one Shape

            MatOfPoint2f approxOutput = new MatOfPoint2f();
            MatOfPoint2f cnt2f = new MatOfPoint2f(cnt.toArray());

            Imgproc.approxPolyDP(cnt2f, approxOutput,0.01*Imgproc.arcLength(cnt2f, true), true);

            float approxOutputLength = approxOutput.total();
            System.out.println(approxOutputLength);

            //Berechnung der Mitte der Formen. Wichtig für die Platzierung in der 3D Welt.
            double xPrepare = 0;
            double yPrepare = 0;

            int rowCounter;

            for(rowCounter = 0; rowCounter < approxOutput.rows(); rowCounter ++){
                Mat rowReturn = approxOutput.row(rowCounter);
                double[] xy = rowReturn.get(0, 0);
                double x = xy[0];
                double y = xy[1];
                xPrepare = xPrepare+x;
                yPrepare = yPrepare+y;
            }
            float descriptionCoordinateX = (float) xPrepare/rowCounter;
            float descriptionCoordinateY = (float) yPrepare/rowCounter;

            //Mittelpunkt der Form
            Point descriptionCoordinates = new Point(descriptionCoordinateX, descriptionCoordinateY);

            //Konturen um das Bild für ein aussagekräfigeres Ausgabebild.
            Imgproc.drawContours(imgMat, contours, -1, new Scalar(255,0,0));

            //Ermittlung der Form anhand der Anzahl an Edges/Vertices. Kreise wurden vorher schon ermittelt. Es war einfacher das zu trennen damit nicht Formen mit vielen Ecken als Kreise erkannt wurden.

                //Dreieck
                if (approxOutputLength == 3){
                    Imgproc.putText(imgMat, "Triangle", descriptionCoordinates, Imgproc.FONT_HERSHEY_COMPLEX, 0.4, new Scalar(255,0,255));
                    allShapeInfos.add(new ObjectInfo("triangle", descriptionCoordinateX, descriptionCoordinateY));
                }
                //Rechteck
                else if (approxOutputLength == 4){
                    Imgproc.putText(imgMat, "Rectangle", descriptionCoordinates, Imgproc.FONT_HERSHEY_COMPLEX, 0.4, new Scalar(255,0,255));
                    allShapeInfos.add(new ObjectInfo("rectangle", descriptionCoordinateX, descriptionCoordinateY));
                }
                //Fünfeck
                else if (approxOutputLength == 5){
                    Imgproc.putText(imgMat, "Pentagon", descriptionCoordinates, Imgproc.FONT_HERSHEY_COMPLEX, 0.4, new Scalar(255,0,255));
                    allShapeInfos.add(new ObjectInfo("pentagon", descriptionCoordinateX, descriptionCoordinateY));
                }
                //Sechseck
                else if (approxOutputLength == 6){
                    Imgproc.putText(imgMat, "Hexagon", descriptionCoordinates, Imgproc.FONT_HERSHEY_COMPLEX, 0.4, new Scalar(255,0,255));
                    allShapeInfos.add(new ObjectInfo("hexagon", descriptionCoordinateX, descriptionCoordinateY));
                }
                //Stern
                else if (approxOutputLength > 6 && approxOutputLength < 11){
                    Imgproc.putText(imgMat, "Star", descriptionCoordinates, Imgproc.FONT_HERSHEY_COMPLEX, 0.4, new Scalar(255,0,255));
                    allShapeInfos.add(new ObjectInfo("star", descriptionCoordinateX, descriptionCoordinateY));
                }
                //Abspeicherung des Endresultates
                img = imgMat;

                //Zur Sicherheit werden hier Objekte die doppelt erkannt wurden aus der Liste gelöscht. Außerdem Sichert dies gegen zu nah beeinander platzierte Objekte ab.
                float xLast = 0;
                float yLast = 0;

                for(int i = 0; i < allShapeInfos.size(); i++){
                    float xCoord = allShapeInfos.get(i).getxCoordinate();
                    float yCoord = allShapeInfos.get(i).getyCoordinate();

                    float xDist = xCoord - xLast;
                    float yDist = yCoord - yLast;

                    if(xDist < 8 && xDist > -8 && yDist < 8 && yDist > -8){
                        allShapeInfos.remove(i);
                    }

                    xLast = xCoord;
                    yLast = yCoord;
                }

                //Das ganze Bild wird immer als Rechteck erkannt. Also löschen wir es einfach aus der Liste.
                for(int i = 0; i < allShapeInfos.size(); i++){
                    if(allShapeInfos.get(i).getTyp().equals("rectangle") && allShapeInfos.get(i).getxCoordinate() > img.cols()/2 -3 && allShapeInfos.get(i).getxCoordinate() < img.cols()/2 +3
                            && allShapeInfos.get(i).getyCoordinate() > img.rows()/2 -3 && allShapeInfos.get(i).getyCoordinate() < img.rows()/2 +3)
                    {
                        allShapeInfos.remove(i);
                    }
                }
        }

        //Konsolenausgabe der Liste + Counter für nicht identifizierte Objekte
        int cirCount = 0;
        int triCount = 0;
        int rectCount = 0;
        int pentCount = 0;
        int hexCount = 0;
        int starCount = 0;
        System.out.println("All Shapes: ");
        for (int x = 0; x < allShapeInfos.size(); x++) {
            System.out.println(allShapeInfos.get(x).getInfo());
            if(allShapeInfos.get(x).getTyp().equals("circle")){
                cirCount++;
            }
            if(allShapeInfos.get(x).getTyp().equals("triangle")){
                triCount++;
            }
            if(allShapeInfos.get(x).getTyp().equals("rectangle")){
                rectCount++;
            }
            if(allShapeInfos.get(x).getTyp().equals("pentagon")){
                pentCount++;
            }
            if(allShapeInfos.get(x).getTyp().equals("hexagon")){
                hexCount++;
            }
            if(allShapeInfos.get(x).getTyp().equals("star")){
                starCount++;
            }
        }
        System.out.println("Undefined shapes: "+ undefinedCounter);
        System.out.println("Trees/Cirles: " + cirCount);
        System.out.println("Bench/Triangle: " + triCount);
        System.out.println("House/Rectangle: " + rectCount);
        System.out.println("Lantern/Pentagon: " + pentCount);
        System.out.println("Trashcan/Hexagon: " + hexCount);
        System.out.println("Windmill/ Star: " + starCount);
    }
    //Methode um zur ShapeInfo Liste hinzuzufügen.
    public ArrayList addToShapeInfo(ObjectInfo objectInfo){
        allShapeInfos.add(objectInfo);
        return allShapeInfos;
    }

}
