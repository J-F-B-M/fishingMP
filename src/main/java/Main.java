import java.util.LinkedList;
import java.util.List;

/**
 * Created by Joachim on 01.04.2017.
 */
public class Main {
    public static void main(String[] args) {
        FishPond pond = new FishPond(100, 1000,0.0001);

        LinkedList<Double> percentages = new LinkedList<>();
        percentages.add(0.1);

        for (int i = 0; i < 100; i++) {
            System.out.println("Fished:  " + Math.round(pond.fish(percentages).get(0)));
            pond.repopulate(1);
            System.out.println("Time " + (i+1) + ": " + Math.round(pond.population));
        }
    }
}
