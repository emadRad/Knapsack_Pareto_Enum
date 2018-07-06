package de.pareto_enum;
/**
 * Created by emad on 08.04.17.
 */
import java.io.*;
import java.util.List;
import java.util.StringTokenizer;
import de.pareto_enum.maxima_compute.Vector;
import de.pareto_enum.enum_core.DimLabel;



/**
 * The ReadInput class provides a set of methods for reading the input
 *  @author Emad Bahrami Rad
 * */
public class ReadInput {

    private InputReader in;


    /**
     * Constructs a ReadInput instance and initializes the FileInputStream
     * using the given file name.
     * @param fileName the input file name
     *
     * */
    public ReadInput(String fileName) throws FileNotFoundException {
        InputStream inputStream;

        try {
            inputStream = new FileInputStream(fileName);
        }
        catch (IOException e) {
            throw new FileNotFoundException("File "+fileName+" is not found");
        }
        in = new InputReader(inputStream);

    }


    /**
    * Reads the dimension labels and vectors from file and stores them in an array and list.
     * @param V a list of Vectors that the input will be stored in
     * @return an array containing label of each dimension
    *
    * */
    public DimLabel[] readData(List<Vector> V) throws Exception {
        Vector vect;
        int N = in.nextInt();
        int d = in.nextInt();

        DimLabel [] il = new DimLabel[d];
        String inp;
        for(int k=0; k<d ;k++){
            inp = in.nextStr();
            if(inp.equals("W"))
                il[k] = DimLabel.WEIGHT;
            else if(inp.equals("P"))
                il[k] = DimLabel.PROFIT;
        }

        for(int i=0;i<N;i++){
            vect = new Vector(d);
            for(int j=0;j<d;j++) {
                vect.setComponent(j,in.nextDouble());
            }
            V.add(i,vect);
        }
    return il;
    }


}

/**
 * InputReader class uses StringTokenizer to break the input into tokens and
 * provides a set of methods to read the next String,Int, Long or the whole line.
 *
 * */
class InputReader{
    StringTokenizer tokenizer;
    BufferedReader reader;
    String skipLineChar;


    /**
     * Constructs an InputReader instances and initializes a BufferedReader.
     * @param stream an instance of InputStream which is created from the input file
     * @param skipChar an String that determines which lines should be skipped from reading
     * */
    public InputReader(InputStream stream,String skipChar){
        reader = new BufferedReader(new InputStreamReader(stream));
        tokenizer = null;
        skipLineChar = skipChar;
    }

    public InputReader(InputStream stream){
        reader = new BufferedReader(new InputStreamReader(stream));
        tokenizer = null;
    }


    /**
     * Reads a line from the input stream.
     * @return the read line
     * */
    public String readLine(){
        try{
            return reader.readLine();
        }
        catch (IOException error){
            throw new RuntimeException(error);
        }
    }


    String next(){
        String line;
        while(tokenizer == null || !tokenizer.hasMoreTokens()) {
            try{
                if(skipLineChar==null)
                    tokenizer = new StringTokenizer(reader.readLine());
                else{
                    line= reader.readLine();
                    while(line.startsWith(skipLineChar))
                        line=reader.readLine();
                    tokenizer = new StringTokenizer(line);
                }
            }
            catch (IOException error){
                throw new RuntimeException(error);
            }

        }
        return tokenizer.nextToken();
    }

    /**
     * Reads the next string token.
     * @return the next string token.
     * */
    public String nextStr(){return next(); }

    /**
     * Reads the next int(type) token.
     * @return the next int token.
     * */
    public int nextInt(){
        return Integer.parseInt(next());
    }

    /**
     * Reads the next long(type) token.
     * @return the next long token.
     * */
    public long nextLong() {
        return Long.parseLong(next());
    }

    /**
     * Reads the next double token.
     * @return the next double token.
     * */
    public double nextDouble(){
        return Double.parseDouble(next());
    }
}

