package NU_Alg;
import NU_Alg.MaximaCompute.Vector;
import NU_Alg.MaximaCompute.*;
import NU_Alg.NU_Core.Item;
import NU_Alg.NU_Core.ItemLabel;

import java.util.*;

public class Main {

    public void solve(String inputFileName) throws Exception {

        if(inputFileName.isEmpty())
            inputFileName ="Inputs/default/input_4d_11p.dat";

        ReadInput ri = new ReadInput(inputFileName);

        List<Vector> vectors = new ArrayList<>();
        ItemLabel [] itemLabels;
        itemLabels = ri.readData(vectors);

        //if the inputs file is empty
        if(vectors.size()==0)
            return;

        sortVectors(vectors,itemLabels);


        List<Item> linked_array = new ArrayList<>();
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
            linked_array.add(node);
        }
        node.getVector().setComponentLabels(itemLabels);

        // the max and min vectors in component 0
        Vector  minVector_comp0 = linked_array.get(0).getVector();
        Vector maxVector_comp0 = linked_array.get(linked_array.size()-1).getVector();


        int dimension = vectors.get(0).getDimension();

//        for(ItemLabel il : itemLabels)
//            System.out.print(il+" ");
//        System.out.println();
//        for(Item item: linked_array) {
//            item.getVector().print();
//        }
//        System.out.println();


        System.out.print(linked_array.size()+","+dimension);

//        System.out.println(linked_array.size()+","+dimension);
        List<Item> linked_array_1 =new ArrayList<>(linked_array);

        int partition_component = dimension-1;


        MaximaCompute mf = new MaximaCompute(linked_array,partition_component,minVector_comp0,maxVector_comp0,itemLabels);
        List<Item> maximals=null;
        long start = System.currentTimeMillis();
        if(dimension>3)
            //starting from last dimension,dimension-1 is the index for last dimension
            maximals=mf.find_maxima(linked_array,dimension-1,labels);
        else if (dimension==3) {
            maximals = mf.find_maxima_base3(linked_array);
        }

        System.out.print(","+maximals.size()+"\n");


        if(!mf.isCorrect(maximals)) {
            System.out.println("Not correct");
            System.exit(1);
        }
//        else
//            System.out.println("It is correct");


        List<Item> maximals_1  = mf.maxima_naive(linked_array_1,dimension);

        if(!listEqualsNoOrder(maximals_1,maximals)) {
            System.out.println("Not equal with the result of naive");
            System.out.println(maximals.size()+" "+maximals_1.size());
            System.exit(1);
        }

    }



    public static void main(String[] args) throws Exception {
        Main main = new Main();
        if(args.length!=0)
            main.solve(args[0]);
        else
            main.solve("");
    }





     public static <T> boolean listEqualsNoOrder(List<T> l1, List<T> l2) {
        final Set<T> s1 = new HashSet<>(l1);
        final Set<T> s2 = new HashSet<>(l2);

        return s1.equals(s2);
    }



   /*
   * Sorting vectors lexicographically
   * */
    public void sortVectors(List<Vector> vectors, ItemLabel [] itemLabels){
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
                        return_v = itemLabels[k]==ItemLabel.WEIGHT ? -1 : 1;
                        return return_v;
                    }
                    if(o1.getComponent(k) < o2.getComponent(k)) {
                        return_v = itemLabels[k]==ItemLabel.WEIGHT ? 1 : -1;
                        return return_v;
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                return return_v;
            }
        });
    }


}
