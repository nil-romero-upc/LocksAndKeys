package edu.epsevg.prop.ac1.cerca;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

import edu.epsevg.prop.ac1.cerca.heuristica.Heuristica;
import edu.epsevg.prop.ac1.model.*;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;


public class CercaAStar extends Cerca {

    private final Heuristica heur;

    public CercaAStar(boolean usarLNT, Heuristica heur) { 
        super(usarLNT); 
        this.heur = heur; 
    }

    @Override
    public  void ferCerca(Mapa inicial, ResultatCerca rc) {
        rc.startTime();

        // Control de cicles (segons usarLNT)
        ControlCicles cc = new ControlCicles(usarLNT);

        // LNO: PriorityQueue ordenada per f(n) = g(n) + h(n)
        PriorityQueue<Node> LNO = new PriorityQueue<>(Comparator.comparingInt(n -> n.g + heur.h(n.estat)));

        // Node arrel
        Node arrel = new Node(inicial, null, null, 0, 0);
        LNO.add(arrel);
        cc.esRepetit(null, inicial, 0);

        boolean trobat = false;
        Node actual = null;

        while (!LNO.isEmpty()) {
            actual = LNO.poll(); // node amb menor f = g + h
            rc.incNodesExplorats();

            // Si arribem a la meta, reconstruïm camí
            if (actual.estat.esMeta()) {
                trobat = true;
                break;
            }

            // Expansió de successors
            for (Moviment mov : actual.estat.getAccionsPossibles()) {
                try {
                    Mapa nouEstat = actual.estat.mou(mov);
                    Node successor = new Node(nouEstat, actual, mov, actual.depth + 1, actual.g + 1);

                    if (!cc.esRepetit(actual, nouEstat, successor.depth)) {
                        LNO.add(successor);
                    } else {
                        rc.incNodesTallats();
                    }
                } catch (Exception e) {
                    // Moviment no vàlid (mur, porta tancada, col·lisió)
                    rc.incNodesTallats();
                }
            }

            rc.updateMemoria(LNO.size() + cc.mida());
        }

        rc.stopTime();

        // Resultats finals
        if (trobat) {
            rc.setCami(reconstruirCami(actual));
        } else {
            rc.setCami(null);
        }
    }
}
