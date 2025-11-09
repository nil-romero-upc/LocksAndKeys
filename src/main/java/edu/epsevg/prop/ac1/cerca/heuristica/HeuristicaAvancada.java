package edu.epsevg.prop.ac1.cerca.heuristica;

import edu.epsevg.prop.ac1.model.Direccio;
import edu.epsevg.prop.ac1.model.Mapa;
import edu.epsevg.prop.ac1.model.Posicio;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Heuristica avançada: Al vostre gust ;-)
 */
public class HeuristicaAvancada implements Heuristica {
    
    // Cache per accelerar càlculs de distància Manhattan
    private static final Map<String, Integer> cacheDist = new HashMap<>();

    // Cache global d’heurístiques per estat (opcional)
    private static final Map<Mapa, Integer> cacheH = new HashMap<>();

    @Override
    public int h(Mapa estat) {
        // Si ja hem arribat a la meta
        if (estat.esMeta()) return 0;

        // Cache: si ja hem calculat aquesta heurística
        Integer val = cacheH.get(estat);
        if (val != null) return val;

        List<Posicio> agents = estat.getAgents();
        Posicio sortida = estat.getSortidaPosicio();
        List<Posicio> clausPendents = estat.getClausPendents();

        int resultat;

        // Cas 1: No hi ha claus pendents -> anar cap a la sortida
        if (clausPendents.isEmpty()) {
            resultat = minDistAgentsA(agents, sortida);

        } else {
            // Cas 2: Hi ha claus pendents

            // Distància mitjana agent->clau (multi-agent)
            int sumDist = 0;
            for (Posicio a : agents) {
                int millor = Integer.MAX_VALUE;
                for (Posicio c : clausPendents) {
                    int d = manhattanCached(a, c);
                    if (d < millor) millor = d;
                }
                sumDist += millor;
            }
            int distAgentsAClaus = sumDist / agents.size();

            // Distància mitjana agent->sortida
            int distSortida = 0;
            for (Posicio a : agents)
                distSortida += manhattanCached(a, sortida);
            distSortida /= agents.size();

            // Penalització per claus massa llunyanes
            int penalitzacio = 0;
            for (Posicio c : clausPendents) {
                int minDist = Integer.MAX_VALUE;
                for (Posicio a : agents)
                    minDist = Math.min(minDist, manhattanCached(a, c));
                if (minDist > 20) penalitzacio += 50; // clau molt llunyana -> penalitzem
            }

            // Fórmula final de l’heurística
            resultat = distAgentsAClaus                 // agents equilibrats
                     + (distSortida / 2)                // reforç de retorn
                     + Math.max(0, clausPendents.size() - 1)
                     + penalitzacio;                    // claus llunyanes
        }

        cacheH.put(estat, resultat);
        return resultat;
    }

    /**
     * Retorna la distància Manhattan entre dues posicions amb cache.
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

    /**
     * Distància mínima entre qualsevol agent i una posició objectiu.
     */
    private int minDistAgentsA(List<Posicio> agents, Posicio objectiu) {
        int min = Integer.MAX_VALUE;
        for (Posicio a : agents) {
            int d = manhattanCached(a, objectiu);
            if (d < min) min = d;
        }
        return min;
    }
}
