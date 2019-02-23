import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class main2{

    public static void findSink(ArrayList<island> restIsland,ArrayList<Integer> sinkIsland,island currentIsland,ArrayList<Integer> path){
        currentIsland.setVisited();
        if (currentIsland.getSize()==0){
            sinkIsland.add(currentIsland.getID());
            path.add(currentIsland.getID());
            System.out.println(path);
        }else{
            int neighborSize=currentIsland.getNeighbor().size();
            path.add(currentIsland.getID());
            for(int i=0;i<neighborSize;i++){
                if(!currentIsland.getNextIsland(i).hasVisited()){
                    ArrayList<Integer> tempPath=new ArrayList<Integer>(path);
                    findSink(restIsland, sinkIsland, currentIsland.getNextIsland(i),tempPath);
                }
            }
        }
    }

    public static void main(String[] args) {
        int n_island,n_bridge,cur_island;
        ArrayList<island> islands=new ArrayList<island>();
        ArrayList<Integer> result=new ArrayList<Integer>();
        ArrayList<Integer> path=new ArrayList<Integer>();       
        try{
            Scanner scanner = new Scanner(new File("input.txt"));
            boolean first=true;
            while(scanner.hasNextLine()){
                    String[] buffer=scanner.nextLine().split(" ");
                    if (first){
                        first=false;
                        n_island=Integer.parseInt(buffer[0]);
                        n_bridge=Integer.parseInt(buffer[1]);
                        cur_island=Integer.parseInt(buffer[2]);
                        System.out.printf("%d %d %d",n_island,n_bridge,cur_island);
                        for(int i=0;i<n_island;i++){
                            islands.add(new island(i+1));//ID island dimulai dari 1
                        }
                    }else{
                        islands.get(Integer.parseInt(buffer[0])-1).addIsland(islands.get(Integer.parseInt(buffer[1])-1));
                    }
                    // System.out.println(scanner.nextLine());
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        // }finally{
            // scanner.close();
        // }
        // long startTime = System.nanoTime();
        // findSink(islands,result,islands.get(cur_island-1),path);
        // long endTime = System.nanoTime();
        // Collections.sort(result);
        // System.out.println(result);
        // System.out.println("Time execution : "+(endTime-startTime)/1000000+" ms");
    }
}


class island{
    private ArrayList<island> nextIsland;
    private int ID;
    private boolean visited;

    public island(int _ID){
        this.nextIsland = new ArrayList<island>();
        this.ID=_ID;
        this.visited=false;
    }
    public void addIsland(island _next){
        this.nextIsland.add(_next);
    }
    public void setVisited(){
        this.visited=true;
    }
    public int getSize(){
        return this.nextIsland.size();
    }
    public int getID(){
        return this.ID;
    }
    public island getNextIsland(int i){
        return this.nextIsland.get(i);
    }
    public ArrayList<island> getNeighbor(){
        return this.nextIsland;
    }
    public boolean hasVisited(){
        return this.visited;
    }
    public void print(){
        System.out.println(this.ID);
    }
}