package VueControleur;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import modele.SimulateurPotager;
import modele.environnement.*;
import modele.environnement.varietes.Legume;
import modele.environnement.varietes.Varietes;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle
 *
 */
public class VueControleurPotager extends JFrame implements Observer {
    private SimulateurPotager simulateurPotager; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)

    private int sizeX; // taille de la grille affichée
    private int sizeY;

    // icones affichées dans la grille
    private ImageIcon icoSalade;
    private ImageIcon icoCarotte;
    private ImageIcon icoPoireau;
    private ImageIcon icoRadis;
    private ImageIcon icoTomate;

    private ImageIcon icoTerre;
    private ImageIcon icoVide;
    private ImageIcon icoMur;


    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)


    public VueControleurPotager(SimulateurPotager _simulateurPotager) throws Exception {
        sizeX = simulateurPotager.SIZE_X;
        sizeY = _simulateurPotager.SIZE_Y;
        simulateurPotager = _simulateurPotager;

        chargerLesIcones();
        placerLesComposantsGraphiques();
        //ajouterEcouteurClavier(); // si besoin
    }
/*
    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT : Controle4Directions.getInstance().setDirectionCourante(Direction.gauche); break;
                    case KeyEvent.VK_RIGHT : Controle4Directions.getInstance().setDirectionCourante(Direction.droite); break;
                    case KeyEvent.VK_DOWN : Controle4Directions.getInstance().setDirectionCourante(Direction.bas); break;
                    case KeyEvent.VK_UP : Controle4Directions.getInstance().setDirectionCourante(Direction.haut); break;
                }
            }
        });
    }
*/

    private void chargerLesIcones() {
    	// image libre de droits utilisée pour les légumes : https://www.vecteezy.com/vector-art/2559196-bundle-of-fruits-and-vegetables-icons	
    
        //On charge les légumes (pos 390)
        icoSalade = chargerIcone("Images/data.png", 0, 0, 140, 140);//chargerIcone("Images/Pacman.png");
        icoCarotte = chargerIcone("Images/data.png", 390, 390, 140, 140);
        icoPoireau = chargerIcone("Images/data.png", 7 * 390, 0, 140, 140);
        icoRadis = chargerIcone("Images/data.png", 2*390, 2*390, 140, 140);
        icoTomate = chargerIcone("Images/data.png", 4*390, 390, 140, 140);

        icoVide = chargerIcone("Images/Vide.png");
        icoMur = chargerIcone("Images/Mur.png");
        icoTerre = chargerIcone("Images/Terre.png");
    }

    private void placerLesComposantsGraphiques() throws Exception {
        //appliquer nimbus look and feel
        UIManager.setLookAndFeel(new NimbusLookAndFeel());

        //elts graphiques
        setTitle("A vegetable garden");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre // libérer les ressources de la window
        setLocationRelativeTo(null); // position de la fenêtre centrée dans mon bureau
        
        

        JPanel infos = new JPanel();
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));

        JTextField jtf = new JTextField("infos diverses"); // TODO inclure dans mettreAJourAffichage ...
        jtf.setEditable(false);
        infos.add(jtf);

        add(infos, BorderLayout.EAST);
        
        JButton carotteButton = new JButton(icoCarotte);
        infos.add(carotteButton);
        carotteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulateurPotager.PlanterLegume(Varietes.carrotte);
            }
        });
        
        JButton SaladeButton = new JButton(icoSalade);
        infos.add(SaladeButton);
        SaladeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulateurPotager.PlanterLegume(Varietes.salade);
            }
        });

        JButton PoireauButton = new JButton(icoPoireau);
        infos.add(PoireauButton);
        PoireauButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulateurPotager.PlanterLegume(Varietes.poireau);
            }
        });

        JButton radisButton = new JButton(icoRadis);
        infos.add(radisButton);
        radisButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulateurPotager.PlanterLegume(Varietes.radis);
            }
        });

        JButton tomateButton = new JButton(icoTomate);
        infos.add(tomateButton);
        tomateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulateurPotager.PlanterLegume(Varietes.tomate);
            }
        });


        JComponent grilleJLabels = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille

        tabJLabel = new JLabel[sizeX][sizeY];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jlab = new JLabel();

                tabJLabel[x][y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                grilleJLabels.add(jlab);
            }
        }
        add(grilleJLabels, BorderLayout.CENTER);

        // écouter les évènements

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                final int xx = x; // constantes utiles au fonctionnement de la classe anonyme
                final int yy = y;
                tabJLabel[x][y].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        simulateurPotager.actionUtilisateur(xx, yy);
                    }
                });
            }
        }
    }

    
    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    private void mettreAJourAffichage() {

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if (simulateurPotager.getPlateau()[x][y] instanceof CaseCultivable) { // si la grille du modèle contient un Pacman, on associe l'icône Pacman du côté de la vue

                    Legume legume = ((CaseCultivable) simulateurPotager.getPlateau()[x][y]).getLegume();

                    if (legume != null) {

                        switch (legume.getVariete()) {
                            case salade: tabJLabel[x][y].setIcon(icoSalade); break;
                            case carrotte: tabJLabel[x][y].setIcon(icoCarotte); break;
                            case tomate: tabJLabel[x][y].setIcon(icoTomate); break;
                            case poireau: tabJLabel[x][y].setIcon(icoPoireau); break;
                            case radis: tabJLabel[x][y].setIcon(icoRadis); break;
                        }

                    } else {
                        tabJLabel[x][y].setIcon(icoTerre);
                    }

                    // si transparence : images avec canal alpha + dessins manuels (voir ci-dessous + créer composant qui redéfinie paint(Graphics g)), se documenter
                    //BufferedImage bi = getImage("Images/smick.png", 0, 0, 20, 20);
                    //tabJLabel[x][y].getGraphics().drawImage(bi, 0, 0, null);
                } else if (simulateurPotager.getPlateau()[x][y] instanceof CaseNonCultivable) {
                    tabJLabel[x][y].setIcon(icoMur);
                } else {

                    tabJLabel[x][y].setIcon(icoVide);
                }
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        mettreAJourAffichage();
        /*
        SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mettreAJourAffichage();
                    }
                }); 
        */

    }


    // chargement de l'image entière comme icone
    private ImageIcon chargerIcone(String urlIcone) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(VueControleurPotager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }


        return new ImageIcon(image);
    }

    // chargement d'une sous partie de l'image
    private ImageIcon chargerIcone(String urlIcone, int x, int y, int w, int h) {
        // charger une sous partie de l'image à partir de ses coordonnées dans urlIcone
        BufferedImage bi = getSubImage(urlIcone, x, y, w, h);
        // adapter la taille de l'image a la taille du composant (ici : 20x20)
        return new ImageIcon(bi.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
    }

    private BufferedImage getSubImage(String urlIcone, int x, int y, int w, int h) {
        BufferedImage image = null;

        try {
            File f = new File(urlIcone);
            image = ImageIO.read(new File(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(VueControleurPotager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        BufferedImage bi = image.getSubimage(x, y, w, h);
        return bi;
    }

}
