package de.pareto_enum;

import de.pareto_enum.maxima_compute.Vector;
import de.pareto_enum.maxima_compute.*;
import de.pareto_enum.enum_core.*;

import java.util.*;

public class Main {


    public void solve(List<Vector> vectors, DimLabel [] dimLabels, String outMode, String alg) throws Exception {

        //if the inputs file is empty
        if(vectors.size()==0)
            return;

        Util util = new Util();
        int dimension=0;

        util.sortVectors(vectors,dimLabels);

        List<Item> linkedArray = new ArrayList<>();

        util.createLinkedList(vectors, linkedArray);


        if(linkedArray.size()>0) {
            //the dimLabels is shared among all objects the Vector class
            linkedArray.get(0).getVector().setComponentLabels(dimLabels);
            dimension = linkedArray.get(0).getVector().getDimension();
        }

        Collections.reverse(linkedArray);


        long [] results;

        if(outMode.equals("single")){
            System.out.println("Using '"+alg+"' algorithm for computing Pareto set.");
            System.out.print("Number of Item: "+ linkedArray.size()+"   "+
                              "Dimension: "+dimension+"\n");
        }

        EnumCore enumCore = new EnumCore(linkedArray,dimLabels);

        /*
         * 0 : TotalTime
         * 1 : AvgIterTime, average time of each iteration
         * 2 : AvgParetoSize, average size of paretoSize
         * 3 : MaxParetoSize
         * 4 : MinParetoSize
         * 5 : AvgSize, average size of unionSets in EnumCore.solve
         * 6 : MaxSize, max size of unionSets in EnumCore.solve
         *
         * */
        results = enumCore.enumerator(alg);

        if(outMode.equals("single")){
            System.out.println("Total runtime: "+ results[0]+" ms");
            System.out.println("Average iteration time: "+ results[1]);
            System.out.println("Average size of Pareto set: "+results[2]);
            System.out.println("Max size of Pareto set: "+results[3]);
            System.out.println("Min size of Pareto set: "+results[4]);
            System.out.println("Average size of input for maxima finding: "+ results[5]);
            System.out.println("Max size of input for maxima finding:: "+ results[6]);
        }
        else if(outMode.equals("expr")){
            System.out.println( results[0]+" "+results[1]+" "+
                                results[2]+" "+results[3]+" "+
                                results[4]+" "+ results[5]+" "+
                                results[6]);
        }


    }



    public void testCorrectness(List<Item> items, DimLabel [] dimLabels) throws Exception {

        int dimension = items.get(0).getVector().getDimension();

        Testing test = new Testing(items, dimension, dimLabels);
        FLET flet = new FLET(dimLabels);
        List<Item> maximals = flet.findMaximaFLET(items);

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



    public static void main(String[] args) throws Exception {

        Main main = new Main();
        String mode = "";
        String fileName = "";
        String algorithm = "";

        if (args.length >= 4) {
            for(int i=0; i<args.length; i++){
                if(args[i].charAt(0)=='-'){
                    if(args.length-1 == i){
                        throw new IllegalArgumentException("Expected arg after: "+args[i]);
                    }
                    switch (args[i].charAt(1)){
                        case 'm':
                            mode = args[i+1];
                            if(!mode.equals("single") && !mode.equals("expr"))
                                throw new IllegalArgumentException("Expected single or expr: but "+ mode+" is given.");
                            break;
                        case 'f':
                            fileName = args[i+1];
                            break;
                        case 'a':
                            algorithm = args[i+1];
                            if( !algorithm.equals("mdc") &&
                                !algorithm.equals("flet"))
                                throw new IllegalArgumentException("Expected mdc, flet" +
                                        " but " + algorithm +" is given.");
                            break;
                    }
                }
            }

            ReadInput ri = new ReadInput(fileName);

            List<Vector> vectors = new ArrayList<>();
            DimLabel [] dimLabels;
            dimLabels = ri.readData(vectors);
            mode = mode.toLowerCase();

            algorithm = algorithm.toLowerCase();
            if(algorithm.isEmpty())
                algorithm="flet";

            main.solve(vectors, dimLabels, mode, algorithm);
        }
        else {
            System.out.println("Usage: java -jar knapsackParetoEnum.jar -f [input_file] " +
                                " -m [single or expr] "+
                                " -a [mdc or flet]");
            System.out.println("In 'expr' mode the program shows output without any labels.");
            System.out.println("Option -a determines which maxima finding algorithm to use.");
        }
    }

}
