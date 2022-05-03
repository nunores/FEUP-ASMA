package proposals;

import java.io.Serializable;

public class AirplaneProposal implements Serializable{

    private int timeToLand; // In seconds
    private int spaceRequiredToLand; // In meters
    private int timeLeftOfFuel; // In seconds
    private int timeWaiting; // In seconds


    public AirplaneProposal(int timeToLand, int spaceRequiredToLand, int timeLeftOfFuel, int timeWaiting) {
        this.timeToLand = timeToLand;
        this.spaceRequiredToLand = spaceRequiredToLand;
        this.timeLeftOfFuel = timeLeftOfFuel;
        this.timeWaiting = timeWaiting;
    }
}
