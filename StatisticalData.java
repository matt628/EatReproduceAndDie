import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

class StatisticalData {
    private int animalNumber;
    private int grassNumber;
    private int meanEnergy;
    private int meanLifespan = 0;
    private int meanChildernNumber = 0;
    private AbstractWorldMap map;

    StatisticalData(AbstractWorldMap map){
        this.map = map;

    }
    String dominatingGene(){
        Map<String, Integer> genes = new HashMap<>();
        for (Animal animal : map.getListOfAnimals()) {
            String string = animal.getGenome().toString();
            if(!genes.containsKey(string)) genes.put(string, 1);
            else genes.put(string, 1 + genes.remove(string));
        }
        Integer max = 0;
        String dominatingGene = null;
        for(String string : genes.keySet()){
            if(genes.get(string) > max){
                max = genes.get(string);
                dominatingGene = string;
            }
        }
        return  dominatingGene;
    }
    void meanEnergy(){
        int meanEnergy = 0;
        int meanChildrenNumber = 0;
        for (Animal animal : map.getListOfAnimals()) {
            meanEnergy += animal.getEnergy();
            meanChildrenNumber += animal.getChildrenNumber();
        }
        this.meanEnergy = meanEnergy / map.getListOfAnimals().size();
        this.meanChildernNumber = meanChildrenNumber / map.getListOfAnimals().size();
    }
    void meanLifespan(){
        this.meanLifespan = map.getLifespan();
    }

    void countAnimals(){
        animalNumber = map.getListOfAnimals().size();
    }
    void countGrass(){
       grassNumber = map.getGrassNumber();
    }

    int countOffspring(Animal animal){
        HashSet<Animal> counted = new HashSet<>();
        countOffspringInnerFunction(animal, counted);
        return counted.size();
    }
    private void countOffspringInnerFunction(Animal animal, HashSet<Animal> counted){
        for(Animal offspring : animal.getOffspring()){
            counted.add(offspring);
            countOffspringInnerFunction(offspring, counted);
        }
    }


}
