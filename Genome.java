import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Genome {
    final private int genomeSize = 32;
    final private int breakPointNumber = 2;
    private static Random random = new Random();
    ArrayList<Integer> genome;
    public Genome(Animal parent1, Animal parent2){
        if(parent1 == null || parent2 == null)
            this.genome = balancedGenome();
        else
            this.genome = setGenome(parent1.getGenome(), parent2.getGenome());

    }
    private ArrayList<Integer> setGenome(Genome parent1, Genome parent2){
        ArrayList<Integer> childGenome = new ArrayList<>(genomeSize);
        ArrayList<Integer> breakPoints = new ArrayList<>();
        breakPoints.add(0); breakPoints.add(genomeSize);
        for(int i = 0; i < breakPointNumber; i++) breakPoints.add(random.nextInt(genomeSize));
            breakPoints.sort(Integer::compareTo);
        for(int i = 1; i < breakPoints.size(); i++){
            int whichParent = random.nextInt(2);
            if(whichParent == 0)
                childGenome.addAll(parent1.getGenome().subList(breakPoints.get(i-1), breakPoints.get(i)));
            else
                childGenome.addAll(parent2.getGenome().subList(breakPoints.get(i-1), breakPoints.get(i)));
        }
        maintainFreeWill(childGenome);
        return childGenome;
    }

    private void maintainFreeWill(ArrayList<Integer> genome){
        Integer[] buckets = getGeneCategory(genome);
        for(Integer i : buckets)
            if(i == 0){
                int max = Collections.max(Arrays.asList(buckets));
                for(int j = 0; j < genomeSize; j++)
                    if(genome.get(j) == max) {
                        genome.remove(j);
                        break;
                }
                genome.add(i);
            }
    }

    private ArrayList<Integer> balancedGenome() {
        ArrayList<Integer> genome = permutation(0,genomeSize);
        genome.replaceAll(a-> a%8);
        return genome;
    }
    private ArrayList<Integer> permutation(int startElement, int genomeSize){
        ArrayList<Integer> genome = new ArrayList<>(genomeSize);
        ArrayList<Integer> tmp = new ArrayList<>(genomeSize);
        for(int i = 0; i < genomeSize; i++) tmp.add(i);
        for(int i = 0; i < genomeSize; i++){
            int rand = random.nextInt(tmp.size());
            genome.add(tmp.get(rand));
            tmp.remove(rand);
        }
        return genome;
    }
    Integer getRandomAllele(){
        int rand = random.nextInt(genomeSize);
        return genome.get(rand);
    }

    public ArrayList<Integer> getGenome() {
        return genome;
    }
    private Integer[] getGeneCategory(ArrayList<Integer> examinedGenome) throws NullPointerException{
        Integer[] buckets = new Integer[8];
        Arrays.fill(buckets, 0);
        for(int i = 0; i<genomeSize; i++) buckets[examinedGenome.get(i)]++;
        return buckets;
    }
    @Override

    public String toString() {
       // try {
            return Arrays.toString(getGeneCategory(this.genome));
        //}
        //catch (NullPointerException ex){
        //    return "There is no gene";
        //}
    }


}