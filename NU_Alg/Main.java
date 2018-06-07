package NU_Alg;
import NU_Alg.MaximaCompute.Vector;
import NU_Alg.MaximaCompute.*;
import NU_Alg.NU_Core.*;

import java.util.*;

public class Main {

    public void solve(String inputFileName) throws Exception {

        if(inputFileName.isEmpty())
            inputFileName ="testing/Inputs/default/input_4d_11p.dat";

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


        Collections.reverse(linked_array);


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
        long [] results;

//       InputSize,Dim,Time(ms),AvgIterTime,AvgParetoSize,MaxParetoSize,MinParetoSize,AvgSize,MaxSize  >> results.log
//        System.out.print(linked_array.size()+" "+ dimension);

        NU_Core nu_core = new NU_Core(linked_array,itemLabels);
        results = nu_core.solve();

        System.out.println(results[0]+" "+results[1]+" "+ results[2]+" "+results[3]+" "+results[4]+" "+ results[5]+" "+ results[6]);


    }


    public static void main(String[] args) throws Exception {
        Main main = new Main();
        if (args.length != 0)
            main.solve(args[0]);
        else {
            System.out.printf("");
            main.solve("");

        }
    }


    public void testCorrectness(List<Item> items, ItemLabel [] itemLabels) throws Exception {

        int dimension = items.get(0).getVector().getDimension();

        Testing test = new Testing(items, dimension, itemLabels);
        FLET flet = new FLET(itemLabels);
        List<Item> maximals = flet.find_maxima_FLET(items);

        if(!test.isCorrect(maximals)){
            System.out.printf("Not correct");
            System.out.print(items.size()+" "+dimension+" ");
            System.exit(1);
        }
        else{
            if(!test.isComplete(maximals)){
                System.out.printf("Not complete");
                System.out.print(items.size()+" "+dimension+" ");
                System.exit(1);
            }
        }

    }



   /*
   * Sorting vectors lexicographically
   * */
    private void sortVectors(List<Vector> vectors, ItemLabel [] itemLabels){
        final ItemLabel [] itemLabel = itemLabels;
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
                        return_v = itemLabel[k]==ItemLabel.WEIGHT ? -1 : 1;
                        return return_v;
                    }
                    if(o1.getComponent(k) < o2.getComponent(k)) {
                        return_v = itemLabel[k]==ItemLabel.WEIGHT ? 1 : -1;
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
