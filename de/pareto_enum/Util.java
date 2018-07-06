package de.pareto_enum;

import de.pareto_enum.maxima_compute.Label;
import de.pareto_enum.maxima_compute.Vector;
import de.pareto_enum.enum_core.DimLabel;
import de.pareto_enum.enum_core.Item;


import java.util.*;

/**
 * A utility class that contains miscellaneous methods.
 *
 * */
public class Util {


    /**
     * Sorting vectors lexicographically. It uses the sort from java Collections.
     * It implements the Comparator for the Vector class in order to compare the vectors.
     * @param vectors a list of vectors
     * @param dimLabels  an array of labels for each dimension
     *
     * */
    public void sortVectors(List<Vector> vectors, DimLabel [] dimLabels){
        final DimLabel [] dimLabel = dimLabels;
        Collections.sort(vectors, new Comparator<Vector>() {
            @Override
            public int compare(Vector o1, Vector o2) {
                int return_v=0;
                int k=0;
                try {
                    while( k<o1.getDimension() && o1.getComponent(k)==o2.getComponent(k))
                        k++;
                    if( k==o1.getDimension() )
                        return 0;
                    if(o1.getComponent(k) > o2.getComponent(k)) {
                        return_v = dimLabel[k]==DimLabel.WEIGHT ? -1 : 1;
                        return return_v;
                    }
                    if(o1.getComponent(k) < o2.getComponent(k)) {
                        return_v = dimLabel[k]==DimLabel.WEIGHT ? 1 : -1;
                        return return_v;
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                return return_v;
            }
        });
    }


    /**
     * Creates a linked list from the given List. The linked list is stored
     * in a List so it can also be accessed by the indices.
     * @param vectors a list of vectors(instances of Vector class)
     * @param linkedArray a list which stores the linked list
     * */
    public void createLinkedList(List<Vector> vectors, List<Item> linkedArray){

        Item node = null;

        Map<Item,Label> labels = new HashMap<>();

        for(int i=0;i<vectors.size();i++) {
            vectors.get(i).setRank(i);
            node = new Item(vectors.get(i),node);
            node.setLabel(Label.NULL);
            labels.put(node,Label.NULL);
            if(node.getNext()!=null){
                node.getNext().setPrevious(node);
            }
            linkedArray.add(node);
        }


    }

}
