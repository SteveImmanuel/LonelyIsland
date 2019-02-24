import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Thread;

public class LonelyIsland implements Runnable{
    private String filename;

    public LonelyIsland(String[] args){//ambil argumen
        if(args.length!=1){
            System.out.println("Cara run : java LonelyIsland <namafile>\n"+
            "File input diletakkan di folder bernama inputfiles");
            System.exit(0);
        }else{
            this.filename=args[0];
            System.out.println("Membaca file "+filename);
        }
    }

    public void findSink(Set<Integer> sinkIsland,island currentIsland){//mencari trap island
        currentIsland.setVisitStatus(1);//set status menjadi sedang dikunjungi
        int size=currentIsland.getSize();
        int childStatus=2;//default=2
        if (size==0){
            sinkIsland.add(currentIsland.getID());
        }else if (size==1){//tetangga hanya satu
            if(currentIsland.getNextIsland(0).getVisitStatus()==0||currentIsland.getNextIsland(0).getVisitStatus()==3){//boleh divisit
                findSink(sinkIsland, currentIsland.getNextIsland(0));
                childStatus=currentIsland.getNextIsland(0).getVisitStatus();
            }else if(currentIsland.getNextIsland(0).getVisitStatus()==1){//parent blm selesai divisit
                sinkIsland.add(currentIsland.getID());
                childStatus=3;
            }
        }else{
            boolean canTravel=false;
            for(int i=0;i<currentIsland.getSize();i++){
                if(currentIsland.getNextIsland(i).getVisitStatus()==0||currentIsland.getNextIsland(i).getVisitStatus()==3){//kunjungi yang blm dikunjungi
                    canTravel=true;
                    findSink(sinkIsland, currentIsland.getNextIsland(i));
                }else if(currentIsland.getNextIsland(i).getVisitStatus()==2){
                    canTravel=true;
                }
                if(currentIsland.getNextIsland(i).getVisitStatus()==3){
                    childStatus=3;
                }
            }
            if(!canTravel){//jika punya banyak tetangga tapi tidak bisa travel, berarti dia bagian siklik
                sinkIsland.add(currentIsland.getID());
                childStatus=3;
            }
        }
        currentIsland.setVisitStatus(childStatus);//set status sesuai childnya
    }

    public void printPath(island source,island dest,ArrayList<Integer> path,boolean[] visited){
        path.add(source.getID());
        visited[source.getID()-1]=true;
        int initialSize=path.size();
        for(int i=0;i<source.getSize();i++){
            if(initialSize<path.size()){
                path.subList(initialSize, path.size()).clear();
            }
            if(source.getNextIsland(i)==dest){
                path.add(dest.getID());
                System.out.println(path);
            }else if(!visited[source.getNextIsland(i).getID()-1]&&source.getNextIsland(i)!=dest){
                printPath(source.getNextIsland(i), dest, path, visited);
            }
        }
        visited[source.getID()-1]=false;
    }

    public void findSink(Set<Integer> sinkIsland,island currentIsland,ArrayList<Integer> path){//mencari trap island
        currentIsland.setVisitStatus(1);//set status menjadi sedang dikunjungi
        int size=currentIsland.getSize();
        int childStatus=2;//default=2
        path.add(currentIsland.getID());
        int initialSize=path.size();
        // System.out.println(initialSize);
        // System.out.println(path);
        if (size==0){
            sinkIsland.add(currentIsland.getID());
            System.out.println(path);
        }else if (size==1){//tetangga hanya satu
            if(currentIsland.getNextIsland(0).getVisitStatus()==0||currentIsland.getNextIsland(0).getVisitStatus()==3){//boleh divisit
                findSink(sinkIsland, currentIsland.getNextIsland(0),path);
                childStatus=currentIsland.getNextIsland(0).getVisitStatus();
            }else if(currentIsland.getNextIsland(0).getVisitStatus()==1){//parent blm selesai divisit
                sinkIsland.add(currentIsland.getID());
                childStatus=3;
                System.out.println(path);
            }
            if(initialSize<path.size()){
                path.subList(initialSize, path.size()).clear();
            }
        }else{
            boolean canTravel=false;
            for(int i=0;i<currentIsland.getSize();i++){
                if(currentIsland.getNextIsland(i).getVisitStatus()==0||currentIsland.getNextIsland(i).getVisitStatus()==3){//kunjungi yang blm dikunjungi
                    canTravel=true;
                    findSink(sinkIsland, currentIsland.getNextIsland(i),path);
                }else if(currentIsland.getNextIsland(i).getVisitStatus()==2){
                    canTravel=true;
                }
                if(currentIsland.getNextIsland(i).getVisitStatus()==3){
                    childStatus=3;
                }
                if(initialSize<path.size()){
                    path.subList(initialSize, path.size()).clear();
                }
            }
            if(!canTravel){//jika punya banyak tetangga tapi tidak bisa travel, berarti dia bagian siklik
                sinkIsland.add(currentIsland.getID());
                childStatus=3;
                System.out.println(path);
            }
        }
        currentIsland.setVisitStatus(childStatus);//set status sesuai childnya
    }

    public static void main(String[] args) throws Exception {//buat thread baru agar tidak stackoverflow
        new Thread(null, new LonelyIsland(args), "Main", 1 << 26).start();
    }
     
    public void run() {//jalankan thread baru
        int n_island=0,n_bridge=0,cur_island=0;
        ArrayList<island> islands=new ArrayList<island>();//sebagai graf
        Set<Integer> tempResult=new HashSet<Integer>();
        ArrayList<Integer> result;
        boolean[] visitStatus;//visitstatus untuk print semua path,
                              //mempercepat program agar tidak perlu mereset visitstatus setiap island
        try{//baca input dari file
            Scanner scanner = new Scanner(new File("inputfiles/"+filename));
            if(scanner.hasNextLine()){
                String[] buffer=scanner.nextLine().split(" ");
                n_island=Integer.parseInt(buffer[0]);
                n_bridge=Integer.parseInt(buffer[1]);
                cur_island=Integer.parseInt(buffer[2]);
                for(int i=0;i<n_island;i++){
                    islands.add(new island(i+1));//ID island dimulai dari 1
                }
                for(int i=0;i<n_bridge;i++){//bentuk graf
                    buffer=scanner.nextLine().split(" ");
                    islands.get(Integer.parseInt(buffer[0])-1).addIsland(islands.get(Integer.parseInt(buffer[1])-1));
                }
            }
            scanner.close();
        }catch(FileNotFoundException|ArrayIndexOutOfBoundsException e){
            if(e instanceof FileNotFoundException){
                System.out.println("Error: File tidak ditemukan");
            }else if(e instanceof ArrayIndexOutOfBoundsException){
                System.out.println("Error: Parameter input salah");
            }
            System.exit(0);
        }
        System.out.println("Baca file selesai");
        visitStatus=new boolean[n_island];
        long startTime = System.nanoTime();//catat waktu awal
        // findSink(tempResult,islands.get(cur_island-1));
        // result=new ArrayList<Integer>(tempResult);
        // Collections.sort(result);
        // System.out.println("Pulau yang membuat terjebak :");
        // System.out.println(result);
        // System.out.println("Semua path yang mungkin :");
        // for(int i=0;i<result.size();i++){//
        //     printPath(islands.get(cur_island-1), islands.get(result.get(i)-1),new ArrayList<Integer>(),visitStatus);
        // }
        System.out.println("Semua path yang mungkin :");
        findSink(tempResult,islands.get(cur_island-1),new ArrayList<Integer>());
        result=new ArrayList<Integer>(tempResult);
        Collections.sort(result);
        System.out.println("Pulau yang membuat terjebak :");
        System.out.println(result);
        long endTime = System.nanoTime();//catat waktu akhir
        System.out.println("Total waktu eksekusi : "+(endTime-startTime)/1000000+" ms");
        System.exit(0);
    }
}

class island{//representasi node
    private ArrayList<island> nextIsland;
    private int ID;
    private int visited; //0=belum dikunjungi,
                         //1=sudah dikunjungi dan sedang mengunjungi childnya,
                         //2=sudah selesai dikunjungi sampai ke child2nya
                         //3=island merupakan bagian dari suatu loop

    public island(int _ID){
        this.nextIsland = new ArrayList<island>();
        this.ID=_ID;
        this.visited=0;
    }
    public void addIsland(island _next){
        this.nextIsland.add(_next);
    }
    public void setVisitStatus(int i){
        this.visited=i;
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
    public int getVisitStatus(){
        return this.visited;
    }
}
