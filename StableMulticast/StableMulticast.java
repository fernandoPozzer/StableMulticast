package StableMulticast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StableMulticast
{
    private String ip;
    private Integer port;
    private IStableMulticast client;

    private List<Message> messageBuffer;
    private MatrixClock mc;

    public StableMulticast(String ip, Integer port, IStableMulticast client)
    {
        this.ip = ip;
        this.port = port;
        this.client = client;

        messageBuffer = new ArrayList<>();
        
        VectorClock startVectorClock = new VectorClock();
        startVectorClock.add(ip, 0);
        
        mc = new MatrixClock();
        mc.addVectorClock(ip, startVectorClock);
    }

    public void msend(String msg, IStableMulticast cliente)
    {
        // VectorClock vc = new VectorClock();

        // vc.add("192.168.0.1", 10);
        // vc.add("192.70.0.1", 2);
        // vc.add("192.168.0.10", 3);

        // String fullMessage = Message.serialize(msg, ip, vc);
        // System.out.println(fullMessage);

        // Message m = new Message(fullMessage);
        // System.out.println("Deserializing Message:\n" + m);
    }

    private void lookForNewParticipants()
    {
        // usar ip multicast
        // add em vc
    }

    private void receiveMessage()
    {
        // Message m = new Message(msg);
        // vc[ip] = m.vc; como cópia

        checkMessagesToDiscard();
    }

    private void checkMessagesToDiscard()
    {
        Iterator<Message> it = messageBuffer.iterator();

        while (it.hasNext())
        {
            Message message = it.next();

            if (message.getTime() <= mc.getMinTimeOfColumn(message.sender))
            {
                it.remove();
            }
        }
    }
}
