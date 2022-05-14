package agents;

import jade.core.AID;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static agents.AirplaneAgent.FlightType.lowCost;
import static agents.AirplaneAgent.FlightType._private;
import static agents.AirplaneAgent.FlightType.businessClass;
import static agents.AirplaneAgent.FlightType.vip;

public class LaunchAgents {

    private final int n_airplanes; // DEFAULT: 3
    private final ArrayList<AgentController> airplaneAgents = new ArrayList<>();

    private static LaunchAgents instance = null;

    private AgentController controlTowerController;

    public AgentController getControlTowerController() {
        return controlTowerController;
    }

    public void setControlTowerController(AgentController controlTowerController) {
        this.controlTowerController = controlTowerController;
    }

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

        args.add(generateRandomBetween(6, 12)); // timeToLand
        args.add(generateRandomBetween(1, 8)); // spaceRequiredToLand
        args.add(50); // timeLeftOfFuel
        args.add(0); // timeWaiting
        args.add(0.3); // fuelPerSecond
        args.add(getRandom(new AirplaneAgent.FlightType[]{lowCost, lowCost, lowCost, lowCost, lowCost, _private, _private, businessClass, businessClass, businessClass, vip})); // flightType
        if (generateRandomBetween(1, 20) == 1) {
            args.add(true); // sos
        }
        else {
            args.add(false); // sos
        }

        return args.toArray();
    }

    private int generateRandomBetween(int low, int high){

        Random r = new Random();
        return r.nextInt(high-low) + low;
    }

    private AirplaneAgent.FlightType getRandom(AirplaneAgent.FlightType[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
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

    public void removeFromAirplaneAgents(String agentName) throws StaleProxyException {
        for (int i = 0; i < airplaneAgents.size(); i++) {
            if (airplaneAgents.get(i).getName().equals(agentName)) {
                airplaneAgents.remove(i);
                break;
            }
        }
    }

}
