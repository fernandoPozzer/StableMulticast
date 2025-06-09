package StableMulticast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StableMulticast
{
    private String clientIP;
    private IStableMulticast client;

    private List<Message> messageBuffer;
    private MatrixClock mc;

    private String multicastIP;
    private Integer port;

    private MulticastSocket multicastSocket;
    private DatagramSocket unicastSocket;

    public StableMulticast(String ip, Integer port, IStableMulticast client)
    {
        multicastIP = ip;
        this.port = port;
        this.client = client;

        messageBuffer = new ArrayList<>();
        VectorClock startVectorClock = new VectorClock();
        mc = new MatrixClock();

        try
        {
            // Inicia Matriz de Relogios

            clientIP = InetAddress.getLocalHost().getHostAddress();
            startVectorClock.add(clientIP, 0);
            mc.addVectorClock(clientIP, startVectorClock);

            // Entra no grupo multicast

            multicastSocket = new MulticastSocket(port);
            InetAddress grupoInet = InetAddress.getByName(ip);
            multicastSocket.joinGroup(grupoInet);

            // Cria socket para mensagens unicast

            unicastSocket = new DatagramSocket(port);

            // Cria threads

            Thread sendMulticastThread = new Thread(() -> sendNewParticipantMessage());
            Thread listenMulticastThread = new Thread(() -> receiveNewParticipantMessage());
            Thread listenUnicastThread = new Thread(() -> receiveMessage());

            sendMulticastThread.start();
            listenMulticastThread.start();
            listenUnicastThread.start();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    private void sendNewParticipantMessage()
    {
        try
        {
            byte[] data = clientIP.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(multicastIP), port);
            multicastSocket.send(packet);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void receiveNewParticipantMessage()
    {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        try
        {
            multicastSocket.receive(packet);
            String newParticipantIP =  new String(packet.getData(), 0, packet.getLength());

            System.out.println("Recebi Participante: " + newParticipantIP);
            mc.addNewParticipant(newParticipantIP);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void receiveMessage()
    {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        try
        {
            unicastSocket.receive(packet);
            String newMessageString =  new String(packet.getData(), 0, packet.getLength());

            Message newMessage = new Message(newMessageString);
            messageBuffer.add(newMessage);

            /// adicionar em MC
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        checkMessagesToDiscard();
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
