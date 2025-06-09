package StableMulticast;

class Message
{
    public String message;
    public String sender;
    public VectorClock vc;

    public Message(String fullMessage)
    {
        String[] parts = fullMessage.split(";");

        message = parts[0];
        sender = parts[1];

        vc = new VectorClock();

        for (int i = 2; i < parts.length; i++)
        {
            String[] key_value = parts[i].split(":");
            vc.add(key_value[0], Integer.parseInt(key_value[1]));
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
        obj += "VC: " + vc + "\n";

        return obj;
    }

    public static String serialize(String message, String ip, VectorClock vc)
    {
        String fullMessage = message;
        fullMessage += ";" + ip + ";";
        fullMessage += vc.serialize();

        return fullMessage;
    }
}
