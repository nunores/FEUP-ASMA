package agents;

import behaviours.AirplaneBehaviour;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.StaleProxyException;

import java.util.concurrent.TimeUnit;

public class AirplaneAgent extends Agent {
    public enum FlightType { lowCost, vip, businessClass, _private }

    private int timeToLand; // In seconds
    private int spaceRequiredToLand; // In meters
    private int fuel; // In liters
    private int timeWaiting; // In seconds
    private double fuelPerSecond; // In seconds
    private FlightType flightType;
    private boolean sos;

    private long startTime;

    private ControlTowerAgent controlTower;

    @Override
    public void setup()
    {
        startTime = System.currentTimeMillis();

        Object[] args = this.getArguments();
        timeToLand = (int) args[0];
        spaceRequiredToLand = (int) args[1];
        fuel = (int) args[2];
        timeWaiting = (int) args[3];
        fuelPerSecond = (double) args[4];
        flightType = (FlightType) args[5];
        sos = (boolean) args[6];


        MessageTemplate message = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
                MessageTemplate.MatchPerformative(ACLMessage.CFP)
        );

        try {
            addBehaviour(new AirplaneBehaviour(this, message));
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }

    }

    public double getFuelPerSecond() { return fuelPerSecond; }

    public FlightType getFlightType() { return flightType; }

    public boolean isSos() { return sos; }

    public int getTimeToLand() { return timeToLand; }

    public int getSpaceRequiredToLand() { return spaceRequiredToLand; }

    public int getFuel() { return fuel; }

    public int getTimeWaiting() {
        long endTime = System.currentTimeMillis();
        return (int) TimeUnit.MILLISECONDS.toSeconds( endTime - startTime); }
}

