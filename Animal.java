import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
public class Animal extends AbstractWorldObject {
    private MapDirection dir;
    private Set<IPositionChangeObserver> observerSet;
    private  int energy;
    private  int childrenNumber = 0;
    private ArrayList<Animal> offspring = new ArrayList<>();
    private  Genome genome;
    private  int age;

    Animal(Vector2d initialPosition, int energy, Animal parent1, Animal parent2){
        super(initialPosition);
        this.dir = MapDirection.randomDirection();
        this.observerSet = new HashSet<>();
        this.energy = energy;
        this.genome = new Genome(parent1, parent2);
        this.age = 0;
    }



    void mate(Animal matingPartner){
        if(this == matingPartner) return;
        if(this.getAge() < 5 && matingPartner.getAge() < 5) return;
        childrenNumber++;
        int childEnergy = (int) ((this.getEnergy() + matingPartner.getEnergy())/4);
        Animal child = new Animal(this.position, childEnergy, this, matingPartner);
        child.positionChanged(null, child.getPosition());
        this.afterMatingExhaustion(); this.rememberOffspring(child);
        matingPartner.afterMatingExhaustion(); matingPartner.rememberOffspring(child);
    }
    private void rememberOffspring(Animal child) {
        this.offspring.add(child);
    }
    private void afterMatingExhaustion(){
        this.energy = (this.energy*3)/4;
    }


    public String toString(){
        int energy = (int) this.energy;
        return String.valueOf(energy);
    }

    public void move () {
        energy--;
        age++;
        this.randomRotate();
        Vector2d oldPosition = position;
        position = position.add(this.dir.toUnitVector());
        positionChanged(oldPosition,position);
    }

    void randomRotate(){
        this.dir = MapDirection.rotate(this.dir, MapDirection.values()[genome.getRandomAllele()]);
    }

    public void eat(int energy){
        this.energy += energy;

    }

    void addObserver(IPositionChangeObserver observer){
        observerSet.add(observer);
    }
    void removeObserver(IPositionChangeObserver observer){
        observerSet.remove(observer);
    }
    void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for (IPositionChangeObserver observer : observerSet)
            observer.positionChanged(oldPosition, newPosition, this);

        }
    public int getEnergy() {
        return energy;
    }



    public boolean sameEnergy(Animal competitor){
        return (java.lang.Math.abs(this.getEnergy() - competitor.getEnergy()) < 0.1);
    }

    public Genome getGenome() {
        return genome;
    }

    public ArrayList<Animal> getOffspring() {
        return offspring;
    }

    public int getAge() {
        return age;
    }

    public int getChildrenNumber() {
        return childrenNumber;
    }
}

