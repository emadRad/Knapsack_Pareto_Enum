package de.pareto_enum.enum_core;

import de.pareto_enum.maxima_compute.FLET;
import de.pareto_enum.maxima_compute.Label;
import de.pareto_enum.maxima_compute.MaximaCompute;
import de.pareto_enum.maxima_compute.Vector;

import java.util.*;

/**
 * Created by emad on 06.06.17.
 */

/**
 * The multidimensional knapsack problem can be solved by using Nemhauser-Ullmann algorithm.
 * The key part of this algorithm is to compute the Pareto set after adding an item to the knapsack.
 * We use the Nemhauser-Ullmann algorithm to enumerate the Pareto sets.
 *
 * Reference: Discrete dynamic programming and capital allocation. George L. Nemhauser and Zev Ullmann
* */
public class EnumCore {
    private List<Item> items;
    private DimLabel [] dimLabels;

    public EnumCore(List<Item> items, DimLabel [] dimLabels){
        this.items = items;
        this.dimLabels = dimLabels;
    }

   /**
    * This method uses the Nemhauser-Ullmann algorithm in combination with multidimensional
    * divide and conquer(findMaxima) and FLET algorithm to enumerate the Pareto sets.
    * According to what is given for algorithm type three algorithm can be used for computing the Pareto set.
    * The three types of algorithm is is as follows:
    *   - MD&C  the Bentley's multidimensional divide and conquer algorithm
    *   - FLET  Fast Linear Expected Time algorithm
    *   - hybrid combination of the two algorithm
    *
    * @param algorithm a string that specifies the type of algorithm to use. It can be mdc, flet or hybrid.
    * @return an array which contains the statistics(runtime, average size of Pareto etc.)
    *   the full description is as follows:
    *       TotalTime
    *       AvgIterTime, average time of each iteration
    *       AvgParetoSize, average size of Pareto size
    *       MaxParetoSize, maximum size of Pareto size(max over all iterations)
    *       MinParetoSize, minimum size of Pareto size
    *       AvgSize, average size each unionSet(the input to the algorithm for computing Pareto set)
    *       MaxSize, max size of unionSet

    * */
    public long [] enumerator(String algorithm)throws Exception{

        /*
        * 0 : TotalTime
        * 1 : AvgIterTime, average time of each iteration
        * 2 : AvgParetoSize, average size of paretoSize
        * 3 : MaxParetoSize
        * 4 : MinParetoSize
        * 5 : AvgSize, average size each unionSet
        * 6 : MaxSize, max size of unionSet
        *
        * */
        long [] results = new long[7];

        /* currParetoSet : the pareto set computed from last iteration */
        List<Item> currParetoSet= new ArrayList<>();
        /* newItems : the set of item with item i added to curr_paretoSeet */
        List<Item> newItems ;

        /*union of currParetoSet and newItems*/
        List<Item> unionSet;

        int dimension;
        if(items.isEmpty())
            return results;
        else
            dimension = items.get(0).getVector().getDimension();


        currParetoSet.add(items.remove(0));

        Map<Item,Label> labels;

        MaximaCompute maximaCompute = new MaximaCompute(dimLabels);
        FLET flet = new FLET(dimLabels);

        // the sum of sizes union set in each iteration
        // it is used for computing the average
        int sizeSum = 0;

        int maxSize = -1;

        int minParetoSize = Integer.MAX_VALUE;
        int maxParetoSize=-1;
        int paretoSizeSum = 0;


        //timeout in millisecond to control the time
        // 6 hour
        double timeout = 2.16e+7;

        long start = System.nanoTime();
        // the starting time of iteration
        long iterStartTime;
        long iterTime=0;
        //for computing the average time of each iteration
        long iterTimeSum=0;
        long totalTime=0;
        long iterNum = 0;

        for(Item item: items){
            iterNum++;

            if(((System.nanoTime()-start)/1e6) > timeout) {
                System.out.println("\nTimeout reached at iteration "+ iterNum);
                System.out.println("Runtime(ms): "+(System.nanoTime() - start)/1e6);
                System.out.print("Average Pareto size "+paretoSizeSum/iterNum+"\n");
            }

            iterStartTime = System.nanoTime();

            newItems = addItemToList(currParetoSet,item);
            unionSet = merge(currParetoSet,newItems);

            insertInPosition(item,unionSet);

            labels = new HashMap<>();

            sizeSum += unionSet.size();
            if(unionSet.size()>maxSize)
                maxSize = unionSet.size();

            List<Item> maximals = null;

            if(algorithm.equals("mdc")) {
                init(unionSet,labels);

                // the max and min vectors int this list
                Vector minVector_comp0 = unionSet.get(unionSet.size() - 1).getVector();
                Vector maxVector_comp0 = unionSet.get(0).getVector();

                maximaCompute.setMaxVector_comp0(maxVector_comp0);
                maximaCompute.setMinVector_comp0(minVector_comp0);

                if (dimension > 3)
                    //starting from last dimension,dimension-1 is the index for last dimension
                    maximals = maximaCompute.findMaximaMDC(unionSet, dimension - 1, labels);
                else if (dimension == 3) {
                    maximals = maximaCompute.find_maxima_base3(unionSet);
                }
                currParetoSet = findSortedOrder(maximals,maxVector_comp0,minVector_comp0);
            }
            else if(algorithm.equals("flet")){
                maximals = flet.findMaximaFLET(unionSet);
                currParetoSet = maximals;
            }

            if(maximals.size() > maxParetoSize )
                maxParetoSize = maximals.size();
            paretoSizeSum += maximals.size();
            if(maximals.size()< minParetoSize)
                minParetoSize = maximals.size();

            iterTime = System.nanoTime()-iterStartTime;
            iterTimeSum += iterTime;

        }


        long averageIterTime = iterTimeSum/items.size();
        int averageSize = sizeSum/items.size();
        int avgParetoSize = paretoSizeSum/items.size();
        totalTime = System.nanoTime() - start;

        //nanoseconds to milliseconds
        totalTime /= 1e6;
        averageIterTime /=1e6;

        /*
        * 0 : TotalTime
        * 1 : AvgIterTime, average time of each iteration
        * 2 : AvgParetoSize, average size of paretoSize
        * 3 : MaxParetoSize
        * 4 : MinParetoSize
        * 5 : AvgSize, average size each unionSet
        * 6 : MaxSize, max size of unionSet
        *
        * */
        results[0] = totalTime;
        results[1] = averageIterTime;
        results[2] = avgParetoSize;
        results[3] = maxParetoSize;
        results[4] = minParetoSize;
        results[5] = averageSize;
        results[6] = maxSize;

//        System.out.print(","+currParetoSet.size()+","+ averageSize+","+maxSize+"\n");
        return results;
    }




    /**
     * Sorting the list according to sorted linked list embedded inside each item
     * @param list
     * @param maxVector the maximum vector in list
     * @param minVector the minimum vector in list
     * @return the sorted list in descending order
     * */
    public List<Item> findSortedOrder(List<Item> list, Vector maxVector, Vector minVector){

        Item currItem;
        int compareTo;
        int startNode_index=0;
        int endNode_index=0;
        int c1=0;
        int c2=0;
        List<Item> newList = new ArrayList<>();


        Vector tmp = maxVector;
        maxVector = minVector;
        minVector = tmp;

        for(int i=0;i<list.size();i++) {
            currItem = list.get(i);

            c1++;

            currItem.setACTIVE(true);

            compareTo = currItem.getVector().compareTo(maxVector);
            if ( compareTo == 1 ){ //
                maxVector = currItem.getVector();
                startNode_index = i;
            }
            else{
                // when two vector are equal it is needed to check
                // whether the maxVector_sublist points to the curr_item or the other way around
                if ( compareTo == 0 ){
                    if ( currItem.getVector().getRank() > maxVector.getRank()  ){
                        maxVector = currItem.getVector();
                        startNode_index = i;
                    }
                    else if(currItem.getVector().getRank() == maxVector.getRank()){
                        maxVector = currItem.getVector();
                        startNode_index = i;
                    }
                }

            }

            compareTo =  currItem.getVector().compareTo(minVector);
            if ( compareTo == -1 ) {
                minVector = currItem.getVector();
                endNode_index = i;
            }
            else if( compareTo == 0 ){
                if ( currItem.getVector().getRank() < minVector.getRank()  ){
                    minVector = currItem.getVector();
                    endNode_index = i;

                }
                else if (currItem.getVector().getRank() == minVector.getRank()){
                    minVector = currItem.getVector();
                    endNode_index = i;
                }
            }

        }

        currItem = list.get(startNode_index) ;
        Item endItem = list.get(endNode_index);
        endItem = endItem.getNext();


        while(currItem != endItem && currItem != null){
            if(currItem.isACTIVE()) {
                c2++;
                newList.add(currItem);
            }
            currItem.setACTIVE(false);
            currItem = currItem.getNext();
        }

        if(c1!=c2){
            System.err.println("Not equal");
            System.exit(1);
        }


        return newList;

    }


    /**
     * Adds the {@code newItem } to the all elements of {@code list}
     *
     * @param list a list of items
     * @param newItem the item to be added
     * @return a new list of items with item added to each element
    *
    * */
    private List<Item> addItemToList(List<Item> list, Item newItem) throws Exception {
        List<Item> items = new ArrayList<>();
        for(Item item: list){
            items.add(vectorAdd(item, newItem));
        }
        return items;
    }

    /**
     * Add two vectors component by component
     * @param item1 first item
     * @param item2 second item
     * @return new item from adding item1 and item2
     *
     * */

    private Item vectorAdd(Item item1, Item item2) throws Exception {
        int dim = item1.getVector().getDimension();
        double [] list = new double[dim];

        for(int i=0; i < dim; i++){
            list[i] = item1.getVector().getComponent(i) + item2.getVector().getComponent(i);
        }
        Vector vect = new Vector(dim,list);
        Item newItem = new Item(vect,null);

        return newItem;
    }

    /**
     * Creates a linked list from the given list and also initializes
     * some variables for items, and initializes the label map
     * @param list
     * @param labels
     *
     * */
    public void init(List<Item> list, Map<Item,Label> labels){
        Item item;
        Item next=null;

        int listSize = list.size();

        //from smallest to biggest
        for(int i = listSize-1 ; i>=0 ; i--){
            item = list.get(i);
            item.setNext(next);
            next = item;

            item.getVector().setRank(listSize-i);
            item.setLabel(Label.NULL);
            labels.put(item,Label.NULL);
            if(item.getNext()!=null){
                item.getNext().setPrevious(item);
            }
        }
    }


    /**
    *  Merge two list
     * @param list1
     * @param list2
     * @return sorted list resulted from merging {@code list1} and {@code list2}
    * */
    private List<Item> merge(List<Item> list1, List<Item> list2){
        int size1=list1.size();
        int size2=list2.size();
        int i=0;
        int j=0;

        Item item1;
        Item item2;
        List<Item> merged = new ArrayList<>();
        int compareVal;

        while( i<size1 && j<size2 ){
            item1 = list1.get(i);
            item2 = list2.get(j);

            compareVal = item1.getVector().compareTo(item2.getVector());
            if(compareVal==1){
                merged.add(item1);
                i++;
            }
            else{
                if(compareVal!=0){
                    merged.add(item2);
                    j++;
                }
                else {
                    if(item1.getVector().getRank() > item2.getVector().getRank()){
                        merged.add(item1);
                        i++;
                    }
                    else{
                        merged.add(item2);
                        j++;
                    }
                }
            }
        }
        while(i<size1){
            merged.add(list1.get(i));
            i++;
        }
        while(j<size2){
            merged.add(list2.get(j));
            j++;
        }

        return merged;
    }




    /**
     * Insert the item in its position in sorted list
     * @param item
     * @param list , sorted list
     * */
    public void insertInPosition(Item item, List<Item> list){
        int size = list.size();
        Item tmp;
        if(size!=0)
            tmp = list.get(0);
        else{
            list.add(item);
            return;
        }
        int i=1;
        while(i<size && tmp.getVector().compareTo(item.getVector())==1){
            tmp = list.get(i);
            i++;
        }
        list.add(i,item);
    }

}
