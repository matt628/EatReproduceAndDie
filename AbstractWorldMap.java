import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.*;

public class AbstractWorldMap implements IWorldMap, IPositionChangeObserver{
    private boolean isRunning = true;
    private MainFrame Layout = new MainFrame(this);
    private List<Animal> listOfAnimals = new ArrayList<>();
    private List<Animal> theDead =new ArrayList<>();
    private int lifespan = 0;
    private Multimap<Vector2d, AbstractWorldObject> map =  ArrayListMultimap.create();
    private MapVisualizer mapVisualizer;
    private int dayCounter = 0;
    Boundary boundary;
    private int grassNumber;
    private StatisticalData statisticalData;
    Multiversum multiversum;

    public AbstractWorldMap(int grassNumber, Boundary boundary, Multiversum multiversum){
        this.grassNumber = grassNumber;
        this.boundary = boundary;
        this.multiversum = multiversum;
        mapVisualizer = new MapVisualizer(map);
        statisticalData = new StatisticalData(this);

        placeGrass();
        initialAnimalPlacement(multiversum.getStartAnimalNumber());

    }

//SIMULATION RUNING
    public void theBeginOfTime() throws InterruptedException {
        for(int i = 0; i<multiversum.getParousiaDay(); i++) {
            dayCounter++;
            if (isRunning) day();
            else while (!isRunning) Thread.sleep(200);
            Thread.sleep(800);
        }
    }

    void day(){
        placeGrass();
        movementTime();
        eatingTime();
        breedingTime();
        decayCorpses();
        String string = this.toString();
        try {
            Layout.changeText(string);
            Layout.actualiseStatistics(statisticalData.toString());
        } catch (Exception ignored){

        }
    }

    private void decayCorpses() {
        int totalLifespanToday = 0;
        List<Animal> todayLeaveUs = new ArrayList<>();
        for(Animal animal:listOfAnimals) {
            if(animal.getEnergy() < 0){
                map.remove(animal.getPosition(), animal);
                todayLeaveUs.add(animal);
                totalLifespanToday += animal.getAge();
            }
        }
        this.lifespan = this.lifespan*theDead.size() + totalLifespanToday;
        theDead.addAll(todayLeaveUs);
        if(theDead.size() != 0)this.lifespan /= theDead.size();
        listOfAnimals.removeIf(animal -> animal.getEnergy() < 0 );

       if(listOfAnimals.isEmpty()) throw new IllegalStateException("All animals became extinct before your arrival Lord.");
    }
    private void placeGrass(){//JUNGLE AND SAVANNA
        for(int i = 0; i < 2*multiversum.getDailyGrassNumber(); i++) {
            Vector2d tmp =null;
            if(i%2 == 0)  //IN SAVANNA OR IN JUNGLE
                tmp = boundary.randomPositionSavanna();
            else
                tmp = boundary.randomPositionJungle();
            Optional<Grass> grassOnPosition = getGrassFromPosition(tmp);
            if(!isAnimalHere(tmp)) {
                if (grassOnPosition.isEmpty()) {
                    Grass grass = new Grass(tmp);
                    place(grass);
                    grassNumber++;
                } else grassOnPosition.get().increaseEnergy(6);
            }
        }
    }

    private void movementTime() {
        for (Animal animal : listOfAnimals) {
            animal.move();
        }
    }
    private void eatingTime(){
        Set<Vector2d> occupiedPositions = map.keySet();
        for(Vector2d position : occupiedPositions){
            List<Animal> whoEats = new ArrayList<>();
            Collection<AbstractWorldObject> hereAre = map.get(position);
            Grass grassHere = null;
            if(hereAre.size()>1){
                for(AbstractWorldObject object : hereAre){
                    if(object instanceof Animal) {
                        if (((Animal) object).sameEnergy(theStrongest(object.getPosition())))
                            whoEats.add((Animal) object);
                    }
                    else if (object instanceof Grass) grassHere = (Grass) object;
                }
                if(grassHere == null) return;
                int energy = grassHere.getEnergy();
                if(!whoEats.isEmpty()){
                    grassNumber--;
                    energy /= whoEats.size();
                    int finalEnergy = energy; //JAVA makes me to do so ;(
                    whoEats.forEach(animal -> animal.eat(finalEnergy));
                    //AbstractWorldObject grass = hereAre.stream().filter(object -> object instanceof Grass).findAny().get();
                    this.map.remove(position, grassHere);
                }
                //Functional code
                //STREAM TO ARRAY??
                /*whoEats = hereAre.stream().filter(object -> object instanceof Animal).filter(animal -> ((Animal) animal).sameEnergy(theStrongest(animal.getPosition())))
                        .map(Animal.class::cast).toArray();*/
                    /*             Optional<Grass> tmp = hereAre.stream().
                        filter(object -> object instanceof Grass).map(Grass.class::cast).findAny();*/
            }
        }

    }


//BREEDING
    private void breedingTime() {
        HashMap<Vector2d, Animal> whoBreeds = new HashMap<>();
        for(Animal animal: listOfAnimals)
            if(isPartnerHere(animal.getPosition()) && animal.sameEnergy(theStrongest(animal.getPosition())))
                whoBreeds.put(animal.getPosition(), animal);
        if(!whoBreeds.isEmpty())
            whoBreeds.forEach((position, animal) -> animal.mate(findMatingPartner(position)));
    }
    private Animal findMatingPartner(Vector2d position) {
        Collection<AbstractWorldObject> hereAre = map.get(position);
        Animal strongest = theStrongest(position);
        hereAre.remove(strongest);
        Optional<AbstractWorldObject> matingPartner = hereAre.stream()
                .filter(object ->  object instanceof Animal).
                min(comparator);
        return (Animal) matingPartner.orElse(null);
    }
    private boolean isPartnerHere(Vector2d position){
        Collection<AbstractWorldObject> hereAre = map.get(position);
        return hereAre.stream().filter(object -> object instanceof Animal).count() > 1;
    }
    private Comparator<AbstractWorldObject> comparator = Comparator.comparing(o ->
                 ((Animal) o).getEnergy()
    ); //WHY it works?
//PLACEMENT
    void initialAnimalPlacement(int startAnimalNumber) {
        for(int i = 0; i < startAnimalNumber; i++) {
            Vector2d tmp = boundary.randomPosition();
            Animal animal = new Animal(tmp, multiversum.getStartEnergy(), null, null, this);
            place(animal);
        }
    }
    public boolean place(AbstractWorldObject object) throws IllegalArgumentException {
        if(canMoveTo(object.getPosition())) {
            map.put(object.getPosition(), object);
            if(object instanceof Animal) {
                listOfAnimals.add((Animal) object);
                ((Animal) object).addObserver(this);
            }
            return true;
        }
        else {
            throw new IllegalArgumentException("trying to place object at: " + object.getPosition() + ". Position is already occupied by an animal.");
        }

    }

//OTHER METHODS
    public Animal theStrongest(Vector2d position){
        Collection<AbstractWorldObject> positionList = map.get(position);
        Animal strongest = (Animal) positionList.stream().filter(object -> object instanceof Animal).findAny().get();
        for(AbstractWorldObject worldObject : positionList) {
            if(worldObject instanceof  Animal){
                if(((Animal) worldObject).getEnergy() >  strongest.getEnergy())
                    strongest = (Animal) worldObject;
            }
        }
        return  strongest;

    }

    private boolean isAnimalHere(Vector2d position){
        Collection<AbstractWorldObject> hereAre = map.get(position);
        return hereAre.stream().anyMatch(object -> object instanceof Animal);
    }

    private Optional<Grass> getGrassFromPosition(Vector2d tmp) {
        return map.get(tmp).stream().filter(object -> object instanceof Grass).map(Grass.class::cast).findAny();
    }

    public Object objectAt(Vector2d position) {
        return map.get(position);
    }

    public void bendTime(){
        isRunning = !isRunning;
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal) {
        if(oldPosition != null){
            map.remove(oldPosition, animal);
            map.put(newPosition, animal);
        }
        else map.put(newPosition,animal);
    }

    public boolean canMoveTo(Vector2d position) {
        return true;

    }
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    public String toString() {
        System.out.println(dayCounter);
        return mapVisualizer.draw(new Vector2d(0,0), boundary.getUpperRight());
    }

//GETTERS


    public StatisticalData getStatisticalData() {
        return statisticalData;
    }

    public int getSize(){
        return map.size();
    }

    private boolean isInList(Grass grass){
        return isOccupied(grass.getPosition());
    }

    public List<Animal> getListOfAnimals() {
        return listOfAnimals;
    }


    public List<Animal> getTheDead() {
        return theDead;
    }

    public int getGrassNumber() {
        return grassNumber;
    }

    public int getLifespan() {
        return lifespan;
    }

    public int getDayCounter() {
        return dayCounter;
    }
}
