public class Grass extends AbstractWorldObject {
    int energy = 6;
    public Grass(Vector2d position){
        super(position);
    }
    public String toString(){
        return " * ";
    }

    public void increaseEnergy(int energy) {
        this.energy += energy;
    }

    public int getEnergy() {
        return energy;
    }
}
