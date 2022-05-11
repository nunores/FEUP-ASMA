package agents;

import jade.wrapper.AgentController;

import java.util.ArrayList;
import java.util.List;

import static agents.AirplaneAgent.FlightType.lowCost;

public class LaunchAgents {

    private final int n_airplanes; // DEFAULT: 3
    private final ArrayList<AgentController> airplaneAgents = new ArrayList<>();

    private static LaunchAgents instance = null;

    private LaunchAgents() {
        this.n_airplanes = 3;
    }
    
    public static LaunchAgents getInstance()
    {
        if (instance == null)
            instance = new LaunchAgents();

        return instance;
    }


    public Object[] createAirplaneArguments(){
		List<Object> args = new ArrayList<>();

        args.add(10); // timeToLand
        args.add(6); // spaceRequiredToLand
        args.add(50); // timeLeftOfFuel
        args.add(0); // timeWaiting
        args.add(0.3); // fuelPerSecond
        args.add(lowCost); // flightType
        args.add(false); // sos

        return args.toArray();
    }

    public Object[] createControlTowerArguments() {
        List<Object> args = new ArrayList<>();

        args.add(10); // runWayLength

        return args.toArray();
    }

    public ArrayList<AgentController> getAirplaneAgents() {
        return this.airplaneAgents;
    }

    public int getNAirplanes(){
        return this.n_airplanes;
    }

}
