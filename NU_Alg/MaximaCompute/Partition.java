package NU_Alg.MaximaCompute;

import NU_Alg.NU_Core.Item;
import java.util.Collections;
import java.util.List;

/**
 * Created by emad on 08.04.17.
 *
 * Implementation of the median of medians algorithm to partition a list by its median
 * Time complexity : O(n)
 * source: https://en.wikipedia.org/wiki/Median_of_medians#CITEREFBlumFloydPrattRivest1973
 *
 */
public class Partition {

    private List<Item> arr;

    double median_value;


    // the component of.getVector() according to which the partitioning is done
    private int partitionComponent;

    public Partition(List<Item> a, int partitionComponent){
        arr = a;
        this.partitionComponent = partitionComponent;
    }

    public Partition(){}


    public void setList(List<Item> a){
        this.arr = a;

    }
    public void setPartitionComponent(int pc){
        partitionComponent = pc;
    }




    // n_th = left+floor((right-left)/2)
    public int select(int left, int right, int n_th) throws Exception {
        if(left==right) {
            median_value = arr.get(right).getVector().getComponent(partitionComponent);
            return right;
        }
        else{
            int pivotIndex = pivot(left,right);
            pivotIndex = partition(left,right,pivotIndex);


            if( n_th == pivotIndex ) {
                median_value = arr.get(n_th).getVector().getComponent(partitionComponent);
                return n_th;
            }
            else{
                if ( n_th < pivotIndex){
                    right = pivotIndex - 1;
                    return select(left,right,n_th);
                }
                else{
                    left = pivotIndex + 1;
                    return select(left,right,n_th);
                }
            }
        }

    }
/*
     compare the partition component and if they are equal
     compares the lower components and so on until a strict comparison

     output:
     if arr[first_index] > arr[second] true
     if arr[first_index] < arr[second] false
    */
    private boolean isGreater(int first_index, int second_index) throws Exception {
        int k = partitionComponent;
        while( k>=0 && getValue(first_index,k) == getValue(second_index,k) )
            k--;
        boolean return_value=true;
        if ( k == -1 ) {
            if (arr.get(first_index).getVector().getRank() > arr.get(second_index).getVector().getRank())
                return true;
            else
                return false;
        }
        if(getValue(first_index,k) > getValue(second_index,k))
            return_value = true;
        else if(getValue(first_index,k) < getValue(second_index,k))
            return_value = false;

        return return_value;
    }

    private int partition(int left, int right, int pivotIndex) throws Exception {
        double pivotValue = getValue(pivotIndex,partitionComponent);
        Collections.swap(arr,pivotIndex,right);
        pivotIndex = right;
        int storeIndex = left;
        for(int i = left; i<right;i++){
            // checks if pivot is greater than current value at i
            if(isGreater(pivotIndex,i)){     //getValue(i,partitionComponent) < pivotValue
                Collections.swap(arr,storeIndex,i);
                storeIndex++;
            }

        }
        Collections.swap(arr,storeIndex,right);

        return storeIndex;
    }


    private int pivot(int left, int right) throws Exception {
        if( right - left < 5 )
            return partition5(left,right);
        else{
            int rightOfSubArr;
            int newIndex;
            int median5;

            for(int i =left; i<right;i+=5 ){
                rightOfSubArr = i+4;
                if (right < rightOfSubArr)
                    rightOfSubArr = right;

                median5 = partition5(i,rightOfSubArr);

                // moving the median of sublist to the start of list
                newIndex = left + (int)Math.floor((i-left)/5);
                Collections.swap(arr,median5,newIndex);

            }
            return select(left,left+(int)Math.ceil((right-left)/5)-1, left+ (int)(Math.floor(right-left)/10));
//            return select(sublist,0,sublist.size()-1, (int)Math.floor(sublist.size()/2));

        }
    }

    // using insertion sort to find the median of sub array of 5
    //insertion sort is a stable sort
    private int partition5(int left, int right) throws Exception {
        int j;
        for(int i=left; i<=right;i++){
            j=i;
            while (j>left && isGreater(j-1,j)){ //getValue(j-1,partitionComponent) > getValue(j,partitionComponent)
                Collections.swap(arr,j,j-1);
                j--;
            }
        }
        return left+(int)(Math.floor(right-left)/2);
    }

    // utility function which returns the value for the component
    private double getValue(int index, int component) throws Exception {
        if(index>=0 && index<arr.size())
            return arr.get(index).getVector().getComponent(component);
        else{
            throw new Exception("Component number d is out of bound ");
        }
    }

    // A utility function to print a element of specefic component
    private void printComponentList(int d,int left, int right) throws Exception {
        for(int i=left;i<=right;i++)
            System.out.print(arr.get(i).getVector().getComponent(d)+"    ");
        System.out.println();
    }
}

