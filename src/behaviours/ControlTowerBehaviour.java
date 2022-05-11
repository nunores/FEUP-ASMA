package behaviours;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetInitiator;

import agents.ControlTowerAgent;
import proposals.AirplaneProposal;

import java.util.Vector;

public class ControlTowerBehaviour extends ContractNetInitiator {

    private ControlTowerAgent controlTowerAgent;

    public ControlTowerBehaviour(Agent a, ACLMessage cfp) {
        super(a, cfp);
        this.controlTowerAgent = (ControlTowerAgent) a;
    }

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {
        System.out.println("HandleAllResponses");
        int bestValue = -1;
        AirplaneProposal bestProposal = null;
        for (int i = 0; i < responses.size(); i++) {
            ACLMessage message = (ACLMessage) responses.get(i);
            if (message.getPerformative() == ACLMessage.PROPOSE) {
                AirplaneProposal proposal = null;
                try {
                    proposal = (AirplaneProposal) message.getContentObject();
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
                int value = calculateValue(proposal);
                if (value > bestValue) {
                    bestValue = value;
                    bestProposal = proposal;
                    ACLMessage reply = message.createReply();
                    reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    acceptances.addElement(reply);
                }
                else {
                    ACLMessage reply = message.createReply();
                    reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                    acceptances.addElement(reply);
                }
            }
        }
        while (!controlTowerAgent.enoughSpace(bestProposal.getSpaceRequiredToLand())) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int calculateValue(AirplaneProposal proposal) {
        if (proposal.isSos()) {
            return 2147483647;
        }
        return proposal.getUrgency();
    }
}
