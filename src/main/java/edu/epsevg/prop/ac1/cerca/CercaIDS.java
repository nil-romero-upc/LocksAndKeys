package edu.epsevg.prop.ac1.cerca;


import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;

import edu.epsevg.prop.ac1.model.*;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;


public class CercaIDS extends Cerca {
    public CercaIDS(boolean usarLNT) { super(usarLNT); }

    @Override
    public void ferCerca(Mapa inicial, ResultatCerca rc) {
        rc.startTime();

        boolean trobat = false;
        Node solucio = null;
        int limit = 0;

        // Bucle principal IDS
        while (!trobat) {

            // ------------------------------
            // LNO: Llista de Nodes Oberts (PILA -> DFS)
            // ------------------------------
            Deque<Node> LNO = new ArrayDeque<>();

            // ------------------------------
            // LNT: Llista de Nodes Tancats (HASHSET -> per evitar reexploracions dins la mateixa iteració)
            // ------------------------------
            HashSet<Mapa> LNT = new HashSet<>();

            // Node inicial
            LNO.push(new Node(inicial, null, null, 0, 0));

            // Cerca en profunditat limitada
            while (!LNO.isEmpty() && !trobat) {
                Node actual = LNO.pop(); // LIFO -> treure l’últim node afegit
                rc.incNodesExplorats();

                // Si ja hem vist aquest estat a aquesta iteració -> saltem
                if (LNT.contains(actual.estat)) {
                    rc.incNodesTallats();
                    continue;
                }
                LNT.add(actual.estat);

                // Si hem trobat la meta
                if (actual.estat.esMeta()) {
                    solucio = actual;
                    trobat = true;
                    break;
                }

                // Si arribem al límit -> no expandim més
                if (actual.depth == limit) {
                    rc.incNodesTallats();
                    continue;
                }

                // Expandeix successors
                for (Moviment mov : actual.estat.getAccionsPossibles()) {
                    Mapa nouEstat = actual.estat.mou(mov);
                    Node fill = new Node(nouEstat, actual, mov, actual.depth + 1, 0);

                    // No cal mirar si ja és a LNT, ja ho fa el check al començar el bucle
                    LNO.push(fill);
                }

                // Actualitza màxim de memòria utilitzada
                rc.updateMemoria(LNO.size() + LNT.size());
            }

            // Si no hem trobat res, augmentem el límit i tornem a començar
            if (!trobat) {
                limit++;
            }
        }

        rc.stopTime();

        // Reconstruir el camí de solució
        if (solucio != null) {
            LinkedList<Moviment> cami = new LinkedList<>();
            Node node = solucio;
            while (node != null && node.accio != null) {
                cami.addFirst(node.accio);
                node = node.pare;
            }
            rc.setCami(cami);
        } else {
            rc.setCami(null);
        }
    }
}
