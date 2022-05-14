import agents.LaunchAgents;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class Main {

    public static void main(String[] args) throws StaleProxyException, InterruptedException {

        LaunchAgents launchAgents = LaunchAgents.getInstance();

        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.GUI, "true");
        ContainerController containerController = runtime.createMainContainer(profile);

        AgentController controlTowerController;

        List<AgentController> airplaneAgents;

        airplaneAgents = launchAgents.getAirplaneAgents();


        for(int i = 0; i < 20; i++){
            /*Random r = new Random();
            int low = 5;
            int high = 11;
            int result = r.nextInt(high-low) + low;*/

            AgentController agentController = containerController.createNewAgent("AirplaneAgent" + i , "agents.AirplaneAgent", launchAgents.createAirplaneArguments());
            airplaneAgents.add(agentController);
            agentController.start();
            //TimeUnit.SECONDS.sleep(result);
        }

        controlTowerController = containerController.createNewAgent("ControlTowerAgent", "agents.ControlTowerAgent", launchAgents.createControlTowerArguments());
        launchAgents.setControlTowerController(controlTowerController);
        controlTowerController.start();



    }

}
