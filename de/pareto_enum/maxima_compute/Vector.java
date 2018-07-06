package de.pareto_enum.maxima_compute;

import de.pareto_enum.enum_core.DimLabel;

/**
 * This class defines the Vector object. It stores the vector as an array and
 * it also provides a set features including rank, DOMINATED and etc that will be
 * explained in their corresponding methods.
 *
 * */
public class Vector implements Comparable<Vector> {

    // dimension of the vector
    private int dimension;

    private double [] list;

    private int sortComponent;

    static DimLabel [] componentLabels;

    private boolean DOMINATED;

    /*
     * It breaks the tie in the case of equality between two or more vectors.
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

    /**
     * Sets the componentLabels array which stores the label of each dimension.
     *
     * @param il an array of type DimLabel
     *
     * */
    public void setComponentLabels(DimLabel [] il) { componentLabels= il;}

    /**
     * Sets the boolean DOMINATED which indicates whether this vector is dominated or not.
     * @return a boolean
     *          true: this vector is dominated by another vector
     *          false: this vector is not dominated
     * */
    public boolean isDominated(){return DOMINATED;}


    public void setDominated(boolean d){ this.DOMINATED=d;}


    /**
     * Sets the rank variable which is defined to break ties when
     * several vectors are equal to each other.
     *
     * @param rank an integer for the rank of this vector
     * */
    public void setRank(int rank){this.rank = rank;}

    /**
     * Returns the rank of vector.
     * @return an integer which is the rank
     * */
    public int getRank(){return rank;}



    /**
    * Setting the value of d-th component of this vector.
     *
    * @param d the component number(d is in the interval [0,dimension) )
    * @param value the value to be stored
    * */
    public void setComponent(int d, double value) throws Exception {
        if(d>=0 && d<dimension)
            list[d]=value;
        else{
            throw new Exception("Component number d is out of bound ");
        }
    }


    /**
     *  Return the d component of this vector
    * @param  d the component number(d is in the interval [0,dimension) )
    * @return  the d-th component of vector
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

    /**
    * Sets the component according to which the comparison is going to be done
    *
    * @param d the component number
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
                return_v = componentLabels[k]==DimLabel.WEIGHT ? -1 : 1;
                return return_v;
            }
            if (this.getComponent(k) < v2.getComponent(k)) {
                return_v = componentLabels[k]==DimLabel.WEIGHT ? 1 : -1;
                return return_v;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
