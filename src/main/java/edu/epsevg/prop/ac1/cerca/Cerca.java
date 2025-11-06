package edu.epsevg.prop.ac1.cerca;

import java.util.LinkedList;
import java.util.List;

import edu.epsevg.prop.ac1.model.Mapa;
import edu.epsevg.prop.ac1.model.Moviment;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;
import edu.epsevg.prop.ac1.cerca.Node;

public abstract class Cerca {
    protected final boolean usarLNT;

    public Cerca(boolean usarLNT) {
        this.usarLNT = usarLNT;
    }

    public abstract void ferCerca(Mapa inicial, ResultatCerca rc);

    /**
    * Reconstrueix el camí (llista de moviments) des de la meta fins a l'estat inicial.
    * Es pot usar des de qualsevol algorisme de cerca.
    * 
    * @param nodeFinal Node que arriba a la meta
    * @return Llista de moviments des de l'inici fins a la meta
    */
    public static List<Moviment> reconstruirCami(Node nodeFinal) {
        LinkedList<Moviment> cami = new LinkedList<>();
        Node n = nodeFinal;

        // Recórrer de la meta cap enrere fins a l'arrel
        while (n != null && n.accio != null) {
            cami.addFirst(n.accio);
            n = n.pare;
        }
        return cami;
    }
}
