package edu.epsevg.prop.ac1.cerca;
 
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.LinkedList;

import edu.epsevg.prop.ac1.model.*;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;

public class CercaDFS extends Cerca {
    public CercaDFS(boolean usarLNT) { super(usarLNT); }

    @Override
    public void ferCerca(Mapa inicial, ResultatCerca rc) {
        rc.startTime();

        // --- Inicialitzacions ---
        Deque<Node> LNO = new ArrayDeque<>();           // Llista de Nodes Oberts (LIFO)
        ControlCicles cc = new ControlCicles(usarLNT); // Control de cicles

        // Node inicial
        Node arrel = new Node(inicial, null, null, 0, 0);
        LNO.addFirst(arrel);

        // --- Bucle principal ---
        while (!LNO.isEmpty()) {
            Node actual = LNO.removeFirst(); // Traiem el més recent (profunditat màxima)
            rc.incNodesExplorats();

            // Comprovem si és la meta
            if (actual.estat.esMeta()) {
                rc.setCami(Cerca.reconstruirCami(actual));
                rc.stopTime();
                return;
            }

            // Si arribem al límit de profunditat, no expandim més
            if (actual.depth >= 50) {
                rc.incNodesTallats();
                continue;
            }

            // Generem successors (en qualsevol ordre determinista)
            for (Moviment mov : actual.estat.getAccionsPossibles()) {
                Mapa nouEstat = actual.estat.mou(mov);
                Node successor = new Node(nouEstat, actual, mov, actual.depth + 1, actual.g + 1);

                // Control de cicles segons la configuració
                if (!cc.esRepetit(actual, nouEstat, successor.depth)) {
                    LNO.addFirst(successor);
                } else {
                    rc.incNodesTallats();
                }
            }

            // Actualitzem memòria màxima utilitzada
            rc.updateMemoria(LNO.size() + cc.mida());
        }

        // Si acabem el bucle sense trobar meta, no hi ha solució
        rc.stopTime();
    }
}
