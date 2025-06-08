package StableMulticast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StableMulticast
{
    private class Message
    {
        public String message;
        public String sender;
        public Map<String, Integer> vc;

        public Message(String fullMessage)
        {
            String[] parts = fullMessage.split(";");

            message = parts[0];
            sender = parts[1];

            vc = new HashMap<>();

            for (int i = 2; i < parts.length; i++)
            {
                String[] key_value = parts[i].split(":");
                vc.put(key_value[0], Integer.parseInt(key_value[1]));
            }
        }

        public Integer getTime()
        {
            return vc.get(sender);
        }
    }

    private String ip;
    private Integer port;
    private IStableMulticast client;

    private List<Message> messageBuffer;
    private Map<String, Map<String, Integer>> mc;

    public StableMulticast(String ip, Integer port, IStableMulticast client)
    {
        this.ip = ip;
        this.port = port;
        this.client = client;

        messageBuffer = new ArrayList<>();
        mc = new HashMap<>();
    }

    public void msend(String msg, IStableMulticast cliente)
    {

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

            if (message.getTime() <= getMinTimeColumn(message.sender))
            {
                it.remove();
            }
        }
    }

    private Integer getMinTimeColumn(String column)
    {
        Integer minValue = 1000000;

        for (Map<String, Integer> vc : mc.values())
        {
            if (vc.get(column) < minValue)
            {
                minValue = vc.get(column);
            }
        }

        return minValue;
    }
}
