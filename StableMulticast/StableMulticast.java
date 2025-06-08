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

        @Override
        public String toString()
        {
            String obj = "";

            obj += "Message: " + message + "\n";
            obj += "Sender: " + sender + "\n";
            obj += "VC: " + vc_toString(vc) + "\n";

            return obj;
        }

        private String vc_toString(Map<String, Integer> vc)
        {
            String result = "";

            for (Map.Entry<String, Integer> entry : vc.entrySet())
            {
                result += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }

            return result;
        }

        public static String serialize(String message, String ip, Map<String, Integer> vc)
        {
            String fullMessage = message;
            fullMessage += ";" + ip + ";";
            fullMessage += serializeVC(vc);

            return fullMessage;
        }

        private static String serializeVC(Map<String, Integer> vc)
        {
            String serialized = "";

            for (Map.Entry<String, Integer> entry : vc.entrySet())
            {
                serialized += entry.getKey() + ":" + entry.getValue() + ";";
            }

            return serialized.substring(0, serialized.length() - 1);
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
        Map<String, Integer> vc = new HashMap<>();

        vc.put("192.168.0.1", 10);
        vc.put("192.70.0.1", 2);
        vc.put("192.168.0.10", 3);

        String fullMessage = Message.serialize(msg, ip, vc);
        System.out.println(fullMessage);

        Message m = new Message(fullMessage);
        System.out.println("Deserializing Message:\n" + m);
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

    private String vc_toString(Map<String, Integer> vc)
    {
        String result = "";

        for (Map.Entry<String, Integer> entry : vc.entrySet())
        {
            result += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
        }

        return result;
    }

    private void print_mc()
    {
        for (Map.Entry<String, Map<String, Integer>> entry : mc.entrySet())
        {
            String ip = entry.getKey();
            Map<String, Integer> vc = entry.getValue();

            System.out.println(ip + ": " + vc_toString(vc));
        }
    }
}
