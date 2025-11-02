package edu.epsevg.prop.ac1.cerca;
 
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.LinkedList;

import edu.epsevg.prop.ac1.model.*;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;

public class CercaDFS extends Cerca {
    public CercaDFS(boolean usarLNT) { super(usarLNT); }

    @Override
    public void ferCerca(Mapa inicial, ResultatCerca rc) {
        Deque<Node> LNO = new ArrayDeque<>();  // pila de nodes oberts
        Set<Mapa> LNT = new HashSet<>();       // conjunts d’estats ja explorats (si usarLNT = true)

        Node arrel = new Node(inicial, null, null, 0, 0);
        LNO.push(arrel);

        boolean trobat = false;
        Node actual = null;

        rc.startTime();

        while (!LNO.isEmpty() && !trobat) {
            actual = LNO.pop();
            rc.incNodesExplorats();

            rc.updateMemoria(LNO.size() + LNT.size());

            // Si és la meta, acabem
            if (actual.estat.esMeta()) {
                trobat = true;
                break;
            }

            // Si ja hem visitat aquest estat
            if (this.usarLNT && LNT.contains(actual.estat)) {
                rc.incNodesTallats();
                continue;
            }

            // Afegim l’estat a LNT
            if (this.usarLNT) LNT.add(actual.estat);

            // Generar successors
            List<Moviment> accions = actual.estat.getAccionsPossibles();
            Collections.reverse(accions); // per mantenir un ordre lògic de visita

            for (Moviment mv : accions) {
                try {
                    Mapa nou = actual.estat.mou(mv);
                    if (this.usarLNT && LNT.contains(nou)) {
                        rc.incNodesTallats();
                        continue;
                    }

                    Node fill = new Node(nou, actual, mv, actual.depth + 1, 0);
                    LNO.push(fill);
                } catch (Exception e) {
                    // moviment invàlid
                    rc.incNodesTallats();
                }
            }
        }

        rc.stopTime();

        if (trobat) {
            // reconstruïm el camí manualment sense cridar cap mètode extern
            LinkedList<Moviment> cami = new LinkedList<>();
            Node node = actual;
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
