package agents;

import behaviours.ControlTowerBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import proposals.ControlTowerProposal;
import utils.Runway;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class ControlTowerAgent extends Agent {

    private final LaunchAgents launchAgents = LaunchAgents.getInstance();

    private static Runway runway;

    @Override
    protected void setup() {
        Object[] args = this.getArguments();

        runway = new Runway((int) args[0]);

        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        ArrayList<AgentController> airplaneAgents = this.launchAgents.getAirplaneAgents();

        for (AgentController agent : airplaneAgents) {
            try {
                message.addReceiver(new AID(agent.getName(), AID.ISGUID));
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }

        message.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        message.setReplyByDate((new Date(System.currentTimeMillis() + 10000))); // 10 seconds reply limit

        ControlTowerProposal proposal = new ControlTowerProposal(runway.getLength());

        try {
            message.setContentObject(proposal);
        } catch (IOException e) {
            e.printStackTrace();
        }

        addBehaviour(new ControlTowerBehaviour(this, message));
    }

    public boolean enoughSpace(int spaceRequiredToLand) {
        return runway.getLength() >= spaceRequiredToLand;
    }

    public static void landPlane(int spaceRequired) {
        runway.setLength(runway.getLength() - spaceRequired);
    }

    public static void parkPlane(int spaceRequired) {
        runway.setLength(runway.getLength() + spaceRequired);
    }
}
