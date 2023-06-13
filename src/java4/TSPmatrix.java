package java4;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class TSPmatrix {
    private int size ;
    private int matrix[][];
    public TSPmatrix(int size){
        this.size = size;
        this.matrix = new int[size][size];
    }
    public int[][] getMatrix(){
        return matrix;
    }
    public void generateMatrix(){
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                if(i==j) matrix[i][j] = 0;
                else matrix[i][j] = -1;
            }
        }
        ArrayList<Integer> usedVertex = new ArrayList<>();
        for(int i=0; i<size; i++){
            ArrayList<Integer> randomNeighbours = new ArrayList<>();
            int randomNumberOfNeighbours = ThreadLocalRandom.current().nextInt(50, 100);
            randomNeighbours.add(i);
            if(i==0) randomNumberOfNeighbours = 150;
            for(int k=0; k<randomNumberOfNeighbours; k++){
                    int newrandNum = randomNeighbours.get(0);
                     while(randomNeighbours.contains(newrandNum)) newrandNum = ThreadLocalRandom.current().nextInt(1, size);
                    randomNeighbours.add(newrandNum);
            }
            for(int j=0; j<size; j++){
               if(randomNeighbours.contains(j)&&j!=i){
                   if(matrix[i][j]==-1){
                   int distance = ThreadLocalRandom.current().nextInt(5, 151);
                   matrix[i][j] = distance;
                   matrix[j][i] = distance;
                   }
               }
            }
            if(!usedVertex.isEmpty()){
            int firstConnectionIndex = ThreadLocalRandom.current().nextInt(0, usedVertex.size());
            int firstConnection = usedVertex.get(firstConnectionIndex);
            if(calculateConnections(firstConnection)<size-3){
            if(matrix[i][firstConnection]==-1){
                int distance = ThreadLocalRandom.current().nextInt(5, 151);
                matrix[i][firstConnection] = distance;
                matrix[firstConnection][i] = distance;
            }
            }
            if(usedVertex.size()>1){
                int secondConnectionIndex = firstConnectionIndex;
                while(secondConnectionIndex==firstConnectionIndex) secondConnectionIndex = ThreadLocalRandom.current().nextInt(0, usedVertex.size());
                int secondConnection = usedVertex.get(secondConnectionIndex);
                if(calculateConnections(secondConnection)<size-3){
                if(matrix[i][secondConnection]==-1){
                    int distance = ThreadLocalRandom.current().nextInt(5, 151);
                    matrix[i][secondConnection] = distance;
                    matrix[secondConnection][i] = distance;
                }
                }
            }
            }
            for(int el:randomNeighbours){
                if(!usedVertex.contains(el)) usedVertex.add(el);
            }
        }
    }
    public int calculateConnections(int el){
        int result = 0;
        for(int i=0; i<size; i++){
             if(matrix[el][i]!=-1)result++;
        }
        return result;
    }
    public void printMatrix(){
        for(int i=0; i<size; i++){
            String line = "";
            String sp = "";
            for(int j=0; j<size; j++){
                if(matrix[i][j]>=100) sp = " ";
                else if(matrix[i][j]>=10||matrix[i][j]==-1) sp = "  ";
                else sp = "   ";
                line +=  matrix[i][j] + sp;

            }
            System.out.println(line);
        }

    }
}
