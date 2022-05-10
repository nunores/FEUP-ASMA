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
            }
        }
    }

    private int calculateValue(AirplaneProposal proposal) {
        return 0;
    }
}
