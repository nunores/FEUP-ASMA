package utils;

import jade.core.AID;

import java.util.ArrayList;
import java.util.List;

public class Runway {
    private List<AID> airplanesOccupying;
    private int length; // In meters

    public Runway(int length){
        this.airplanesOccupying = new ArrayList<AID>();
        this.length = length;
    }

    public int getLength(){
        return length;
    }


}
