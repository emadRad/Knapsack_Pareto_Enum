package de.pareto_enum.enum_core;

import de.pareto_enum.maxima_compute.Label;
import de.pareto_enum.maxima_compute.Vector;

/**
 * This class defines the object for a knapsack item. It stores the vector of values(profit or weight).
 * The main purpose of this class is that it enables us to implement a linked list on objects of this class.
 *
 * @author  <a href="mailto:emadbahramirad@gmail.com">Emad Bahrami Rad</a>
 * */
public class Item implements Comparable<Item> {
    Vector vector;


    //for linked list interface
    Item next;
    Item previous;


    private boolean ACTIVE;
    private Label label;



    public Item(Vector v, Item n){
        vector=v;
        next = n;
        ACTIVE = false;
        label = Label.NULL;
    }

    //copy constructor
    public Item(Item item){
        vector = item.vector;
        ACTIVE = false;
        if(item.next!=null)
            next = item.next;
        else
            next = null;
        label = item.getLabel();
    }

    @Override
    public String toString(){
        return vector.toString();
    }

    public Label getLabel(){return label;}
    public void setLabel(Label l){this.label=l;}

    public void setACTIVE(boolean a){ ACTIVE = a;}
    public boolean isACTIVE(){return ACTIVE;}

    public Item getNext(){return next;}
    public void setNext(Item n){next = n;}

    public void setVector(Vector v){ vector = v;}
    public Vector getVector(){return vector; }

    public void setPrevious(Item p){ previous = p;}
    public Item getPrevious(){return previous;}

    @Override
    public int compareTo(Item i2) {
        if(this.getVector().compareTo(i2.getVector())==1)
            return 1;
        else if(this.getVector().compareTo(i2.getVector())==-1)
            return -1;
            else
                return 0;
    }

}

