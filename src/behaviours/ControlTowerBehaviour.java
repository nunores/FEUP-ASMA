package behaviours;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import agents.ControlTowerAgent;

public class ControlTowerBehaviour extends ContractNetInitiator {

    private ControlTowerAgent controlTowerAgent;

    public ControlTowerBehaviour(Agent a, ACLMessage cfp) {
        super(a, cfp);
        this.controlTowerAgent = (ControlTowerAgent) a;
        System.out.println("Added behaviour");
    }
}
