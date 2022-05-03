package behaviours;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetResponder;
import agents.AirplaneAgent;
import proposals.AirplaneProposal;
import proposals.ControlTowerProposal;

import java.io.IOException;

public class AirplaneBehaviour extends ContractNetResponder {

    private AirplaneAgent agent;

    public AirplaneBehaviour(Agent a, MessageTemplate mt) {
        super(a, mt);
        this.agent = (AirplaneAgent) a;
    }

    private ACLMessage createProposal(ACLMessage cfp) {
        ACLMessage propose = cfp.createReply();
        propose.setPerformative(ACLMessage.PROPOSE);

        AirplaneProposal proposalArgs = new AirplaneProposal(agent.getTimeToLand(), agent.getSpaceRequiredToLand(), agent.getTimeLeftOfFuel(), agent.getTimeWaiting());

        try {
            propose.setContentObject(proposalArgs);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return propose;
    }

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException {

        ControlTowerProposal proposal = null;
        try {
            proposal = (ControlTowerProposal) cfp.getContentObject();
        } catch (UnreadableException e) {
            e.printStackTrace();
        }

        if (agent.getSpaceRequiredToLand() > proposal.getRunwayLength()) {
            throw new RefuseException("Plane too big for the runway");
        }
        else {
            return createProposal(cfp);
        }
    }
}
