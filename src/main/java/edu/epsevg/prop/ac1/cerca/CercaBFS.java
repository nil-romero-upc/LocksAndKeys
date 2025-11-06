package edu.epsevg.prop.ac1.cerca;

import edu.epsevg.prop.ac1.model.*;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class CercaBFS extends Cerca {
    public CercaBFS(boolean usarLNT) { super(usarLNT); }

    @Override
    public void ferCerca(Mapa inicial, ResultatCerca rc) {
        rc.startTime();

        // --- Inicialitzacions ---
        Queue<Node> LNO = new LinkedList<>();   // Llista de Nodes Oberts (FIFO)
        ControlCicles cc = new ControlCicles(usarLNT); // Control de cicles

        // Afegim el node inicial
        Node arrel = new Node(inicial, null, null, 0, 0);
        LNO.add(arrel);

        // --- Bucle principal ---
        while (!LNO.isEmpty()) {
            Node actual = LNO.poll(); // extreure el primer (nivell més antic)
            rc.incNodesExplorats();

            // Comprovem si hem arribat a la meta
            if (actual.estat.esMeta()) {
                rc.setCami(Cerca.reconstruirCami(actual));
                rc.stopTime();
                return;
            }

            // Generem successors
            for (Moviment mov : actual.estat.getAccionsPossibles()) {
                Mapa nouEstat = actual.estat.mou(mov);
                Node successor = new Node(nouEstat, actual, mov, actual.depth + 1, actual.g + 1);

                // Evitem estats repetits segons la política de cicles
                if (!cc.esRepetit(actual, nouEstat, successor.depth)) {
                    LNO.add(successor);
                } else {
                    rc.incNodesTallats(); // comptem com a node tallat
                }
            }

            // Actualitzem memòria màxima usada
            rc.updateMemoria(LNO.size() + cc.mida());
        }

        // Si arribem aquí, no hi ha solució
        rc.stopTime();
    }
}
