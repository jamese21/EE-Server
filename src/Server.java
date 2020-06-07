import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.*;

public class Server
{
    BlockingQueue<Runnable> queue;
    Classifier classifier = new Classifier();
    int transRate;
    int corePoolSize, maxPoolSize, queueSize;

    public Server(Classifier classifier, int corePoolSize, int maxPoolSize, int queueSize)
    {
        this.classifier = classifier;
        this.queue = new ArrayBlockingQueue<>(queueSize);
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
    }

}
