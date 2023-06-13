package java4;
import java.io.*;
import java.util.ArrayList;

public class Main {
    private static int cityAmount = 300;
    private static int[][] matrix;
    public static void main(String[] args) throws IOException {
    TSPmatrix tsp = new TSPmatrix(300);
//    tsp.generateMatrix();
//    matrix = tsp.getMatrix();
    matrix = new int[cityAmount][cityAmount];
    BufferedReader matrixReader = new BufferedReader(new FileReader("src/java4/matrix"));
    String line;
    int j = 0;
    while((line=matrixReader.readLine())!=null){
        String[] distance = line.split(" ");
        int matrixIndex = 0;
        for(int i=0; i<distance.length; i++){
           if(distance[i]!=""){
               matrix[j][matrixIndex] = Integer.parseInt(distance[i]);
               matrixIndex++;
           }
        }
        j++;
    }
   // tsp.printMatrix();
    printMatrix();
    System.out.println();
    ArrayList<Integer> path = new ArrayList<>();
    for(int i=0; i<300; i+=20){
        path.add(i);
    }

//    GeneticSolution G = new GeneticSolution(matrix, 10, path, 1, 1, 1);
//    G.initializePopulation();
//    G.printPopulation();
    BufferedWriter FMAXwriter =  new BufferedWriter(new FileWriter("src/java4/FMAX.txt"));
    ArrayList<ArrayList<Integer>> iterations100FMAX10 = new ArrayList<>();
    for(int k=0; k<10; k++){
        ArrayList<Integer> iterations100FMAX = new ArrayList<>();
        GeneticSolution G = new GeneticSolution(matrix,     100, path, 2, 2, 2);
        G.initializePopulation();
        G.printPopulation();
        int results = 0;
        for(int i=0; i<1000; i++) {
            results=G.geneticAlgorithm();
            if((i+1)%100==0) iterations100FMAX.add(results);
        }
        iterations100FMAX10.add(iterations100FMAX);
    }
    ArrayList<Integer> averFMAX = new ArrayList<>();
        for(int i=0; i<iterations100FMAX10.size(); i++){
            int sum = 0;
            for(int k=0; k<iterations100FMAX10.size(); k++){
                sum+= iterations100FMAX10.get(k).get(i);
            }
            int aver = sum/10;
            averFMAX.add(aver);
            FMAXwriter.append(" aver "+ i + " " + averFMAX.get(i)+"\n");
        }
    FMAXwriter.close();
    }

    public static void printMatrix(){
        for(int i=0; i<cityAmount; i++){
            String line = "";
            String sp = "";
            for(int j=0; j<cityAmount; j++){
                if(matrix[i][j]>=100) sp = " ";
                else if(matrix[i][j]>=10||matrix[i][j]==-1) sp = "  ";
                else sp = "   ";
                line +=  matrix[i][j] + sp;

            }
            System.out.println(line);
        }

    }
}
