import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Joachim on 01.04.2017.
 */
public class FishPond {
    double population;
    private final double targetPopulation;
    private double growthFactor;

    public FishPond(double startPopulation, double targetPopulation) {
        this(startPopulation,targetPopulation, 0.0005);
    }

    public FishPond(double startPopulation, double targetPopulation, double growthFactor){
        population = startPopulation;
        this.targetPopulation = targetPopulation;
        this.growthFactor = growthFactor;
    }

    public List<Double> fish(List<Double> percentages){
         List<Double> amounts = percentages.stream().mapToDouble(percent->population * percent).mapToObj(d->(Double.valueOf(d))).collect(Collectors.toList());
         population -= amounts.stream().mapToDouble(Double::doubleValue).sum();
        return amounts;
    }

    public void repopulate(double time){
        double rand = Math.random()*2;

        double newPopulation  = targetPopulation / (1 + Math.exp(-growthFactor*rand * targetPopulation * time) * (targetPopulation/population-1));
        population = newPopulation;
    }
}
