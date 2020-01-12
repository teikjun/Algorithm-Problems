import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.ArrayList;


class Main {
    /* Pseudo code - Using ConcurrentHashMap
    1. Create 2 ConcurrentHashMap, leftHM for ranks smaller than team 1, 
                         rightHM for ranks equal or larger than team 1
        Start with rightHM filled with all teams except team 1, with points 0, penalty 0
    2. 
    A. If other team is updated, search leftHM and rightHM  O(1)
        If in leftHM: 
            Update its value.
        If in rightHM:
            Update its value. 
            If rank is smaller than team 1
                remove it from rightHM and insert it in leftHM O(1)
    B. If team 1 is updated,                                                 
                For each team in leftHM, if its rank is larger than team 1,             O(N)
                                move it to rightHM                                      O(1)
    */
    static int teamOnePoints = 0;
    static int teamOnePenalty = 0;
    static ConcurrentHashMap<Integer, ArrayList<Integer>> leftHM = new ConcurrentHashMap<>();
    static ConcurrentHashMap<Integer, ArrayList<Integer>> rightHM = new ConcurrentHashMap<>();
    
    public static void main(String[] args) {
        // scan input
        Kattio io = new Kattio(System.in, System.out);
        int teamCount = io.getInt();
        int eventCount = io.getInt();
        // initalize rightHM with all teams
        for (int j = 1; j < teamCount; j++) {
            ArrayList<Integer> list = new ArrayList<>();
            list.add(0);
            list.add(0);
            rightHM.put(j + 1, list);
        }
        // for each event, update the team's values, print out the rank of team 1
        for (int i = 0; i < eventCount; i++) {
            int teamNum = io.getInt();
            int penalty = io.getInt();
            // update team's values and print rank of team 1
            if (teamNum == 1) {
                // update team 1 values
                Main.updateTeamOne(penalty);
            } else {
                // update other team values
                Main.updateOther(teamNum, penalty);
            }
        }
    }

    public static void updateTeamOne(int penalty) {
        teamOnePoints += 1;
        teamOnePenalty += penalty;

        for (Map.Entry<Integer, ArrayList<Integer>> m : leftHM.entrySet()) {
            Integer teamNum = m.getKey();
            ArrayList<Integer> valueList = m.getValue();
            Integer otherPoints = valueList.get(0);
            Integer otherPenalty = valueList.get(1);
            if (!Main.isSmaller(otherPoints, otherPenalty)) {
                // teamOne is smaller. remove entry from leftHM, insert entry into rightHM
                rightHM.put(teamNum, valueList);
                leftHM.remove(teamNum);
            }
            // print rank 
            
           
        }
        System.out.println(leftHM.size() + 1);
    }

    public static void updateOther(int teamNum, int penalty) {
        ArrayList<Integer> valueList = leftHM.get(teamNum);
        if (valueList != null) {
            // teamNum is in leftHM, update team's values
            Integer newPoints = valueList.get(0) + 1;
            Integer newPenalty = valueList.get(1) + penalty;
            valueList.set(0, newPoints);
            valueList.set(1, newPenalty);
        } else {
            // teamNum is in rightHM
            valueList = rightHM.get(teamNum);
            assert(valueList != null);
            // update team's values
            Integer newPoints = valueList.get(0) + 1;
            Integer newPenalty = valueList.get(1) + penalty;
            valueList.set(0, newPoints);
            valueList.set(1, newPenalty);
            // if rank smaller than team 1, move entry to leftHM
            if (Main.isSmaller(newPoints, newPenalty)) {
                // remove from rightHM, move to leftHM
                leftHM.put(teamNum, valueList);
                rightHM.remove(teamNum);
            }
        }
        System.out.println(leftHM.size() + 1);
    }

    public static boolean isSmaller(int otherPoints, int otherPenalty) {
        if (otherPoints > teamOnePoints) {
            return true;
        } else if (otherPoints == teamOnePoints) {
            if (otherPenalty < teamOnePenalty) {
                return true;
            }
        }
        return false;
    }

}
