import java.util.Arrays;
import java.util.List;

public class Course {
    public int id;
    public String name;
    public int minutes;
    public int weekly_meetings;
    public int classes_offered;
    public List<Integer> compatible_rooms;
    public int number_of_compatible_rooms;
    public static int count = 0;


    public Course(String name, int minutes, int weekly_meetings, int classes_offered, int[] compatible_rooms) {
        this.id = count;
        count++;
        this.name = name;
        this.minutes = minutes;
        this.weekly_meetings = weekly_meetings;
        this.classes_offered = classes_offered;
        this.compatible_rooms = Arrays.stream(compatible_rooms).boxed().toList(); // Allows for easier input
        this.number_of_compatible_rooms = compatible_rooms.length;
    }
}
