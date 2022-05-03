import agents.LaunchAgents;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.List;
import java.util.ArrayList;


public class Main {

    public static void main(String[] args) throws StaleProxyException {

        LaunchAgents launchAgents = LaunchAgents.getInstance();

        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.GUI, "true");
        ContainerController containerController = runtime.createMainContainer(profile);

        AgentController airplaneAgentController;

        List<AgentController> airplaneAgents = new ArrayList<AgentController>();

        airplaneAgents = launchAgents.getAirplaneAgents();

        for(int i=1; i < launchAgents.getNAirplanes()+1; i++){
            try {
                airplaneAgents.add(containerController.createNewAgent("AirplaneAgent" + i , "agents.AirplaneAgent", launchAgents.createAirplaneArguments()));
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }

        for(AgentController airplaneAgent : airplaneAgents){
            try {
                airplaneAgent.start();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }

        containerController.createNewAgent("ControlTowerAgent", "agents.ControlTowerAgent", launchAgents.createControlTowerArguments()).start();
    }

}
