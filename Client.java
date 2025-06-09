import StableMulticast.*;

public class Client implements IStableMulticast
{
    private static StableMulticast stableMulticast;
    private static Client client;

    public static void main(String[] args)
    {
        client = new Client();
        String groupIP = "230.0.0.1";
        stableMulticast = new StableMulticast(groupIP, 2020, client);

        stableMulticast.msend("eu gosto de bolo", client);
    }

    @Override
    public void deliver(String msg)
    {
        System.out.println("Recebi " + msg);
    }
}
