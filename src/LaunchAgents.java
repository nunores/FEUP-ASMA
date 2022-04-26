import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class LaunchAgents {

    public static void main(String[] args) {
        int airplanes = 3;

        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.GUI, "true");
        ContainerController containerController = runtime.createMainContainer(profile);


        for(int i=1; i<airplanes+1; i++){
            AgentController testAgentController;
            try {
                testAgentController = containerController.createNewAgent("TestAgent" +i , "TestAgent", null);
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }


    }
}
