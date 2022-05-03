package agents;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.List;
import java.util.ArrayList;

public class LaunchAgents {

    private int n_airplanes; // DEFAULT: 3
    private ArrayList<AgentController> airplaneAgents = new ArrayList<AgentController>();

    private static LaunchAgents instance = null;

    public LaunchAgents() {
        this.n_airplanes = 3;
    }

    public LaunchAgents(int n_airplanes) {
        this.n_airplanes = n_airplanes;
    }

    public static LaunchAgents getInstance()
    {
        if (instance == null)
            instance = new LaunchAgents();

        return instance;
    }

    public Object[] createAirplaneArguments(){
		List<Object> args = new ArrayList<Object>();

        args.add(10);
        args.add(100);
        args.add(50);
        args.add(0);

        return args.toArray();
    }

    public Object[] createControlTowerArguments() {
        List<Object> args = new ArrayList<Object>();

        args.add(10);

        return args.toArray();
    }

    public ArrayList<AgentController> getAirplaneAgents() {
        return this.airplaneAgents;
    }

    public int getNAirplanes(){
        return this.n_airplanes;
    }

}
