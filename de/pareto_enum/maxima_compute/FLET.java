package de.pareto_enum.maxima_compute;

import de.pareto_enum.enum_core.Item;
import de.pareto_enum.enum_core.DimLabel;

import java.util.*;

/**
 * This class implements a heuristic algorithm from
 *  <a href="https://dl.acm.org/citation.cfm?id=320196">Fast linear expected-time algorithms for computing maxima and convex hulls</a>paper.
 *
 * This algorithm has an average-running time of O(dn) for a list of n d-dimensional vectors.
 * Maxima is the set of Pareto optimal solutions.
 *
 * @author  <a href="mailto:emadbahramirad@gmail.com">Emad Bahrami Rad</a>
 *
 * */
public class FLET {

    DimLabel [] dimLabels;
    Comparison compare;

    public FLET(DimLabel [] dimLabels){
        this.dimLabels = dimLabels;
        compare = new Comparison(dimLabels);
    }


    /**
     * Computes the maxima of the given list of items.
     * @param vectors a list of input vectors for finding the maxima
     * @return the maxima of the input vectors in a list
     * */
    public List<Item> findMaximaFLET(List<Item> vectors) throws Exception {

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
