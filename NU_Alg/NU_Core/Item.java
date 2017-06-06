package NU_Alg.NU_Core;
import NU_Alg.MaximaCompute.Label;
import NU_Alg.MaximaCompute.Vector;

public class Item {
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

}

