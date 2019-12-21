import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.*;

public class AbstractWorldMap implements IWorldMap, IPositionChangeObserver{
    private List<Animal> listOfAnimals = new ArrayList<>();
    private List<Animal> theDead =new ArrayList<>();
    private int lifespan = 0;
    private Multimap<Vector2d, AbstractWorldObject> map =  ArrayListMultimap.create();
    MapVisualizer mapVisualizer;
    private int dayCounter = 0;
    protected Boundary boundary;
    private int parousiaDay;
    private int grassNumber;
    private int startEnergy;
    private MainFrame Layout = new MainFrame();
    private StatisticalData statisticalData;

    public AbstractWorldMap(int grassNumber, int parousiaDay, Boundary boundary,
                            int startAnimalNumber, int startEnergy){
        this.parousiaDay = parousiaDay;
        this.grassNumber = grassNumber;
        this.mapVisualizer = new MapVisualizer(this);
        this.startEnergy = startEnergy;
        this.boundary = boundary;
        mapVisualizer = new MapVisualizer(this);
        statisticalData = new StatisticalData(this);
        placeGrass();
        initialAnimalPlacement(startAnimalNumber);

    }


    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal) {
        if(oldPosition != null){
            map.remove(oldPosition, animal);
            map.put(newPosition, animal);
        }
        else map.put(newPosition,animal);
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
    void theBeginOfTime(){
        for(int i = 0; i<parousiaDay; i++){
            dayCounter++;
            day();
        }
    }

    private void day(){
        movementTime();
        eatingTime();
        breedingTime();
        decayCorpses();
        placeGrass();
        String string = this.toString();
        try {
            Layout.changeText(string);
        } catch (Exception ignored){

        }
        //System.out.println(this.toString());
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
    void initialAnimalPlacement(int startAnimalNumber) {
        for(int i = 0; i < startAnimalNumber; i++) {
            Vector2d tmp = boundary.randomPosition();
            Animal animal = new Animal(tmp, startEnergy, null, null);
            place(animal);
        }
    }
    void placeGrass(){
        //IN SAVANNA
        for(int i = 0; i < grassNumber/2; i++) {
            Vector2d tmp = boundary.randomPositionSavanna();
            Optional<Grass> grassOnPosition = getGrass(tmp);
            if(grassOnPosition.isEmpty()) {
                Grass grass = new Grass(tmp);
                place(grass);
                grassNumber++;
            }
            else grassOnPosition.get().increaseEnergy(6);
        }
        //IN JUNGLE
        for(int i = 0; i < grassNumber/2; i++){
            Vector2d tmp = boundary.randomPositionJungle();
            if(!map.containsKey(tmp)) {
                Grass grass = new Grass(tmp);
                map.put(tmp, grass);
                grassNumber++;
            }
        }
    }

    private Optional<Grass> getGrass(Vector2d tmp) {
        return map.get(tmp).stream().filter(object -> object instanceof Grass).map(Grass.class::cast).findAny();
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
            Grass grass = null;
            for(AbstractWorldObject object : hereAre){
                if(object instanceof Animal) {
                    if (((Animal) object).sameEnergy(theStrongest(object.getPosition())))
                        whoEats.add((Animal) object);
                }
                else if (object instanceof Grass) grass = (Grass) object;

            }
            if(grass == null) return;
            int energy = grass.getEnergy();
            if(!whoEats.isEmpty()){
                grassNumber--;
                energy /= whoEats.size();
                int finalEnergy = energy; //JAVA makes me to do so ;(
                whoEats.forEach(animal -> animal.eat(finalEnergy));
                map.remove(grass.getPosition(), grass);
            }

        }

    }



    private void breedingTime() {
        HashMap<Vector2d, Animal> whoBreeds = new HashMap<>();
        for(Animal animal: listOfAnimals)
            if(isPartnerHere(animal.getPosition()) && animal.sameEnergy(theStrongest(animal.getPosition())))
                whoBreeds.put(animal.getPosition(), animal);
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
    );


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

    public Object objectAt(Vector2d position) {
        return map.get(position);
    }

    @Override
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


    public int size(){
        return map.size();
    }

    private boolean isInList(Grass grass){
        return isOccupied(grass.getPosition());
    }

    public List<Animal> getListOfAnimals() {
        return listOfAnimals;
    }

    public int getStartEnergry(){
        return startEnergy;
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
}
