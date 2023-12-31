import java.util.ArrayList;
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
    public List<Activity> course_classes;
    public Course lecture_component;
    public static int count = 0;


    public Course(String name, int minutes, int weekly_meetings, int classes_offered, int[] compatible_rooms) { // Constructor for normal courses
        this.id = count;
        count++;
        this.name = name;
        this.minutes = minutes;
        this.weekly_meetings = weekly_meetings;
        this.classes_offered = classes_offered;
        this.compatible_rooms = Arrays.stream(compatible_rooms).boxed().toList(); // Allows for easier input
        this.number_of_compatible_rooms = compatible_rooms.length;
        this.course_classes = new ArrayList<>();
        this.lecture_component = null;
    }

    public Course(String name, int minutes, int weekly_meetings, int classes_offered, int[] compatible_rooms, Course lecture_component) { // Constructor for Lab Course
        this.id = count;
        count++;
        this.name = name + " (Lab)";
        lecture_component.name = name + " (Lec)";
        this.minutes = minutes;
        this.weekly_meetings = weekly_meetings;
        this.classes_offered = classes_offered;
        lecture_component.classes_offered = classes_offered;
        this.compatible_rooms = Arrays.stream(compatible_rooms).boxed().toList();
        this.number_of_compatible_rooms = compatible_rooms.length;
        this.course_classes = new ArrayList<>();
        this.lecture_component = lecture_component;
    }


    public Course(int minutes, int weekly_meetings, int[] compatible_rooms) { // Constructor for Lecture Component
        this.id = count;
        count++;
        this.name = "Unnamed Lecture";
        this.minutes = minutes;
        this.weekly_meetings = weekly_meetings;
        this.classes_offered = Integer.MAX_VALUE;
        this.compatible_rooms = Arrays.stream(compatible_rooms).boxed().toList();
        this.number_of_compatible_rooms = compatible_rooms.length;
        this.course_classes = new ArrayList<>();
        this.lecture_component = null;
    }

    public void printCourseSchedule() {
        System.out.println(name + " {");
        for(Activity a : course_classes) {
            System.out.println("\tDay " + a.sched.day_of_week + " - " + a.instance + " (" + a.start_time + "-" + a.getEndTime() + ") [" + a.room.id + "]");
        }
        System.out.println("}");
    }
}
