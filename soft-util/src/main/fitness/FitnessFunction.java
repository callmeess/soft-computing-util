
import com.example.softcomputing.genetic.chromosome.Chromosome;

public class FitnessFunction {
    public static boolean evaluate(Chromosome<?> parent1 , Chromosome<?> parent2) {

        return parent1.evaluate() > parent2.evaluate(); 
    }
    
}
