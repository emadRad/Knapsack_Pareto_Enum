package NU_Alg.MaximaCompute;

import NU_Alg.NU_Core.Item;
import NU_Alg.NU_Core.ItemLabel;
import NU_Alg.MaximaCompute.Comparison;

import java.util.*;


public class FLET {

    ItemLabel [] itemLabels;
    Comparison compare;

    public FLET(ItemLabel [] itemLabels){
        this.itemLabels = itemLabels;
        compare = new Comparison(itemLabels);
    }


    /**
     * An implementation of the heuristic algorithm from Fast linear expected-time algorithms for computing maxima and convex hulls
     *    ({@link https://dl.acm.org/citation.cfm?id=320196}) paper.
     *
     * @param vectors list of input vectors for finding the maxima
     * @return the maxima of the input vectors
     * */
    public List<Item> find_maxima_FLET(List<Item> vectors) throws Exception {

        List<Item> maximals = new ArrayList<>();
        maximals.add(vectors.get(0));
        int j=0;
        int dim = vectors.get(0).getVector().getDimension();

        for (int i=1; i<vectors.size(); i++){
            j=0;
            while ( j < maximals.size() ){
                if (compare.dominates(maximals.get(j),vectors.get(i),dim)){
                    Collections.swap(maximals,0,j);
                    j = maximals.size()+1;
                }
                else if (compare.dominates(vectors.get(i),maximals.get(j),dim)){
                    maximals.remove(j);
                }
                //incomparable
                else{
                    j++;
                }
            }
            if ( j == maximals.size() )
                maximals.add(vectors.get(i));
        }
        return maximals;
    }

}
