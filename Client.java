import StableMulticast.*;

public class Client implements IStableMulticast
{
    private static StableMulticast stableMulticast;
    private static Client client;

    public static void main(String[] args)
    {
        client = new Client();
        stableMulticast = new StableMulticast("null", 2020, client);
    }

    @Override
    public void deliver(String msg)
    {
        System.out.println("Recebi " + msg);
    }
}
