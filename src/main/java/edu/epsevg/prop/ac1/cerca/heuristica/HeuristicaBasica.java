package edu.epsevg.prop.ac1.cerca.heuristica;

import java.util.ArrayList;
import java.util.List;

import edu.epsevg.prop.ac1.model.Mapa;
import edu.epsevg.prop.ac1.model.Posicio;


/** 
 * Distància de Manhattan a la clau més propera 
 * (si queden per recollir) o a la sortida.
 */
public class HeuristicaBasica implements Heuristica {
    @Override
    public int h(Mapa estat) {
        // Si ja som a la meta
        if (estat.esMeta()) return 0;

        // Obtenim informació necessària
        List<Posicio> agents = estat.getAgents();
        Posicio sortida = estat.getSortidaPosicio();
        List<Posicio> clausPendents = estat.getClausPendents();

        int minDist = Integer.MAX_VALUE;

        if (!clausPendents.isEmpty()) {
            // Hi ha claus pendents: distància mínima agent -> clau
            for (Posicio agent : agents) {
                for (Posicio clau : clausPendents) {
                    int dist = Math.abs(agent.x - clau.x) + Math.abs(agent.y - clau.y);
                    if (dist < minDist) minDist = dist;
                }
            }
        } else {
            // Totes les claus recollides: distància mínima agent -> sortida
            for (Posicio agent : agents) {
                int dist = Math.abs(agent.x - sortida.x) + Math.abs(agent.y - sortida.y);
                if (dist < minDist) minDist = dist;
            }
        }

        return minDist;
    }
}
