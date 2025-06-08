import StableMulticast.*;

public class Client implements IStableMulticast
{
    private static StableMulticast stableMulticast;
    private static Client client;

    public static void main(String[] args)
    {
        client = new Client();
        stableMulticast = new StableMulticast("192.168.0.1", 2020, client);

        stableMulticast.msend("eu gosto de bolo", client);
    }

    @Override
    public void deliver(String msg)
    {
        System.out.println("Recebi " + msg);
    }
}
