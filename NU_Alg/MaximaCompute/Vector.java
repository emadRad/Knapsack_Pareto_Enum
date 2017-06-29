package NU_Alg.MaximaCompute;

import NU_Alg.NU_Core.ItemLabel;

public class Vector implements Comparable<Vector> {

    // dimension of the vector
    private int dimension;

    private double [] list;

    private int sortComponent;

    static ItemLabel [] componentLabels;

    private boolean DOMINATED;

    /*
    *  It is used in marriage of base case(d=3) to for comparing vectors
     * in order to have an ordering in the case of equal vectors
    * */
    private int rank;

    //creating empty vector of size dimension
    public Vector(int dimension){
        this.dimension = dimension;
        list = new double[dimension];
        sortComponent = 0;
        DOMINATED = false;
        this.rank = 0;
    }

    public Vector(int dimension, double [] list){
        this.dimension = dimension;
        this.list = list;
        sortComponent = 0;
        DOMINATED = false;
        this.rank = 0;
    }

    public void setComponentLabels(ItemLabel [] il) { componentLabels= il;}

    public boolean isDominated(){return DOMINATED;}
    public void setDominated(boolean d){ this.DOMINATED=d;}

    public int getRank(){return rank;}
    public void setRank(int rank){this.rank = rank;}


    /* Setting the value of d-th component this vector
    * @input:
    *       d , the component number(d is in the interval [0,dimension) )
    *       value , the value to be stored
    * @output:
    * */
    public void setComponent(int d, double value) throws Exception {
        if(d>=0 && d<dimension)
            list[d]=value;
        else{
            throw new Exception("Component number d is out of bound ");
        }
    }

    /* Return the d component of this vector
    * @input: d, the component number(d is in the interval [0,dimension) )
    * @output: the d-th component of vector
    * */
    public double getComponent(int d) throws Exception {
        if(d>=0 && d<dimension)
            return list[d];
        else{
            throw new Exception("Component number "+ d + " is out of bound ");
        }
    }


    public int getDimension(){
        return dimension;
    }


    @Override
    public String toString(){
        int length = list.length;
        String str="( ";
        if(length!=0)
            for(int i=0;i<length;i++) {
                str = str + list[i];
                str += length-1==i ? "" : "   ";
        }
        str+=" )";
        return str;
    }

    public void print(){
        int length = list.length;
        if(length!=0)
            for(int i=0;i<length;i++)
                System.out.print(list[i]+"    ");
        System.out.println();
    }

    /*
    * set the component according to which the comparison is going to be done
    *
    * @input:
    *       d , the component number
    * @output:
    *
    * */
    public void setSortComponent(int d){
        sortComponent = d;
    }


    @Override
    public int compareTo(Vector v2) {
        if (sortComponent < 0 || sortComponent > dimension) {
            System.err.println("Sort component is out of bound");
            System.exit(0);
        }
        int k = 0;
        int return_v=0;
        try {
            while (k < this.getDimension() && this.getComponent(k) == v2.getComponent(k))
                k++;
            if (k == this.getDimension())
                return 0;
            if (this.getComponent(k) > v2.getComponent(k)) {
                return_v = componentLabels[k]==ItemLabel.WEIGHT ? -1 : 1;
                return return_v;
            }
            if (this.getComponent(k) < v2.getComponent(k)) {
                return_v = componentLabels[k]==ItemLabel.WEIGHT ? 1 : -1;
                return return_v;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


}
