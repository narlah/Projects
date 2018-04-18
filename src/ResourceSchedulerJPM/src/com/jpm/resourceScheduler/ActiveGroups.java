package ResourceSchedulerJPM.src.com.jpm.resourceScheduler;

import java.util.ArrayList;
import java.util.HashMap;

public class ActiveGroups {
    private HashMap<Integer, Integer> activeGroupsHashMap = new HashMap<Integer, Integer>();

    public ActiveGroups() {
    }

    public void addActiveGroup(int activeGroupID) {
        if (activeGroupsHashMap.containsKey(activeGroupID)) {
            activeGroupsHashMap.put(activeGroupID, activeGroupsHashMap.get(activeGroupID) + 1);
        } else
            activeGroupsHashMap.put(activeGroupID, 1);
    }

    public void reduceActiveGroup(Integer activeGroupID) {
        if (activeGroupsHashMap.containsKey(activeGroupID))
            activeGroupsHashMap.put(activeGroupID, (activeGroupsHashMap.get(activeGroupID) - 1));
    }

    public boolean isGroupActive(int groupID) {
        return (activeGroupsHashMap.containsKey(groupID) && activeGroupsHashMap.get(groupID) > 0) ? true : false;
    }

    public Integer[] getActiveGroups() {
        if (activeGroupsHashMap.size() > 0) {
            ArrayList<Integer> result = new ArrayList<Integer>();
            for (int key : activeGroupsHashMap.keySet()) {
                if (activeGroupsHashMap.get(key) > 0) {
                    result.add(key);
                }
            }
            return result.toArray(new Integer[result.size()]);
        } else {
            return new Integer[0];
        }
    }
}
