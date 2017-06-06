package NU_Alg;
/**
 * Created by emad on 08.04.17.
 */
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import NU_Alg.MaximaCompute.Vector;
import NU_Alg.NU_Core.ItemLabel;

public class ReadInput {
    private InputReader in;
    public ReadInput(String fileName){
        InputStream inputStream;

        try {
            inputStream = new FileInputStream(fileName);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        in = new InputReader(inputStream);

    }

    public ItemLabel[] readData(List<Vector> V) throws Exception {
        Vector vect;
        int N = in.nextInt();
        int d = in.nextInt();

        ItemLabel [] il = new ItemLabel[d];
        String inp;
        for(int k=0; k<d ;k++){
            inp = in.nextStr();
            if(inp.equals("W"))
                il[k] = ItemLabel.WEIGHT;
            else if(inp.equals("P"))
                il[k] = ItemLabel.PROFIT;
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


class InputReader{
    StringTokenizer tokenizer;
    BufferedReader reader;
    String skipLineChar;

    public InputReader(InputStream stream,String skipChar){
        reader = new BufferedReader(new InputStreamReader(stream));
        tokenizer = null;
        skipLineChar = skipChar;
    }

    public InputReader(InputStream stream){
        reader = new BufferedReader(new InputStreamReader(stream));
        tokenizer = null;
    }

    public InputReader(){

        reader = new BufferedReader(new InputStreamReader(System.in));
    }

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

    public String nextStr(){return next(); }
    public int nextInt(){
        return Integer.parseInt(next());
    }
    public long nextLong() {
        return Long.parseLong(next());
    }
    public double nextDouble(){
        return Double.parseDouble(next());
    }
}

