package StableMulticast;

import java.util.HashMap;
import java.util.Map;

class VectorClock
{
    public Map<String, Integer> vc;

    public VectorClock()
    {
        vc = new HashMap<>();
    }

    public void add(String ip)
    {
        vc.put(ip, -1);
    }

    public void add(String ip, Integer value)
    {
        vc.put(ip, value);
    }

    public Integer get(String ip)
    {
        return vc.get(ip);
    }

    public void copy(VectorClock original)
    {
        vc.putAll(original.vc);
    }

    public void incrementOne(String ip)
    {
        vc.put(ip, vc.get(ip) + 1);
    }

    public String serialize()
    {
        String serialized = "";

        for (Map.Entry<String, Integer> entry : vc.entrySet())
        {
            serialized += entry.getKey() + ":" + entry.getValue() + ";";
        }

        return serialized.substring(0, serialized.length() - 1);
    }

    @Override
    public String toString()
    {
        String result = "";

        for (Map.Entry<String, Integer> entry : vc.entrySet())
        {
            result += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
        }

        return result;
    }
}
