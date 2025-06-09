package StableMulticast;

import java.util.HashMap;
import java.util.Map;

class MatrixClock
{
    Map<String, VectorClock> mc;
    
    public MatrixClock()
    {
        mc = new HashMap<>();
    }

    public Integer getMinTimeOfColumn(String column)
    {
        Integer minValue = 1000000;

        for (VectorClock vc : mc.values())
        {
            if (vc.get(column) < minValue)
            {
                minValue = vc.get(column);
            }
        }

        return minValue;
    }

    public void addVectorClock(String ip, VectorClock newVectorClock)
    {
        VectorClock copy = new VectorClock();
        copy.copy(newVectorClock);

        mc.put(ip, copy);
    }

    @Override
    public String toString()
    {
        String obj = "";

        for (Map.Entry<String, VectorClock> entry : mc.entrySet())
        {
            String ip = entry.getKey();
            VectorClock vc = entry.getValue();

            obj += ip + ": " + vc + "\n";
        }

        return obj;
    }
}
