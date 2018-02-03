package NU_Alg;
import NU_Alg.MaximaCompute.Vector;
import NU_Alg.MaximaCompute.*;
import NU_Alg.NU_Core.*;

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


        Collections.reverse(linked_array);
        NU_Core nu_core = new NU_Core(linked_array,itemLabels);
        nu_core.solve();


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
