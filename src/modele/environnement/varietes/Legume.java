package modele.environnement.varietes;

public abstract class Legume {
    //états

    public abstract Varietes getVariete();
    
    public void nextStep() {
        croissance();
    }

    protected abstract void croissance(); // définir selon les conditions
}
