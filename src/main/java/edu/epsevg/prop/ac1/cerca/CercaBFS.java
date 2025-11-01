package edu.epsevg.prop.ac1.cerca;

import edu.epsevg.prop.ac1.model.*;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class CercaBFS extends Cerca {
    public CercaBFS(boolean usarLNT) { super(usarLNT); }

    @Override
    public void ferCerca(Mapa inicial, ResultatCerca rc) {
        Node Na = null;
        Queue<Node> LNO = new LinkedList<>();
        //List<Node> LF = new ArrayList<>();
        Map<Mapa,Integer> LNT = new HashMap<>();
        
        LNO.add(new Node(inicial,null,null,0,0));
        boolean sortida = false;

        while ( !(LNO.isEmpty() || sortida) ) {
            Na = LNO.poll();
            for( Moviment mov : Na.estat.getAccionsPossibles() ) {
                 
            }
        }
    }
   
}
