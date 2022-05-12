package behaviours;

import agents.AirplaneAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetInitiator;

import agents.ControlTowerAgent;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import proposals.AirplaneProposal;
import proposals.ControlTowerProposal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class ControlTowerBehaviour extends ContractNetInitiator {

    private final ControlTowerAgent controlTowerAgent;

    public ControlTowerBehaviour(Agent a, ACLMessage cfp) {
        super(a, cfp);
        this.controlTowerAgent = (ControlTowerAgent) a;
    }

    @Override
    protected void handleInform(ACLMessage inform) {


        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        ArrayList<AgentController> airplaneAgents = controlTowerAgent.getLaunchAgents().getAirplaneAgents();

        for (AgentController agent : airplaneAgents) {
            try {
                message.addReceiver(new AID(agent.getName(), AID.ISGUID));
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }

        message.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        message.setReplyByDate((new Date(System.currentTimeMillis() + 10000))); // 10 seconds reply limit

        ControlTowerProposal proposal = new ControlTowerProposal(controlTowerAgent.getRunway().getOriginalLength());

        try {
            message.setContentObject(proposal);
        } catch (IOException e) {
            e.printStackTrace();
        }

        controlTowerAgent.addBehaviour(new ControlTowerBehaviour(controlTowerAgent, message));
    }

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {
        System.out.println("HandleAllResponses");
        System.out.println(responses.size());
        int bestValue = -1;
        ACLMessage bestReply = null;
        AirplaneProposal bestProposal = null;
        for (int i = 0; i < responses.size(); i++) {
            ACLMessage message = (ACLMessage) responses.get(i);
            if (message.getPerformative() == ACLMessage.PROPOSE) {
                ACLMessage reply = message.createReply();
                reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                acceptances.addElement(reply);

                AirplaneProposal proposal = null;
                try {
                    proposal = (AirplaneProposal) message.getContentObject();
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
                assert proposal != null;
                int value = calculateValue(proposal);
                if (value > bestValue) {
                    bestValue = value;
                    bestProposal = proposal;
                    bestReply = reply;
                }
            }
        }
        if (bestReply != null) {
            bestReply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
        }

        while (!controlTowerAgent.enoughSpace(bestProposal.getSpaceRequiredToLand())) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            controlTowerAgent.getLaunchAgents().removeFromAirplaneAgents(bestProposal.getName());
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    private int calculateValue(AirplaneProposal proposal) {
        if (proposal.isSos()) {
            return 2147483647;
        }
        return proposal.getUrgency();
    }
}
