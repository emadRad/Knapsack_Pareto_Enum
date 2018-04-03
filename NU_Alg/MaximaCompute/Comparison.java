package NU_Alg.MaximaCompute;

import NU_Alg.NU_Core.Item;
import NU_Alg.NU_Core.ItemLabel;


public class Comparison {


    ItemLabel [] itemLabels;

    public Comparison(ItemLabel [] itemLabels){

        this.itemLabels= itemLabels;
    }

    //checks if the first vector dominates the second one or not
    /*
     * if first dominates second => true
     * else false
     * */
    public boolean dominates(Item first, Item second, int dimension) throws Exception {
        int equal_comp_num=0;

        for (int d=0;d<dimension;d++) {
            if ( first.getVector().getComponent(d) == second.getVector().getComponent(d) ){
                equal_comp_num++;
            }
            else {
                if(itemLabels[d]==ItemLabel.PROFIT) {
                    if (first.getVector().getComponent(d) < second.getVector().getComponent(d))
                        return false;
                }
                else if(itemLabels[d]==ItemLabel.WEIGHT){
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



    /*
         compare two vector up to the dimension d
         output:
            if v1 > v2 return (1 OR -1 if it is weight)
            if v2 > v1 return (-1 OR 1 if it is weight)
            if v1 = v2 return 0
        */
    public int partialCompare(Vector v1, Vector v2, int d) throws Exception {
        int k = 0;
        int return_v=0;
        while (k < d && v1.getComponent(k) == v2.getComponent(k))
            k++;
        if ( k == d )
            return 0;
        else {
            if (v1.getComponent(k) > v2.getComponent(k)) {
                return_v = itemLabels[k]==ItemLabel.WEIGHT ? -1 : 1;
                return return_v;
            }
            if (v1.getComponent(k) < v2.getComponent(k)){
                return_v = itemLabels[k]==ItemLabel.WEIGHT ? 1 : -1;
                return return_v;
            }
        }
        return 0;
    }


}
