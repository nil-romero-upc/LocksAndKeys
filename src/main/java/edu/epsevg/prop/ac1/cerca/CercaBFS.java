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
        Queue<Node> LNO = new LinkedList<>();       // Nodes oberts
        Map<Mapa, Integer> LNT = new HashMap<>();   // Nodes tancats

        Node inicialNode = new Node(inicial, null, null, 0, 0);
        LNO.add(inicialNode);

        Node actual = null;
        boolean trobat = false;

        rc.startTime();

        while (!LNO.isEmpty() && !trobat) {
            actual = LNO.poll();
            rc.incNodesExplorats();

            // Actualitzar ús de memòria (nombre total de nodes en memòria)
            rc.updateMemoria(LNO.size() + LNT.size());

            // Comprovar si és estat meta
            if (actual.estat.esMeta()) {
                trobat = true;
                break;
            }

            // Evitar repetir estats
            if (LNT.containsKey(actual.estat)) continue;
            LNT.put(actual.estat, actual.depth);

            // Generar successors
            for (Moviment mov : actual.estat.getAccionsPossibles()) {
                try {
                    Mapa nouEstat = actual.estat.mou(mov);
                    if (!LNT.containsKey(nouEstat)) {
                        Node fill = new Node(nouEstat, actual, mov, actual.depth + 1, 0);
                        LNO.add(fill);
                    }
                } catch (Exception e) {
                    // Moviment invàlid → no cal fer res
                    rc.incNodesTallats();
                }
            }
        }

        rc.stopTime();

        // Informar del resultat
        if (trobat) {
            // reconstruir el camí manualment
            LinkedList<Moviment> cami = new LinkedList<>();
            Node node = actual;
            while (node != null && node.accio != null) {
                cami.addFirst(node.accio);
                node = node.pare;
            }
            rc.setCami(cami);
        } else {
            rc.setCami(null); // sense solució
        }
    }
    
   
}
