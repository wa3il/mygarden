/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.environnement;

import modele.SimulateurPotager;
import modele.environnement.varietes.Legume;

public abstract class Case implements Runnable {
    protected SimulateurPotager simulateurPotager;

    private int précipitations; // TODO : mis à jour par le simulateur de météo pour chaque case ()
    private int ensolleillement;

    private boolean RecoltePlante = true;
    
    
    public Case(SimulateurPotager _simulateurPotager) {
        simulateurPotager = _simulateurPotager;
    }

    public abstract void actionUtilisateur();

  }
