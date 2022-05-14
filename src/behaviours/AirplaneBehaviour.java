package behaviours;

import agents.AirplaneAgent;
import agents.ControlTowerAgent;
import agents.LaunchAgents;
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

import java.io.IOException;

public class AirplaneBehaviour extends ContractNetResponder {

    private final AirplaneAgent agent;
    private ControlTowerAgent controlTowerAgent;

    public AirplaneBehaviour(Agent a, MessageTemplate mt) throws StaleProxyException {
        super(a, mt);
        this.agent = (AirplaneAgent) a;
        //sendHello();
    }

    private void sendHello() throws StaleProxyException {
        ACLMessage hello = new ACLMessage(ACLMessage.INFORM);
        //System.out.println(LaunchAgents.getInstance().getControlTowerController().getName());
        hello.addReceiver(new AID("ControlTowerAgent@192.168.0.3:1099/JADE", AID.ISGUID));
        hello.setContent("Hello");
        this.agent.send(hello);
    }

    private ACLMessage createProposal(ACLMessage cfp) throws Exception {
        ACLMessage propose = cfp.createReply();
        propose.setPerformative(ACLMessage.PROPOSE);

        AirplaneProposal proposalArgs = new AirplaneProposal(agent.getTimeToLand(), agent.getSpaceRequiredToLand(),
                calculateTimeLeftOfFuel(agent.getFuel(), agent.getFuelPerSecond()), calculateUrgency(agent.getTimeWaiting(), agent.getFlightType()), agent.isSos(), agent.getName());

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
        return importanceDegree*timeWaiting;
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
        System.out.println("Landed " + agent.getSpaceRequiredToLand());
        ControlTowerAgent.landPlane(agent.getSpaceRequiredToLand());
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("Parking " + agent.getSpaceRequiredToLand());
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
