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

        // Definim LNO (frontera): PriorityQueue ordenada per f = g + h
        PriorityQueue<Node> LNO = new PriorityQueue<>(
            Comparator.comparingInt(n -> n.g + heur.h(n.estat))
        );

        // LNT: map per registrar el cost més baix trobat per a cada estat
        HashMap<Mapa, Integer> LNT = new HashMap<>();

        // Node inicial
        Node inicialNode = new Node(inicial, null, null, 0, 0);
        LNO.add(inicialNode);
        LNT.put(inicial, 0);

        Node solucio = null;
        boolean trobat = false;

        // Bucle principal de cerca
        while (!LNO.isEmpty() && !trobat) {
            Node actual = LNO.poll();
            rc.incNodesExplorats();

            // Si hem arribat a la meta
            if (actual.estat.esMeta()) {
                solucio = actual;
                trobat = true;
                break;
            }

            // Generar successors
            for (Moviment mov : actual.estat.getAccionsPossibles()) {
                Mapa nouEstat = actual.estat.mou(mov);
                int nouCost = actual.g + 1; // cada moviment costa 1

                // Si no l’hem vist mai o hem trobat un camí millor
                if (!LNT.containsKey(nouEstat) || nouCost < LNT.get(nouEstat)) {
                    LNT.put(nouEstat, nouCost);
                    Node fill = new Node(nouEstat, actual, mov, actual.depth + 1, nouCost);
                    LNO.add(fill);
                } else {
                    rc.incNodesTallats();
                }
            }

            rc.updateMemoria(LNO.size() + LNT.size());
        }

        rc.stopTime();

        // Reconstruir el camí si trobem solució
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
