import java.util.ArrayList;
import java.util.HashMap;

public class Classifier
{
    HashMap<Packet, Flow> map;
    String algorithm;

    public Classifier(String algorithm)
    {
        this.algorithm = algorithm;
        map = new HashMap<>();
    }
}
