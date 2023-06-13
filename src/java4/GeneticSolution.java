package java4;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class GeneticSolution {
    private static int[][] matrix;
    private ArrayList<ArrayList<Integer>> population = new ArrayList<>();
    private int personsAmount;
    private F fMAX = new F();
    private F fMIN = new F();
    private ArrayList<Integer> path;
    private int mutationType;
    private int crossingType;
    private int localImprovementType;
    GeneticSolution(int[][] matrix, int personsAmount, ArrayList<Integer> path, int mutationType, int crossingType, int localImprovementType){
        this.personsAmount = personsAmount;
        this.path = path;
        this.mutationType = mutationType;
        this.crossingType = crossingType;
        this.localImprovementType = localImprovementType;
        this.matrix = matrix;
    }
    public void initializePopulation(){
        for(int i=0; i<personsAmount; i++){
            ArrayList<Integer> person = new ArrayList<>();
            person.add(0, path.get(0));
            ArrayList<Integer> unvisitedCities = new ArrayList<>();
            unvisitedCities.addAll(path);
            unvisitedCities.remove(0);
            for(int j=1; j<path.size(); j++){
                ArrayList<Integer> availableCities = getAvailableCities(person.get(j-1), person);
                if(!availableCities.isEmpty()){
                int randomIndex = ThreadLocalRandom.current().nextInt(0, availableCities.size());
                person.add(j, availableCities.get(randomIndex));
                unvisitedCities.remove(availableCities.get(randomIndex));
                }else{
                    int randomIndex = ThreadLocalRandom.current().nextInt(0, unvisitedCities.size());
                    person.add(j, unvisitedCities.get(randomIndex));
                    unvisitedCities.remove(randomIndex);
                }
            }
            person.add(path.size(), path.get(0));
            population.add(i, person);
        }
    }
    public ArrayList<Integer> getAvailableCities(int index, ArrayList<Integer> person){
        ArrayList<Integer> nextStep = new ArrayList<>();
        for(int j=0; j<matrix.length; j++){
            if(matrix[index][j]!=-1&&matrix[index][j]!=0&&!person.contains(j)){
                if(path.contains(j))nextStep.add(j);
            }
        }
        return nextStep;
    }
    public void printPopulation(){
        System.out.println();
        for(int i=0; i<personsAmount; i++){
            String line = "Person " + Integer.toString(i) + " | ";
            for(int j=0; j<path.size()+1; j++){
                line += population.get(i).get(j) + " ";
            }
            boolean cycle = isCycled(population.get(i));
            line += " cycle " + cycle;
            System.out.println(line);
        }
    }
    public boolean isCycled(ArrayList<Integer> person){
        int connections = 0;
        for(int i=0; i<person.size()-1; i++){
            if(matrix[person.get(i)][person.get(i+1)]!=-1)connections++;
        }
        if(connections==person.size()-1) return true;
        else return false;
    }
    public int geneticAlgorithm(){
       int[] parentsIndexes = getParents();
       System.out.println("Parents at indexes " + parentsIndexes[0] + " " + parentsIndexes[1]);
        System.out.println("Parent 1 ");
        printPerson(population.get(parentsIndexes[0]));
        System.out.println("Parent 2");
        printPerson(population.get(parentsIndexes[1]));
       ArrayList<Integer> child = crossing(parentsIndexes);
       System.out.println("Child after crossing");
       printPerson(child);
       System.out.println(isCycled(child));
       int mutationProbability = ThreadLocalRandom.current().nextInt(1, 11);
       if(mutationProbability==10) {
           System.out.println("Mutation, child after mutation ");
           child = mutation(child);
           printPerson(child);
       }
       population.set(fMIN.getIndex(), child);
       printPopulation();
       return fMAX.getDistance();
    }
    public ArrayList<Integer> crossing(int[] parentsIndexes){
        ArrayList<Integer> child = new ArrayList<>();
        ArrayList<Integer> child2 = new ArrayList<>();
        switch (crossingType){
            case 1:
                for(int i=0; i<path.size()/2; i++) {
                    child.add(i, population.get(parentsIndexes[0]).get(i));
                    child2.add(i, population.get(parentsIndexes[1]).get(i));
                }
                for(int k=1; k<path.size(); k++) {
                    if (!child.contains(population.get(parentsIndexes[1]).get(k))) child.add(population.get(parentsIndexes[1]).get(k));
                    if (!child2.contains(population.get(parentsIndexes[0]).get(k))) child2.add(population.get(parentsIndexes[0]).get(k));
                }
            break;
            case 2:
                for(int i=0; i<population.get(0).size(); i++){
                    if(i%2==0){
                        for(int j=0; j<population.get(0).size(); j++){
                            if(!child.contains(population.get(parentsIndexes[0]).get(j))){
                                child.add(i, population.get(parentsIndexes[0]).get(j));
                                break;
                            }
                        }
                        for(int j=0; j<population.get(0).size(); j++){
                            if(!child2.contains(population.get(parentsIndexes[1]).get(j))){
                                child2.add(i, population.get(parentsIndexes[1]).get(j));
                                break;
                            }
                        }
                    }else{
                        if(i%2!=0){
                            for(int j=0; j<population.get(0).size(); j++){
                                if(!child.contains(population.get(parentsIndexes[1]).get(j))){
                                    child.add(i, population.get(parentsIndexes[1]).get(j));
                                    break;
                                }
                            }
                            for(int j=0; j<population.get(0).size(); j++){
                                if(!child2.contains(population.get(parentsIndexes[0]).get(j))){
                                    child2.add(i, population.get(parentsIndexes[0]).get(j));
                                    break;
                                }
                            }
                        }
                    }
                }
            break;
            case 3:
                child.add(population.get(parentsIndexes[0]).get(0));
                child2.add(population.get(parentsIndexes[0]).get(0));
                for(int i=1; i<population.get(0).size()-1; i++){
                   if(i%2==0){
                       child.add(getConnection(child.get(i-1), population.get(parentsIndexes[0]), child));
                       child2.add(getConnection(child2.get(i-1), population.get(parentsIndexes[1]), child2));
                   }else{
                       child.add(getConnection(child.get(i-1), population.get(parentsIndexes[1]), child));
                       child2.add(getConnection(child2.get(i-1), population.get(parentsIndexes[0]), child2));
                   }
                }
            break;
        }
        child2.add(path.get(0));
        child.add(path.get(0));
        int fChild = calculateFitness(child);
        int fChild2 = calculateFitness(child2);
        if(fChild2<fChild){
            child.clear();
            child.addAll(child2);
        }
        if(!isCycled(child)||(child.equals(population.get(parentsIndexes[0]))&&child.equals(population.get(parentsIndexes[1])))) child = localImprovement(child);
        return child;
    }
    public int getConnection(int element, ArrayList<Integer> person, ArrayList<Integer> child){
        int unusedIndex = 0;
        int index = 0;
        boolean checker = false;
        boolean unused = false;
        for(int j=1; j<person.size(); j++){
            if(matrix[element][person.get(j)]>0&&(!child.contains(person.get(j)))){
                checker = true;
                index = j;
                break;
            }
            if(!child.contains(person.get(j))&&!unused){
                unused = true;
                unusedIndex = j;
            }
        }
        if(checker) return person.get(index);
        else return person.get(unusedIndex);
    }
    public int calculateFitness(ArrayList<Integer> person){
        if(!isCycled(person)) return Integer.MAX_VALUE;
        else {
            int distance = 0;
            for(int i=0; i<person.size()-1; i++){
                distance += matrix[person.get(i)][person.get(i+1)];
            }
            return distance;
        }
    }
    public int[] getParents() {
        int[] parentsIndexes = new int[2];
        setMaxF();
        setMinF();
        int randomParentIndex = fMAX.getIndex();
        while (randomParentIndex== fMAX.getIndex())randomParentIndex = ThreadLocalRandom.current().nextInt(0, personsAmount);
        parentsIndexes[0] = fMAX.getIndex();
        parentsIndexes[1] = randomParentIndex;
        return parentsIndexes;
    }
    public void setMaxF(){
        fMAX.setIndex(0);
        fMAX.setDistance(Integer.MAX_VALUE);
        for(int i=0; i<personsAmount; i++){
            if(isCycled(population.get(i))){
                int distance = 0;
                for(int j=0; j<path.size(); j++){
                    distance += matrix[population.get(i).get(j)][population.get(i).get(j+1)];
                }
                if(distance< fMAX.getDistance()){
                    fMAX.setDistance(distance);
                    fMAX.setIndex(i);
                }
            }
        }
        System.out.println("Max F at index " + fMAX.getIndex() + " distance " + fMAX.getDistance());
    }
    public void setMinF(){
        fMIN.setIndex(personsAmount-1);
        fMIN.setDistance(Integer.MIN_VALUE);
        for(int i=0; i<personsAmount; i++){
            if(!isCycled(population.get(i))&&(i!=fMAX.getIndex())){
                fMIN.setDistance(Integer.MAX_VALUE);
                fMIN.setIndex(i);
            }else{
                int distance = 0;
                for(int j=0; j<path.size(); j++){
                    distance += matrix[population.get(i).get(j)][population.get(i).get(j+1)];
                }
                if(distance> fMIN.getDistance()){
                    fMIN.setIndex(i);
                    fMIN.setDistance(distance);
                }
            }
        }
        System.out.println("Min F at index " + fMIN.getIndex() + " distance " + fMIN.getDistance());
    }
    public void printPerson(ArrayList<Integer> person){
    String line = "";
    for(int i=0; i<person.size(); i++){
        line += person.get(i) + " ";
    }
    System.out.println(line);
    }
    public ArrayList<Integer> mutation(ArrayList<Integer> person){
        switch (mutationType){
            case 1:
                System.out.println("Mutation 1");
            int randomIndex = ThreadLocalRandom.current().nextInt(1, path.size()-1);
            int temp = person.get(randomIndex);
            person.set(randomIndex, person.get(randomIndex+1));
            person.set(randomIndex+1, temp);
            break;
            case 2:
                System.out.println("Mutation 2");
                ArrayList<Integer> reverse = new ArrayList<>();
                for(int i=path.size(); i>=0; i--){
                    reverse.add(person.get(i));
                }
                person.clear();
                person.addAll(reverse);
                System.out.println(isCycled(person));
            break;
        }
        return person;
    }
    public ArrayList<Integer> localImprovement(ArrayList<Integer> child){
        ArrayList<Integer> person = new ArrayList<>();
        switch (localImprovementType){
            case 1:
                System.out.println("Local improvement ");
                person.add(0, path.get(0));
                ArrayList<Integer> unvisitedCities = new ArrayList<>();
                unvisitedCities.addAll(path);
                unvisitedCities.remove(0);
                for(int j=1; j<path.size(); j++){
                    ArrayList<Integer> availableCities = getAvailableCities(person.get(j-1), person);
                    if(!availableCities.isEmpty()){
                        int randomIndex = ThreadLocalRandom.current().nextInt(0, availableCities.size());
                        person.add(j, availableCities.get(randomIndex));
                        unvisitedCities.remove(availableCities.get(randomIndex));
                    }else{
                        int randomIndex = ThreadLocalRandom.current().nextInt(0, unvisitedCities.size());
                        person.add(j, unvisitedCities.get(randomIndex));
                        unvisitedCities.remove(randomIndex);
                    }
                }
                person.add(path.size(), path.get(0));
            break;
            case 2:
                System.out.println("Local improvement 2");
                printPerson(child);
                ArrayList<ArrayList<Integer>> edges = new ArrayList<>();
                ArrayList<Integer> edge = new ArrayList<>();
                for(int i=0; i<path.size(); i++){
                    edge.add(child.get(i));
                    if(!(matrix[child.get(i)][child.get(i+1)]>0)){
                        ArrayList<Integer> copyEdge = new ArrayList<>();
                        copyEdge.addAll(edge);
                        edges.add(copyEdge);
                        edge.clear();
                    }
                }
                edge.add(child.get(path.size()));
                edges.add(edge);
                for(int j=0; j<edges.size(); j++){
                    if(j==0||j==edges.size()-1||(j%2==0))edges.set(j, getReverse(edges.get(j)));
                }
                for(int i= edges.size()-1; i>=0; i--){
                    for(int j=0; j<edges.get(i).size(); j++){
                        person.add(edges.get(i).get(j));
                    }
                }
            break;
        }
        System.out.println("after");
        printPerson(person);
        System.out.println(isCycled(person));
        return person;
    }
    public ArrayList<Integer> getReverse(ArrayList<Integer> arr){
        ArrayList<Integer> newArr = new ArrayList<>();
        for(int i=arr.size()-1; i>=0; i--) newArr.add(arr.get(i));
        return newArr;
    }
}


