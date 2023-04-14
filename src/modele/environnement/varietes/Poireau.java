package modele.environnement.varietes;

public class Poireau extends Legume {
    @Override
    public Varietes getVariete() {
        return Varietes.poireau;
    }

    @Override
    protected void croissance() {
        System.out.println("Un poireau pousse !!");
    }
}
