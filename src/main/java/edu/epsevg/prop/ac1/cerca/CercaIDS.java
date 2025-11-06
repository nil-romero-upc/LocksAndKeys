package edu.epsevg.prop.ac1.cerca;


import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import edu.epsevg.prop.ac1.model.*;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;


public class CercaIDS extends Cerca {
    public CercaIDS(boolean usarLNT) { super(usarLNT); }

    @Override
    public void ferCerca(Mapa inicial, ResultatCerca rc) {
        rc.startTime();

        int limit = 1;              // profunditat inicial
        boolean trobat = false;     // marca si hem trobat la meta
        List<Moviment> cami = null; // guardar el camí quan es trobi

        // Bucle infinit controlat: cada iteració incrementa el límit de profunditat
        while (!trobat) {
            // Reiniciem estructures per cada iteració
            ControlCicles cc = new ControlCicles(usarLNT);
            Deque<Node> LNO = new ArrayDeque<>();

            // Node arrel
            Node arrel = new Node(inicial, null, null, 0, 0);
            LNO.addFirst(arrel);
            cc.esRepetit(null, inicial, 0);

            // Flag per detectar si hi ha hagut algun node tallat
            boolean haExpandit = false;

            while (!LNO.isEmpty()) {
                Node actual = LNO.removeFirst();
                rc.incNodesExplorats();

                // Si trobem la meta, sortim
                if (actual.estat.esMeta()) {
                    cami = Cerca.reconstruirCami(actual);
                    trobat = true;
                    break;
                }

                // Si arribem al límit actual de profunditat, no expandim més
                if (actual.depth >= limit) {
                    rc.incNodesTallats();
                    continue;
                }

                // Expandim successors
                for (Moviment mov : actual.estat.getAccionsPossibles()) {
                    Mapa nouEstat = actual.estat.mou(mov);
                    Node successor = new Node(nouEstat, actual, mov, actual.depth + 1, actual.g + 1);

                    if (!cc.esRepetit(actual, nouEstat, successor.depth)) {
                        LNO.addFirst(successor);
                        haExpandit = true;
                    } else {
                        rc.incNodesTallats();
                    }
                }

                rc.updateMemoria(LNO.size() + cc.mida());
            }

            // Si hem trobat la solució, aturem la cerca
            if (trobat) break;

            // Si ja no hi ha nodes nous a expandir, la cerca s'ha esgotat
            if (!haExpandit && LNO.isEmpty()) {
                break; // No hi ha més nodes possibles: no hi ha solució
            }

            // Incrementem el límit i tornem a començar
            limit++;
        }

        rc.stopTime();

        // Guardem resultat final
        if (trobat) {
            rc.setCami(cami);
        } else {
            rc.setCami(null);
        }
    }
}
