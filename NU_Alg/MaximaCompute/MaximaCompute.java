package NU_Alg.MaximaCompute;
import NU_Alg.NU_Core.Item;
import NU_Alg.NU_Core.ItemLabel;
import NU_Alg.MaximaCompute.Comparison
        ;
import java.util.*;

/**
 * Created by emad on 17.04.17.
 */
public class MaximaCompute {

    Partition partition;
    int partitionComponent;

    Vector maxVector_comp0;
    Vector minVector_comp0;

    //TODO can be shared between needed classes
    ItemLabel [] itemLabels;

    Comparison compare;

    public MaximaCompute(ItemLabel [] itemLabels){
        partition =new Partition(itemLabels);
        this.itemLabels= itemLabels;
        compare = new Comparison(itemLabels);

    }

    public void setPartitionComponent(int partitionComponent){this.partitionComponent = partitionComponent;}
    public void setItemLabels(ItemLabel [] itemLabels){this.itemLabels = itemLabels;}
    public void setMinVector_comp0(Vector minVect){ this.minVector_comp0 = minVect;}
    public void setMaxVector_comp0(Vector maxVect){ this.maxVector_comp0 = maxVect;}

    public MaximaCompute(List<Item> arr, int partitionComponent, Vector min, Vector max, ItemLabel [] il){
        this.partitionComponent = partitionComponent;
        partition = new Partition(arr,partitionComponent,il);
        maxVector_comp0 = max;
        minVector_comp0 = min;
        itemLabels = il;
        compare = new Comparison(itemLabels);
    }


    public List<Item> find_maxima(List<Item> vectors, int k, Map<Item, Label> labels) throws Exception {
        if (vectors.size()<=2){
            if (vectors.size()==1)
                return vectors;
            else
                return marriage_base_k(vectors);
        }
        else{
            partition.setPartitionComponent(k);
            partitionComponent = k;
            partition.setList(vectors);

            int median_position;
            if (vectors.size()%2==0)
                median_position = (int)Math.floor(vectors.size()/2)-1;
            else
                median_position = (int)Math.floor(vectors.size()/2);

            int median_index = partition.select(0,vectors.size()-1, median_position );

            List<Item> first_sublist;
            List<Item> second_sublist;


            first_sublist =  vectors.subList(0, median_index+1);
            second_sublist = vectors.subList(median_index+1, vectors.size());


            first_sublist = find_maxima(first_sublist,k,labels);
            second_sublist = find_maxima(second_sublist,k,labels);


            return solveLowerDim(first_sublist,second_sublist,labels,k);
        }

    }


    public List<Item> find_maxima_k(List<Item> vectors, int k, Map<Item, Label> labels) throws Exception {
        if(k==2) {
            for (Item item : vectors){
                item.setLabel(labels.get(item));
            }
            return find_maxima_base3(vectors);
        }
        else{
            partition.setPartitionComponent(k);
            partitionComponent = k;
            partition.setList(vectors);

            int median_position;
            if (vectors.size()%2==0)
                median_position = (int)Math.floor(vectors.size()/2)-1;
            else
                median_position = (int)Math.floor(vectors.size()/2);

            int median_index = partition.select(0,vectors.size()-1, median_position);

            Map<Item,Label> vectors_label = new HashMap<>();

            List<Item> first_sublist = new ArrayList<>();
            List<Item> second_sublist = new ArrayList<>() ;

            // storing the labeling from upper dimension
            for(Item item : vectors.subList(0,median_index+1) ) {
                vectors_label.put(item, labels.get(item));
                first_sublist.add(item);
            }

            for(Item item : vectors.subList(median_index+1,vectors.size()) ) {
                vectors_label.put(item, labels.get(item));
                second_sublist.add(item);
            }


            if (second_sublist.size()==0 || first_sublist.size()==0){
                return vectors;
            }

            first_sublist = find_maxima(first_sublist,k,vectors_label);
            second_sublist = find_maxima(second_sublist,k,vectors_label);

            return solveLowerDim(first_sublist,second_sublist,vectors_label,k);
        }

    }

    public List<Item> solveLowerDim(List<Item> first_sublist, List<Item> second_sublist, Map<Item,Label> labels, int k ) throws Exception {


        List<Item> merged_list = new ArrayList<>();
        List<Item> maximals = new ArrayList<>();

        int num_of_A=0;
        int num_of_B=0;

        Map<Item,Label> vectors_label = new HashMap<>();

        for (Item item : first_sublist  ) {
            if (labels.get(item)==Label.NULL){
                vectors_label.put(item,Label.A);
                merged_list.add(item);
                num_of_A++;
            }
            else{
                if (labels.get(item) == Label.A) {
                    vectors_label.put(item, labels.get(item));
                    merged_list.add(item);
                    num_of_A++;
                }
                if (labels.get(item) == Label.B) {
                    maximals.add(item);
                }
            }
        }

        for (Item item : second_sublist){
            if (labels.get(item)==Label.NULL) {
                vectors_label.put(item,Label.B);
                merged_list.add(item);
                maximals.add(item);

                num_of_B++;
            }
            else{
                if (labels.get(item) == Label.B) {
                    merged_list.add(item);
                    vectors_label.put(item,labels.get(item));
                    num_of_B++;
                }
                maximals.add(item);
            }
        }


        List<Item> merge_result;
        if (num_of_A+num_of_B<2) {
            if(num_of_A==1) {
                if (vectors_label.get(merged_list.get(0)) == Label.A)
                    maximals.add(merged_list.get(0));
            }
        }
        else {
            merge_result = find_maxima_k(merged_list, k - 1, vectors_label);

            for (Item item : merge_result){
                if (vectors_label.get(item)==Label.A)
                    maximals.add(item);
            }
        }

        return  maximals;
    }




    public List<Item> marriage_base_k(List<Item> vectors) throws Exception {
        List<Item> maximals = new ArrayList<>();
        if(vectors.size()<2)
            return vectors;
        Item first = vectors.get(0);
        Item second = vectors.get(1);


        if (compare.dominates(first,second,first.getVector().getDimension())){
            maximals.add(first);
        }
        else if (compare.dominates(second,first,first.getVector().getDimension())){
            maximals.add(second);
        }
        else{
            maximals.add(first);
            maximals.add(second);
        }
        return maximals;
    }

    /*
    * Utility function to update the linked list by deleting dominated vectors
    * */
    private void updateLinkedList(Item item){
        if(item.getPrevious()!=null)
            item.getPrevious().setNext(item.getNext());
        else if (item.getPrevious()==null && item.getNext()!=null)
            item.getNext().setPrevious(null);
    }


    public List<Item> find_maxima_base3(List<Item> vectors) throws Exception {
        if(vectors.size()<=2){
            if( vectors.size()==1 || vectors.size()==0 )
                return vectors;
            else
                return marriage_base_k(vectors);
        }
        else{
            partition.setPartitionComponent(2);
            partitionComponent = 2;
            partition.setList(vectors);

            int median_position;
            if (vectors.size()%2==0)
                median_position = (int)Math.floor(vectors.size()/2)-1;
            else
                median_position = (int)Math.floor(vectors.size()/2);

            int medIndex = partition.select(0,vectors.size()-1, median_position);

            Item median = vectors.get(medIndex);

            List<Item> first_sublist;
            List<Item> second_sublist;


            Map<Item,Label> vectors_label;

            vectors_label = new HashMap<>();



            first_sublist = new ArrayList<>();
            second_sublist = new ArrayList<>();

            for (Item item : vectors.subList(0, medIndex+1)) {
                first_sublist.add(item);
                vectors_label.put(item, Label.A);
            }

            for (Item item : vectors.subList(medIndex+1, vectors.size())) {
                vectors_label.put(item, Label.B);
                second_sublist.add(item);
            }


            first_sublist = find_maxima_base3(first_sublist);
            second_sublist = find_maxima_base3(second_sublist);

            List<Item> maximals = new ArrayList<>();


            maximals.addAll(second_sublist);


            List<Item> merged_list = new ArrayList<>();

            merged_list.addAll(first_sublist);
            merged_list.addAll(second_sublist);


            if (merged_list.size()<=2) {
                maximals = marriage_base_k(merged_list);
            }
            else{
                List<Item> tmp = marriage(merged_list, median, vectors_label);
                maximals.addAll(tmp);
            }

            return maximals;

        }
    }


    public List<Item> marriage(List<Item> vectors, Item median, Map<Item,Label> labels) throws Exception {

        List<Item> maximals = new ArrayList<>();


        if(vectors.size()==2) {
            return marriage_base_k(vectors);
        }

        Vector maxVector_sublist = minVector_comp0 ;
        Vector minVector_sublist = maxVector_comp0;


        int startNode_index = 0;
        int endNode_index = vectors.size()-1;
        Item curr_item;

        int compareTo;

        int c1=0;
        int c2=0;

        for(int i=0;i<vectors.size();i++) {
            curr_item = vectors.get(i);

            c1++;
//            System.out.println(curr_item+" "+curr_item.getVector().getRank());

            curr_item.setACTIVE(true);
            curr_item.getVector().setDominated(false);

            compareTo = curr_item.getVector().compareTo(maxVector_sublist);
            if ( compareTo == 1 ){ //
                maxVector_sublist = curr_item.getVector();
                startNode_index = i;
            }
            else{
                // when two vector are equal it is needed to check
                // whether the maxVector_sublist points to the curr_item or the other way around
                if ( compareTo == 0 ){
                    if ( curr_item.getVector().getRank() > maxVector_sublist.getRank()  ){
                        maxVector_sublist = curr_item.getVector();
                        startNode_index = i;
                    }
                    else if(curr_item.getVector().getRank() == maxVector_sublist.getRank()){
                        maxVector_sublist = curr_item.getVector();
                        startNode_index = i;
                    }
                }

            }

            compareTo =  curr_item.getVector().compareTo(minVector_sublist);
            if ( compareTo == -1 ) {
                minVector_sublist = curr_item.getVector();
                endNode_index = i;
            }
            else if( compareTo == 0 ){
                if ( curr_item.getVector().getRank() < minVector_sublist.getRank()  ){
                    minVector_sublist = curr_item.getVector();
                    endNode_index = i;

                }
                else if (curr_item.getVector().getRank() == minVector_sublist.getRank()){
                    minVector_sublist = curr_item.getVector();
                    endNode_index = i;
                }
            }

        }

        curr_item = vectors.get(startNode_index) ;
        Item endItem = vectors.get(endNode_index);
        endItem = endItem.getNext();


        // the current maximum value of the component 1(y) of the points with label B
        // if component 1 is weight we need to find the minimum
        double current_max_1_B = Double.MAX_VALUE;

        if(itemLabels[1]==ItemLabel.PROFIT)
            current_max_1_B = -1 * current_max_1_B;


        Label vector_label=null;
        Vector vector=null;

        //the label from upper dimension
        Label upper_vector_label=null;
        Label curr_maxVector_upper_label =null;

        Item max_vector_B = null;


        while(curr_item != endItem && curr_item !=null){
            if(curr_item.isACTIVE()) {
                c2++;
                vector = curr_item.getVector();
                vector_label =  labels.get(curr_item);

                // if there is not any labeling from upper dimension( in the case input with d=3)
                // set the upper label equal to the label of current dimension
                upper_vector_label = curr_item.getLabel()!=Label.NULL ? curr_item.getLabel() : vector_label;


                if(vector_label==Label.B){
                    if(itemLabels[1]==ItemLabel.PROFIT) {
                        if (curr_item.getVector().getComponent(1) > current_max_1_B && upper_vector_label == Label.B) {
                            max_vector_B = curr_item;
                            curr_maxVector_upper_label = curr_item.getLabel() != Label.NULL ? curr_item.getLabel() : vector_label;
                            current_max_1_B = curr_item.getVector().getComponent(1);
                        }
                    }
                    else if (itemLabels[1]==ItemLabel.WEIGHT){
                        if (curr_item.getVector().getComponent(1) < current_max_1_B && upper_vector_label == Label.B) {
                            max_vector_B = curr_item;
                            curr_maxVector_upper_label = curr_item.getLabel() != Label.NULL ? curr_item.getLabel() : vector_label;
                            current_max_1_B = curr_item.getVector().getComponent(1);
                        }
                    }
                }

                if(vector_label==Label.A){

                    if(upper_vector_label == Label.A && curr_maxVector_upper_label == Label.B) {
                        if (vector.getComponent(1) == current_max_1_B) {
                            if (compare.partialCompare(max_vector_B.getVector(), curr_item.getVector(), 3) != 0) {
                                if (compare.dominates(max_vector_B, curr_item, 3)) {
                                    vector.setDominated(true);
                                }
                            }
                            else {
                                if (compare.dominates(max_vector_B, curr_item, curr_item.getVector().getDimension())) {
                                    vector.setDominated(true);
                                }
                            }
                        }
                        else {
                            if(itemLabels[1]==ItemLabel.PROFIT) {
                                if (vector.getComponent(1) < current_max_1_B) {
                                    vector.setDominated(true);
                                }
                            }
                            else if (itemLabels[1]==ItemLabel.WEIGHT){
                                if (vector.getComponent(1) > current_max_1_B) {
                                    vector.setDominated(true);

                                }
                            }
                        }
                    }

                }

                if(vector!=null && !curr_item.getVector().isDominated() && vector_label==Label.A) {
                    maximals.add(curr_item);
                }
                curr_item.setACTIVE(false);
            }
            curr_item = curr_item.getNext();
        }

        return maximals;
    }

    //the marriage step for two sublist of size 1 (base case of recursive)
    public List<Item> marriage_base(List<Item> vectors, Map<Item,Label> labels) throws Exception {
        if(vectors.size()<2)
            return vectors;

        List<Item> maximals = new ArrayList<>();
        Item first = vectors.get(0);
        Item second = vectors.get(1);
        boolean first_dominated = false;
        boolean second_dominated = false;

        if(compare.dominates(second,first,3)) {
            first_dominated = true;
        }
        else if(compare.dominates(first,second,3)) {
            second_dominated = true;
        }

        if (! first_dominated)
            maximals.add(first);
        if (! second_dominated)
            maximals.add(second);

        return maximals;
    }




    public boolean isCorrect(List<Item> vectors) throws Exception {

        Item toCheck;
        int dim;
        if(vectors.size()>0)
            dim = vectors.get(0).getVector().getDimension();
        else
            return true;

        for (int i=0;i<vectors.size();i++) {
            toCheck = vectors.get(i);
            for (int j = 0; j < vectors.size(); j++) {
                if(compare.dominates(toCheck,vectors.get(j),dim)){
                    System.out.println("It is not correct!!");
                    toCheck.getVector().print();
                    vectors.get(j).getVector().print();
                    return false;
                }
            }
        }
        return true;

    }


    /**
     * The naive algorithm O(k*n^2)
     *   k: dimension
     *   n: number of vectors
     *   @param vectors a list of vectors
     *   @param dim dimension of vector
     *   @return list of maximal vectors
     * */
    public List<Item> maxima_naive(List<Item> vectors, int dim) throws Exception {
        List<Item> maximals = new ArrayList<>();
        int i=0;
        int j;
        boolean notDominated= true;
        while (i<vectors.size()){
            j=0;
            notDominated= true;
            while (j<vectors.size()){
                if (j!=i) {
                    if (compare.dominates(vectors.get(j), vectors.get(i),dim)) {
                        notDominated = false;
                        break;
                    }
                }
                j++;
            }
            if(notDominated)
                maximals.add(vectors.get(i));
            i++;
        }
        return maximals;
    }

}
