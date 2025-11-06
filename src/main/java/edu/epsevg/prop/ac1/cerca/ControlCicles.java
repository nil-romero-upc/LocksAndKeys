package edu.epsevg.prop.ac1.cerca;

import edu.epsevg.prop.ac1.model.Mapa;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe auxiliar per gestionar el control de cicles.
 * 
 * Pot treballar en dos modes segons el valor de 'usarLNT':
 *  - usarLNT = false → Control local dins de la branca actual (via recorregut dels pares)
 *  - usarLNT = true  → Control global amb una Llista de Nodes Tancats (LNT)
 */
public class ControlCicles {
    private final boolean usarLNT;
    private final Map<Mapa, Integer> LNT; // guarda profunditats dels estats explorats

    /**
     * Constructor principal
     * @param usarLNT true per control global, false per control local
     */
    public ControlCicles(boolean usarLNT) {
        this.usarLNT = usarLNT;
        this.LNT = usarLNT ? new HashMap<>() : null;
    }

    /**
     * Comprova si un estat ja s'ha visitat.
     * @param actual node actual des d'on generem successors
     * @param estat estat nou a comprovar
     * @param depth profunditat del nou node
     * @return true si l'estat ja s'ha vist i no cal expandir-lo
     */
    public boolean esRepetit(Node actual, Mapa estat, int depth) {
        if (usarLNT) {
            // CONTROL GLOBAL: usem la LNT
            Integer oldDepth = LNT.get(estat);
            if (oldDepth == null || depth < oldDepth) {
                // És un estat nou o trobat amb menys profunditat → el guardem
                LNT.put(estat, depth);
                return false;
            }
            // Ja l'hem vist amb igual o menor profunditat
            return true;
        } else {
            // CONTROL LOCAL: comprovem si ja existeix dins la branca
            Node aux = actual;
            while (aux != null) {
                if (aux.estat.equals(estat)) return true;
                aux = aux.pare;
            }
            return false;
        }
    }

    /**
     * @return Mida de la LNT (només si usarLNT = true)
     */
    public int mida() {
        return usarLNT ? LNT.size() : 0;
    }

    /**
     * Esborra el contingut de la LNT (si s'està usant control global).
     */
    public void neteja() {
        if (usarLNT) LNT.clear();
    }
}
