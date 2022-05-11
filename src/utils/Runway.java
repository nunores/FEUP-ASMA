package utils;

import jade.core.AID;

import java.util.ArrayList;
import java.util.List;

public class Runway {
    private int length; // In meters

    public Runway(int length){
        List<AID> airplanesOccupying = new ArrayList<>();
        this.length = length;
    }

    public int getLength(){
        return length;
    }

    public void setLength(int length){
        this.length = length;
    }

}
