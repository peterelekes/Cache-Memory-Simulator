import Controller.*;
import View.*;
import Controller.*;
import Model.*;

public class Main {
    public static void main(String[] args) {
        SimulationView simulationView = new SimulationView();
        ViewController viewController = new ViewController(simulationView);
    }
}