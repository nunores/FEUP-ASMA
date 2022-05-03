package agents;

import behaviours.AirplaneBehaviour;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AirplaneAgent extends Agent {

    private int timeToLand; // In seconds
    private int spaceRequiredToLand; // In meters
    private int timeLeftOfFuel; // In seconds
    private int timeWaiting; // In seconds


    @Override
    public void setup()
    {
        Object[] args = this.getArguments();
        timeToLand = (int) args[0];
        spaceRequiredToLand = (int) args[1];
        timeLeftOfFuel = (int) args[2];
        timeWaiting = (int) args[3];

        MessageTemplate message = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
                MessageTemplate.MatchPerformative(ACLMessage.CFP)
        );

        addBehaviour(new AirplaneBehaviour(this, message));

    }

    public int getTimeToLand() {
        return timeToLand;
    }

    public int getSpaceRequiredToLand() {
        return spaceRequiredToLand;
    }

    public int getTimeLeftOfFuel() {
        return timeLeftOfFuel;
    }

    public int getTimeWaiting() {
        return timeWaiting;
    }
}

