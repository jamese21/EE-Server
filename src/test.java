import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.*;

public class test
{
    public static void main(String[] args)
    {

//        server.input();
        ArrayList<Packet> packets = new ArrayList<>();
        Server server = new Server(new Classifier(), 2, 4, 20);

        ServerThreadPool executor = new ServerThreadPool(server.corePoolSize,
                server.maxPoolSize, 5000, TimeUnit.MILLISECONDS, server.queue);
        int lostPackets = 0;


        //Creating packets from CSV
        String csv = "D:/Users/James/IntelliJProjects/EEServer/src/Packets2.csv";
        String line = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csv))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] packetStrings = line.split(cvsSplitBy);
                packets.add(new Packet(packetStrings[0], packetStrings[1], packetStrings[2]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                //add something here to count or keep track of lost packets that couldn't fit into the queue
//                lostPackets++;
                System.out.println("FAIL");
            }
        });
        executor.prestartAllCoreThreads();
        //Creating the threads, threads used to mimic the limited processing power of server
        for (int i = 0; i < packets.size(); i++) {
            Runnable worker = new WorkerThread("Data: " + packets.get(i).getData() + ", MaxJitter: " +
                    packets.get(i).getMaxJitter() + ", MaxLatency: " + packets.get(i).getMaxLatency());
            packets.get(i).setStartTime();
            executor.execute(worker);
            packets.get(i).setEndTime();
//            System.out.println("LATENCY: "+packets.get(i).getLatency());
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");
    }
}
