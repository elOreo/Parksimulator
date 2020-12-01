package structure;

import imageprocessing.ObjectInfo;

import java.util.ArrayList;

public class StructureGenerator {
    public StructureGenerator(ArrayList<ObjectInfo> allShapes, int imgX, int imgY){
        //Generate Flor (x * y) with Colordetection as texture
        // ...........

        //Generate Objects (Out of allShapes Arraylist)

        for(ObjectInfo object : allShapes){
            String objTyp = object.getTyp();
            float objX = object.getxCoordinate();
            float objY = object.getyCoordinate();
            if(objTyp.equals("triangle")){
                // new triangle object (poisition: objX, objY)
            }
            else if(objTyp.equals("rectangle")){
                // new rectangle object (poisition: objX, objY)
            }
            else if(objTyp.equals("pentagon")){
                // new pentagon object (poisition: objX, objY)
            }
            else if(objTyp.equals("hexagon")){
                // new hexagon object (poisition: objX, objY)
            }
            else if(objTyp.equals("star")){
                // new star object (poisition: objX, objY)
            }
            else{
                // new circle object (poisition: objX, objY)
            }
        }
    }
}
