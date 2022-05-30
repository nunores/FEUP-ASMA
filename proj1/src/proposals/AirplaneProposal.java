package proposals;

import agents.AirplaneAgent;

import java.io.Serializable;

public class AirplaneProposal implements Serializable{

    private final int timeToLand; // In seconds
    private final int spaceRequiredToLand; // In meters
    private final int timeLeftOfFuel; // In seconds
    private final int urgency;
    private final boolean sos;
    private final String name;
    private final int timeWaiting;
    private final AirplaneAgent.FlightType flightType;


    public int getTimeToLand() {
        return timeToLand;
    }

    public int getSpaceRequiredToLand() {
        return spaceRequiredToLand;
    }

    public int getUrgency() {
        return urgency;
    }

    public boolean isSos() {
        return sos;
    }

    public String getName() {
        return name;
    }

    public int getTimeWaiting() { return timeWaiting; }

    public AirplaneAgent.FlightType getFlightType() { return flightType; }

    public AirplaneProposal(int timeToLand, int spaceRequiredToLand, int timeLeftOfFuel, int urgency, boolean sos, String name, int timeWaiting, AirplaneAgent.FlightType flightType) {
        this.timeToLand = timeToLand;
        this.spaceRequiredToLand = spaceRequiredToLand;
        this.timeLeftOfFuel = timeLeftOfFuel;
        this.urgency = urgency;
        this.sos = sos;
        this.name = name;
        this.timeWaiting = timeWaiting;
        this.flightType = flightType;
    }
}
