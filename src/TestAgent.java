import jade.core.Agent;

public class TestAgent extends Agent {
    @Override
    public void setup()
    {
        System.out.println("Test agent name is: " +getAID().getName());
    }
}

