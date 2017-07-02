package NU_Alg.NU_Core;

import NU_Alg.MaximaCompute.Label;
import NU_Alg.MaximaCompute.MaximaCompute;
import NU_Alg.MaximaCompute.Vector;

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

        int partition_component = dimension-1;

        System.out.print(items.size()+","+dimension);

        currParetoSet.add(items.remove(0));

        Map<Item,Label> labels;

        MaximaCompute maximaCompute = new MaximaCompute(itemLabels);

        int maxSize = -1;

        for(Item item: items){
            newItems = addItemToList(currParetoSet,item);

//            System.out.println(currParetoSet+"   "+newItems);

            unionSet = merge(currParetoSet,newItems);


            //TODO this function is not needed, since `item` is always
            // the smallest element among current element of unionSet
            insertInPosition(item,unionSet);

            labels = new HashMap<>();
            init(unionSet,labels);


//            System.out.println(unionSet.size());

/*            System.out.println("Union set");
            for(Item i: unionSet) {
//                i.getVector().print();
                System.out.println(i+" "+i.getVector().getRank());
//                System.out.println(i+" "+i.getLabel());
            }
            System.out.println();*/

/*            System.out.println("Linked");
            Item x = unionSet.get(0);
            while(x!=null){
                System.out.println(x);
                x = x.getNext();

            }
            System.out.println();*/


            if(unionSet.size()>maxSize)
                maxSize=unionSet.size();


            // the max and min vectors in component 0
            Vector  minVector_comp0 = unionSet.get(unionSet.size()-1).getVector();
            Vector maxVector_comp0 = unionSet.get(0).getVector();

            List<Item> union_1 = new ArrayList<>(unionSet);

            maximaCompute.setMaxVector_comp0(maxVector_comp0);
            maximaCompute.setMinVector_comp0(minVector_comp0);
//            maximaCompute = new MaximaCompute(unionSet,partition_component,minVector_comp0,maxVector_comp0,itemLabels);
            List<Item> maximals=null;
            long start = System.currentTimeMillis();
            if(dimension>3)
                //starting from last dimension,dimension-1 is the index for last dimension
                maximals=maximaCompute.find_maxima(unionSet,dimension-1,labels);
            else if (dimension==3) {
                maximals = maximaCompute.find_maxima_base3(unionSet);
            }


            if(!maximaCompute.isCorrect(maximals)) {
                System.out.println("Not correct");
                System.exit(1);
            }
        else {
                List<Item> maximals_1  = maximaCompute.maxima_naive(union_1,dimension);

                if(maximals.size()!=maximals_1.size() || !listEqualsNoOrder(maximals_1,maximals)) {
                    System.out.println("Not equal with the result of naive");
                    System.out.println(maximals.size()+" "+maximals_1.size());

                    for (Item it : difference(maximals_1,maximals))
                        System.out.println(it);


                    System.exit(1);
                }

                //TODO use linked list and sort it in linear time
                Collections.sort(maximals, Collections.reverseOrder());

//                System.out.println("It is correct");
                currParetoSet = maximals;
            }
        }


        System.out.print(","+currParetoSet.size()+","+ maxSize+"\n");
    }


    public static <T> Set<T> difference(List<T> l1, List<T> l2) {
        final Set<T> setA = new HashSet<>(l1);
        final Set<T> setB = new HashSet<>(l2);
        Set<T> tmp = new HashSet<T>(setA);
        tmp.removeAll(setB);
        return tmp;
    }


    public static <T> boolean listEqualsNoOrder(List<T> l1, List<T> l2) {
        final Set<T> s1 = new HashSet<>(l1);
        final Set<T> s2 = new HashSet<>(l2);

        return s1.equals(s2);
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
//        items.add(newItem);
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
     * Create a linked list from the given list and also initialize
     * some variables for items, and initialize label map
     * @param list
     * @param labels
     *
     * */
    public void init(List<Item> list, Map<Item,Label> labels){
        Item item;
        Item next=null;

        //TODO newItem and newList aren't necessary
        Item newItem=null;
        List<Item> newList = new ArrayList<>();

        int listSize = list.size();

        //from smallest to biggest
        for(int i = listSize-1 ; i>=0 ; i--){
            item = list.get(i);
//            newItem = new Item(item.getVector(),newItem);
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
