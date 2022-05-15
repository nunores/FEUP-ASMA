package utils;

import jade.core.AID;

import java.util.ArrayList;
import java.util.List;

public class Runway {
    private int length; // In space units
    private int originalLength;

    public Runway(int length){
        List<AID> airplanesOccupying = new ArrayList<>();
        this.length = length;
        this.originalLength = length;
    }

    public int getLength(){
        return length;
    }

    public void setLength(int length){
        this.length = length;
    }

    public int getOriginalLength() {
        return this.originalLength;
    }

}
