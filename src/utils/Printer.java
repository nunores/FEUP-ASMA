package utils;


import agents.ControlTowerAgent;
import proposals.AirplaneProposal;

public class Printer {

    private static Printer printerInstance = null;

    private ControlTowerAgent agent;

    public void setAgent(ControlTowerAgent agent) {
        this.agent = agent;
    }

    public static Printer getInstance(){
        if(printerInstance == null){
            printerInstance = new Printer();
        }

        return printerInstance;
    }

    public void printData(){
        printTable();
        printRunway();
    }

    private void printTable(){
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%40s %20s %20s %20s %20s %20s", "AIRPLANE", "SOS", "SPACE TO LAND", "TIME TO LAND", "TIME WAITING", "FLIGHT TYPE");
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        for(AirplaneProposal airplane: agent.getCurrentAirplanes()){
            System.out.format("%40s %20s %20s %20s %20s %20s",
                    airplane.getName(), airplane.isSos(), airplane.getSpaceRequiredToLand(), airplane.getTimeToLand(), airplane.getTimeWaiting(), airplane.getFlightType());
            System.out.println();
        }

        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

    }

    public void printRunway(){
        System.out.println(ControlTowerAgent.getRunway().getLength() + " out of " + ControlTowerAgent.getRunway().getOriginalLength() + " space units available for landing.");
    }

}
