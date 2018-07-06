package de.pareto_enum.maxima_compute;

import de.pareto_enum.enum_core.Item;
import de.pareto_enum.enum_core.DimLabel;

/**
 * This class provides a set of methods for vector comparison.
 *
 * */
public class Comparison {


    DimLabel [] dimLabels;

    public Comparison(DimLabel [] dimLabels){

        this.dimLabels= dimLabels;
    }

    /**
     * Checks if the first vector dominates the second one or not
     * @param first the first item to be checked
     * @param second the second item to be checked
     * @param dimension the dimension of items
     * @return if first dominates second => true </br>
     *         else false
     * */
    public boolean dominates(Item first, Item second, int dimension) throws Exception {
        int equal_comp_num=0;

        for (int d=0;d<dimension;d++) {
            if ( first.getVector().getComponent(d) == second.getVector().getComponent(d) ){
                equal_comp_num++;
            }
            else {
                if(dimLabels[d]==DimLabel.PROFIT) {
                    if (first.getVector().getComponent(d) < second.getVector().getComponent(d))
                        return false;
                }
                else if(dimLabels[d]==DimLabel.WEIGHT){
                    if (first.getVector().getComponent(d) > second.getVector().getComponent(d))
                        return false;

                }
            }
        }
        if(equal_comp_num==dimension) {
            return false;
        }
        return true;
    }



    /**
     *
     * Compares two vectors up to the d dimension.
     * @param v1 the first vector
     * @param v2 the second vector
     * @param d dimension of vectors
     * @return
     *       if v1 > v2 return (1 OR -1 if it is weight)</br>
     *       if v2 > v1 return (-1 OR 1 if it is weight)</br>
     *       if v1 = v2 return 0
     *
     * */
    public int partialCompare(Vector v1, Vector v2, int d) throws Exception {
        int k = 0;
        int return_v=0;
        while (k < d && v1.getComponent(k) == v2.getComponent(k))
            k++;
        if ( k == d )
            return 0;
        else {
            if (v1.getComponent(k) > v2.getComponent(k)) {
                return_v = dimLabels[k]==DimLabel.WEIGHT ? -1 : 1;
                return return_v;
            }
            if (v1.getComponent(k) < v2.getComponent(k)){
                return_v = dimLabels[k]==DimLabel.WEIGHT ? 1 : -1;
                return return_v;
            }
        }
        return 0;
    }


}
