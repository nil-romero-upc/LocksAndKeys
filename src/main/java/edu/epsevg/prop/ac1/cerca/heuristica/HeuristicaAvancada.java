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

        // Cache: si ja hem calculat aquesta heurística per aquest estat
        Integer val = cacheH.get(estat);
        if (val != null) return val;

        List<Posicio> agents = estat.getAgents();
        Posicio sortida = estat.getSortidaPosicio();
        List<Posicio> clausPendents = estat.getClausPendents();

        int resultat;

        // Cas 1: No hi ha claus pendents -> anar cap a la sortida
        if (clausPendents.isEmpty()) {
            resultat = minDistAgentsA(estat, agents, sortida);

        } else {
            // Cas 2: Hi ha claus pendents

            // Distància mínima agent -> clau
            int minAgentClau = Integer.MAX_VALUE;
            for (Posicio a : agents) {
                for (Posicio c : clausPendents) {
                    int d = distAmbMurs(estat, a, c);
                    if (d < minAgentClau) minAgentClau = d;
                }
            }

            // Distància mínima (des de qualsevol agent) fins la sortida
            int minAgentSortida = minDistAgentsA(estat, agents, sortida);

            // Penalització lleu per nombre de claus pendents
            int penalitzacioClaus = Math.max(0, clausPendents.size() - 1);

            resultat = minAgentClau + minAgentSortida + penalitzacioClaus;
        }

        cacheH.put(estat, resultat);
        return resultat;
    }

    // =========================================================================
    //  Funcions auxiliars
    // =========================================================================

    /**
     * Distància mínima (amb murs) entre qualsevol agent i una posició objectiu.
     */
    private int minDistAgentsA(Mapa estat, List<Posicio> agents, Posicio objectiu) {
        int min = Integer.MAX_VALUE;
        for (Posicio a : agents) {
            int d = distAmbMurs(estat, a, objectiu);
            if (d < min) min = d;
        }
        return min;
    }

    /**
     * Distància Manhattan + petita penalització segons murs trobats
     * al llarg d’un camí rectilini (primer en X, després en Y).
     *
     * És barata de calcular i té en compte la densitat de murs
     * entre l’agent i l’objectiu.
     */
    private int distAmbMurs(Mapa estat, Posicio a, Posicio b) {
        int man = manhattanCached(a, b);
        int extra = 0;

        int x = a.x;
        int y = a.y;

        int stepX = Integer.compare(b.x, a.x);
        int stepY = Integer.compare(b.y, a.y);

        // Moviment en X
        while (x != b.x) {
            x += stepX;
            if (estat.esParet(x, y)) {
                extra++;
                break;      // Només comptar una penalització
            }
        }

        // Moviment en Y
        while (y != b.y) {
            y += stepY;
            if (estat.esParet(x, y)) {
                extra++;
                break;
            }
        }

        return man + (extra == 0 ? 0 : 1);
    }

    /**
     * Distància Manhattan entre dues posicions amb cache.
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
