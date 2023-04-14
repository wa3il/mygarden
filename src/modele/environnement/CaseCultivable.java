package modele.environnement;

import modele.SimulateurPotager;
import modele.environnement.varietes.Carrotte;
import modele.environnement.varietes.Legume;
import modele.environnement.varietes.Salade;
import modele.environnement.varietes.Tomate;
import modele.environnement.varietes.Poireau;
import modele.environnement.varietes.Radis;
import modele.environnement.varietes.Varietes;

public class CaseCultivable extends Case {

    private Legume legume;
    
    public CaseCultivable(SimulateurPotager _simulateurPotager) {
        super(_simulateurPotager);
    }

    @Override
    public void actionUtilisateur() {
        Varietes v = simulateurPotager.choixLegume;
        if (legume == null) {
                if (v == Varietes.salade) legume = new Salade();
                if (v == Varietes.carrotte) legume = new Carrotte(); 
                if (v == Varietes.tomate) legume = new Tomate(); 
                if (v == Varietes.poireau) legume = new Poireau(); 
                if (v == Varietes.radis) legume = new Radis();
        } else {
            legume = null;
        }
    }

    public Legume getLegume() {
        return legume;
    }

    @Override
    public void run() {
        if (legume != null) {
            legume.nextStep();
        }
    }

}
