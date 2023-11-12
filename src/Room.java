import java.util.ArrayList;
import java.util.List;

public class Room {
    public String id;
    public int type;
    public List<DaySched> scheds;

    public Room(String id, int type) {
        this.id = id;
        this.type = type;
        scheds = new ArrayList<>();
        for(int i = 1; i <= 5; i++) { // Adds Monday to Friday schedule
            scheds.add(new DaySched(i, this));
        }
        if(Timetable.include_saturday) {
            scheds.add(new DaySched(6, this)); // Adds Saturday schedule if include_saturday is enabled
        }
    }
}
