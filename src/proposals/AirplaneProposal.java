package proposals;

import java.io.Serializable;

public class AirplaneProposal implements Serializable{

    private int timeToLand; // In seconds
    private int spaceRequiredToLand; // In meters
    private int timeLeftOfFuel; // In seconds
    private int urgency;
    private boolean sos;


    public int getTimeToLand() {
        return timeToLand;
    }

    public int getSpaceRequiredToLand() {
        return spaceRequiredToLand;
    }

    public int getTimeLeftOfFuel() {
        return timeLeftOfFuel;
    }

    public int getUrgency() {
        return urgency;
    }

    public boolean isSos() {
        return sos;
    }

    public AirplaneProposal(int timeToLand, int spaceRequiredToLand, int timeLeftOfFuel, int urgency, boolean sos) {
        this.timeToLand = timeToLand;
        this.spaceRequiredToLand = spaceRequiredToLand;
        this.timeLeftOfFuel = timeLeftOfFuel;
        this.urgency = urgency;
        this.sos = sos;
    }
}
