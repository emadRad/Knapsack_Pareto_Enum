package NU_Alg.NU_Core;

import NU_Alg.MaximaCompute.Label;
import NU_Alg.MaximaCompute.MaximaCompute;
import NU_Alg.MaximaCompute.Vector;
import NU_Alg.MaximaCompute.FLET;

import java.util.*;

/**
 * Created by emad on 06.06.17.
 */

/*
* An implementation of the Nemhauser-Ullmann algorithm for solving
*  multi-dimensional knapsack problem
* */
public class NU_Core {
    private List<Item> items;
    private ItemLabel [] itemLabels;

    public NU_Core(List<Item> items, ItemLabel [] itemLabels){
        this.items = items;
        this.itemLabels = itemLabels;
    }

   /**
    * The main method that runs the Nemhauser-Ullmann algorithm
    *
   * */
    public void solve()throws Exception{

        /* currParetoSet : the pareto set computed from last iteration */
        List<Item> currParetoSet= new ArrayList<>();
        /* newItems : the set of item with item i added to curr_paretoSeet */
        List<Item> newItems ;

        /*union of currParetoSet and newItems*/
        List<Item> unionSet;

        int dimension;
        if(items.isEmpty())
            return;
        else
            dimension = items.get(0).getVector().getDimension();


        System.out.print(items.size()+","+dimension);

        currParetoSet.add(items.remove(0));

        Map<Item,Label> labels;

        MaximaCompute maximaCompute = new MaximaCompute(itemLabels);
        FLET flet = new FLET(itemLabels);

        // the sum of sizes union set in each iteration
        // it is used for computing the average
        int sizeSum = 0;

        int maxSize = -1;

        /*
         The threshold according to which the algorithm will be changed
         from FLET(Fast Linear Expected-Time) algorithm to
         multi-dimensional divide and conquer(D&C) algorithm
         after 50000 it is going to use D&C
        */
        int threshold = 50000;
//        int threshold = 0;

        // a timeout in millisecond to control the time
        // 60 min
        int timeout = 3600000;

        long start = System.currentTimeMillis();

        // the starting time of iteration
        long iterStartTime;
        long iterTime=0;

        //for computing the average time of each iteration
        long iterTimeSum=0;

        //number of iteration
        int iterNum=0;

        for(Item item: items){
            iterNum++;

            if(System.currentTimeMillis()-start > timeout) {
                System.out.println("\nTimeout reached at iteration "+ iterNum);
                System.out.print(","+(System.currentTimeMillis() - start)+","+iterTimeSum/iterNum);
                System.out.print(","+currParetoSet.size()+","+ sizeSum/iterNum+","+maxSize+"\n");
            }

            iterStartTime = System.currentTimeMillis();

            newItems = addItemToList(currParetoSet,item);
            unionSet = merge(currParetoSet,newItems);

            //TODO this function is not needed, since `item` is always
            // the smallest element among current element of unionSet
            insertInPosition(item,unionSet);

            labels = new HashMap<>();

            sizeSum += unionSet.size();
            if(unionSet.size()>maxSize)
                maxSize = unionSet.size();

            List<Item> maximals = null;

            if(unionSet.size()>threshold) {
                init(unionSet,labels);

                // the max and min vectors int this list
                Vector minVector_comp0 = unionSet.get(unionSet.size() - 1).getVector();
                Vector maxVector_comp0 = unionSet.get(0).getVector();

                maximaCompute.setMaxVector_comp0(maxVector_comp0);
                maximaCompute.setMinVector_comp0(minVector_comp0);

                if (dimension > 3)
                    //starting from last dimension,dimension-1 is the index for last dimension
                    maximals = maximaCompute.find_maxima(unionSet, dimension - 1, labels);
                else if (dimension == 3) {
                    maximals = maximaCompute.find_maxima_base3(unionSet);
                }
                currParetoSet = findSortedOrder(maximals,maxVector_comp0,minVector_comp0);
            }
            else{
                maximals = flet.find_maxima_FLET(unionSet);
                currParetoSet = maximals;

                // the size next unionSet would be at most 2*size of current pareto set
                if(2 * currParetoSet.size() >= threshold)
                    Collections.sort(currParetoSet,Collections.reverseOrder());
            }

            iterTime = System.currentTimeMillis()-iterStartTime;
            iterTimeSum += iterTime;

        }

        long averageIterTime = iterTimeSum/items.size();
        System.out.print(","+(System.currentTimeMillis() - start)+","+averageIterTime);
        int averageSize = sizeSum/items.size();

        System.out.print(","+currParetoSet.size()+","+ averageSize+","+maxSize+"\n");
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
