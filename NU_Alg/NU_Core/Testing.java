package NU_Alg.NU_Core;
import java.util.*;
import NU_Alg.MaximaCompute.Comparison;


public class Testing {

    Comparison compare;
    private List<Item> maximals_correct;

    public Testing(List<Item> vectors, int dim, ItemLabel [] itemLabels) throws Exception {
        compare = new Comparison(itemLabels);
        maximals_correct = maxima_naive(vectors, dim);
    }



    /**
     * Compares the computed solution from give algorithm to
     * the solution from naive algorithm for finding maxima
     * @param maximals list of maximal elements to be tested
     * @return a boolean value,
     *         true if maximals is equal to maximals_correct(the correct set of maximals)
     *         false otherwise
     * */
    public boolean isComplete(List<Item> maximals){

        if(maximals.size()!=maximals_correct.size() || !listEqualsNoOrder(maximals_correct,maximals)) {
            System.out.println("Not equal with the result of naive");
            System.out.println(maximals.size()+" "+maximals_correct.size());

            for (Item it : difference(maximals_correct,maximals))
                System.out.println(it);

            return false;
//            System.exit(1);
        }
        return true;
    }

    /**
    * Checks if all no vector in vectors dominates any other vector in the set
     *
     * @param vectors the set of vectors to be tested
     * @return a boolean value,
     *         true if no vector dominates any other vector
     *         false otherwise
    * */
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


}
