import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Thread;

public class main implements Runnable{
    
    public static void findSink(ArrayList<island> restIsland,ArrayList<Integer> sinkIsland,island currentIsland){
        currentIsland.setVisited();
        int size=currentIsland.getSize();
        if (size==0){
            sinkIsland.add(currentIsland.getID());
            currentIsland.setSink();
        }else if (size==1){
            if(!currentIsland.getNextIsland(0).hasVisited()){
                findSink(restIsland, sinkIsland, currentIsland.getNextIsland(0));
            }else{
                sinkIsland.add(currentIsland.getID());
                currentIsland.setSink();
            }
        }else{
            for(int i=0;i<currentIsland.getSize();i++){
                if(!currentIsland.getNextIsland(i).hasVisited()){
                    findSink(restIsland, sinkIsland, currentIsland.getNextIsland(i));
                }
            }
        }
    }
    public static void main(String[] args) throws Exception {
        new Thread(null, new main(), "Main", 1 << 26).start();
    }
     
    public void run() {
        int n_island=0,n_bridge=0,cur_island=0;
        ArrayList<island> islands=new ArrayList<island>();
        ArrayList<Integer> result=new ArrayList<Integer>();
        // ArrayList<Integer> path=new ArrayList<Integer>();
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
                    for(int i=0;i<n_island;i++){
                        islands.add(new island(i+1));//ID island dimulai dari 1
                    }
                }else{
                    islands.get(Integer.parseInt(buffer[0])-1).addIsland(islands.get(Integer.parseInt(buffer[1])-1));
                }
            }
            scanner.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        System.out.printf("%d %d %d\n",n_island,n_bridge,cur_island);
        long startTime = System.nanoTime();
        findSink(islands,result,islands.get(cur_island-1));
        long endTime = System.nanoTime();
        Collections.sort(result);
        System.out.println(result);
        System.out.println("Time execution : "+(endTime-startTime)+" ns");
    }


}


class island{
    private ArrayList<island> nextIsland;
    private int ID;
    private boolean visited;
    private boolean sink;

    public island(int _ID){
        this.nextIsland = new ArrayList<island>();
        this.ID=_ID;
        this.visited=false;
        this.sink=false;
    }
    public void addIsland(island _next){
        this.nextIsland.add(_next);
    }
    public void setVisited(){
        this.visited=true;
    }
    public void setSink(){
        this.sink=true;
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
    public boolean hasVisited(){
        return this.visited;
    }
    public boolean isSink(){
        return this.sink;
    }
    public void print(){
        System.out.println(this.ID);
    }
}