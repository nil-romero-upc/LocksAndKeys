package edu.epsevg.prop.ac1.cerca.heuristica;

import edu.epsevg.prop.ac1.model.Mapa;
import edu.epsevg.prop.ac1.model.Posicio;

import java.util.*;

/**
 * Heuristica avançada: Al vostre gust ;-)
 */
public class HeuristicaAvancada implements Heuristica {
    
    // Cache per accelerar càlculs de distància Manhattan
    private static final Map<String, Integer> cacheDist = new HashMap<>();

    // Cache global de valors heurístics per estat
    private static final Map<Mapa, Integer> cacheH = new HashMap<>();

    @Override
    public int h(Mapa estat) {
        // Cas directe: ja hem arribat a la meta
        if (estat.esMeta()) return 0;

        // Comprovem si ja hem calculat aquesta heurística
        Integer val = cacheH.get(estat);
        if (val != null) return val;

        List<Posicio> agents = estat.getAgents();
        Posicio sortida = estat.getSortidaPosicio();
        List<Posicio> clausPendents = estat.getClausPendents();

        int resultat;

        if (clausPendents.isEmpty()) {
            // No queden claus -> distància mínima agent->sortida
            int minDist = Integer.MAX_VALUE;
            for (Posicio a : agents) {
                int dist = manhattanCached(a, sortida);
                if (dist < minDist) minDist = dist;
            }
            resultat = minDist;
        } else {
            // Queden claus -> estimem: agent->clau + clau->sortida
            int minAgentClau = Integer.MAX_VALUE;
            int minClauSortida = Integer.MAX_VALUE;

            // distància mínima agent->clau
            for (Posicio a : agents) {
                for (Posicio c : clausPendents) {
                    int dist = manhattanCached(a, c);
                    if (dist < minAgentClau) minAgentClau = dist;
                }
            }

            // distància mínima clau->sortida
            for (Posicio c : clausPendents) {
                int dist = manhattanCached(c, sortida);
                if (dist < minClauSortida) minClauSortida = dist;
            }

            // penalització lleu per claus pendents
            resultat = minAgentClau + minClauSortida + Math.max(0, clausPendents.size() - 1);
        }

        // Guardem a la cache
        cacheH.put(estat, resultat);

        return resultat;
    }

    /**
     * Retorna la distància Manhattan entre dues posicions amb cache per millorar el rendiment.
     */
    private int manhattanCached(Posicio a, Posicio b) {
        String key = (a.x <= b.x)
                ? a.x + "," + a.y + "-" + b.x + "," + b.y
                : b.x + "," + b.y + "-" + a.x + "," + a.y;
        Integer dist = cacheDist.get(key);
        if (dist == null) {
            dist = Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
            cacheDist.put(key, dist);
        }
        return dist;
    }  
}
