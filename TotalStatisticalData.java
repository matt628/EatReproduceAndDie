import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class TotalStatisticalData {
    private StatisticalData statisticalData;
    private ArrayList<Integer> animalNumberByDay = new ArrayList();
    private ArrayList<Integer> grassNumberByDay = new ArrayList();
    private ArrayList<String> dominatingGenome = new ArrayList();
    private ArrayList<Integer> meanEnergyByDay = new ArrayList();
    private ArrayList<Integer> meanLifespanByDay = new ArrayList();
    private ArrayList<Integer> meanChildrenNuberByDay = new ArrayList();
    TotalStatisticalData(StatisticalData statisticalData){
        this.statisticalData = statisticalData;

    }

    void actualiseTotalData(){
        animalNumberByDay.add(statisticalData.getAnimalNumber());
        grassNumberByDay.add(statisticalData.getGrassNumber());
        dominatingGenome.add( statisticalData.getDominatingGene());
        meanEnergyByDay.add( statisticalData.getMeanEnergy());
        meanLifespanByDay.add(statisticalData.getMeanLifespan());
        meanChildrenNuberByDay.add(statisticalData.getMeanChildrenNumber());

    }
    int getMean(ArrayList<Integer> array){
        int mean =0;
        for (Integer integer : array) {
            mean += integer;
        }
        mean /= array.size();
        return  mean;
    }
    String topGenome(){
        HashMap<String, Integer> genomes = new HashMap<>();
        for (String genome : dominatingGenome) {
            if(genomes.containsKey(genome)) genomes.put(genome, genomes.remove(genome)+1);
            else genomes.put(genome, 1);
        }

        Integer max = 0;
        String dominatingGene = null;
        for(String string : genomes.keySet()){
            if(genomes.get(string) > max){
                max = genomes.get(string);
                dominatingGene = string;
            }
        }
        return  dominatingGene;

    }

    String calculateMeans(){
        int meanAnimalNumber = getMean(animalNumberByDay);
        int meanGrassNumber = getMean(grassNumberByDay);
        int meanEnergy = getMean(meanEnergyByDay);
        int meanLifespan = getMean(meanLifespanByDay);
        int meanChilderenNumber = getMean(meanChildrenNuberByDay);

        String tmp = String.format("Mean animal number: %d", meanAnimalNumber).concat(System.lineSeparator()).
                concat(String.format("Mean grass number: %d", meanGrassNumber)).concat(System.lineSeparator()).
                concat(String.format("Mean energy: %d", meanEnergy)).concat(System.lineSeparator()).
                concat(String.format("Mean mean lifespan: %d", meanLifespan)).concat(System.lineSeparator()).
                concat(String.format("Mean chldren number: %d", meanChilderenNumber)).concat(System.lineSeparator()).
                concat(topGenome()).concat(System.lineSeparator());
        return  tmp;


    }

    public void exportStatisticalData(){
        try {
            int days = animalNumberByDay.size();
            File statisticalDataFile = new File(String.format("StatisticalDataAfter%d", days));
            if(!statisticalDataFile.exists()) statisticalDataFile.createNewFile();
            PrintWriter pw = new PrintWriter(statisticalDataFile);
            pw.print(calculateMeans());
            pw.close();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

}
