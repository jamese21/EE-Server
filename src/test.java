import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.*;

public class test
{

    public static void main(String[] args)
    {

//        server.input();
        ArrayList<Packet> packets = new ArrayList<>();
        Server server = new Server(new Classifier("hi"), 6, 6, 50);

        ServerThreadPool executor = new ServerThreadPool(server.corePoolSize,
                server.maxPoolSize, 5000, TimeUnit.MILLISECONDS, server.queue);


        //Creating packets from CSV
        String csv = "/Users/jameseyre/Desktop/EEServer/src/Packets3.csv";
        String line = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csv))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] packetStrings = line.split(cvsSplitBy);
                packets.add(new Packet(packetStrings[0], packetStrings[1], packetStrings[2], Integer.parseInt(packetStrings[3])));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //sorting the packets
        Scanner scan = new Scanner(System.in);
        System.out.println("0 - FCFS, 1 - SP, 2 - WRR. Enter type: \n");
        int type = scan.nextInt();
        HashMap<Integer, ArrayList<Packet>> flows = new HashMap<>();

        switch (type){
            //FCFS
            case 0 :
                break;
            //SP
            case 1:
                for(Packet p : packets)
                {
                    if(flows.containsKey(p.getPriority()))
                    {
                        flows.get(p.getPriority()).add(p);
                    }
                    else
                    {
                        flows.put(p.getPriority(), new ArrayList<Packet>());
                        flows.get(p.getPriority()).add(p);
                    }
                }
                packets.clear();
                for(int i = 1; i <= 5; i++)
                {
                    packets.addAll(flows.get(i));
                }
                break;
            //WRR
            case 2:

                break;
            default:
                break;
        }




        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                //add something here to count or keep track of lost packets that couldn't fit into the queue
                executor.lostPackets++;
                System.out.println("FAIL");
            }
        });
        executor.prestartAllCoreThreads();
        //Creating the threads, threads used to mimic the limited processing power of server
        for (int i = 0; i < packets.size(); i++) {
            Runnable worker = new WorkerThread("Data: " + packets.get(i).getData() + ", MaxJitter: " +
                    packets.get(i).getMaxJitter() + ", MaxLatency: " + packets.get(i).getMaxLatency() + ", Priority: " + packets.get(i).getPriority());
            packets.get(i).setStartTime();
            executor.execute(worker);
            executor.totalPackets++;
            packets.get(i).setEndTime();
//            System.out.println("LATENCY: "+packets.get(i).getLatency());
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Total packets: " + executor.totalPackets);
        System.out.println("Lost packets: " + executor.lostPackets);
        System.out.println("Finished all threads");
    }
}
