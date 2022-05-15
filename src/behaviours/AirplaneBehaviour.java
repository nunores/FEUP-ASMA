package behaviours;

import agents.AirplaneAgent;
import agents.ControlTowerAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetResponder;
import jade.wrapper.StaleProxyException;
import proposals.AirplaneProposal;
import proposals.ControlTowerProposal;
import utils.Printer;

import java.io.IOException;

public class AirplaneBehaviour extends ContractNetResponder {

    private final AirplaneAgent agent;

    public AirplaneBehaviour(Agent a, MessageTemplate mt) {
        super(a, mt);
        this.agent = (AirplaneAgent) a;
    }

    private ACLMessage createProposal(ACLMessage cfp) throws Exception {
        ACLMessage propose = cfp.createReply();
        propose.setPerformative(ACLMessage.PROPOSE);

        AirplaneProposal proposalArgs = new AirplaneProposal(agent.getTimeToLand(), agent.getSpaceRequiredToLand(),
                calculateTimeLeftOfFuel(agent.getFuel(), agent.getFuelPerSecond()), calculateUrgency(agent.getTimeWaiting(), agent.getFlightType()), agent.isSos(), agent.getName(), agent.getTimeWaiting(), agent.getFlightType());

        try {
            propose.setContentObject(proposalArgs);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return propose;
    }

    private int calculateUrgency(int timeWaiting, AirplaneAgent.FlightType flightType) throws Exception {
        int importanceDegree;
        switch (flightType) {
            case lowCost:
                importanceDegree = 1;
                break;
            case vip:
                importanceDegree = 5;
                break;
            case businessClass:
                importanceDegree = 2;
                break;
            case _private:
                importanceDegree = 3;
                break;
            default:
                throw new Exception("No information about the flight type.");
        }
        return importanceDegree*(timeWaiting+1);
    }

    private int calculateTimeLeftOfFuel(int fuel, double fuelPerSecond) { return (int) (fuel / fuelPerSecond); }

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException {
        ControlTowerProposal proposal = null;
        try {
            proposal = (ControlTowerProposal) cfp.getContentObject();
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
        assert proposal != null;
        if (agent.getSpaceRequiredToLand() > proposal.getRunwayLength()) {
            ACLMessage refuse = cfp.createReply();
            refuse.setPerformative(ACLMessage.REFUSE);
            throw new RefuseException(refuse);
        } else {
            try {
                return createProposal(cfp);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) {
        ControlTowerAgent.landPlane(agent.getSpaceRequiredToLand());
        Printer.getInstance().printData();
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        ControlTowerAgent.parkPlane(agent.getSpaceRequiredToLand());
                    }
                },
                agent.getTimeToLand() * 1000L
        );

        ACLMessage inform = accept.createReply();
        inform.setPerformative(ACLMessage.INFORM);
        try {
            inform.setContentObject(agent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inform;
    }
}
