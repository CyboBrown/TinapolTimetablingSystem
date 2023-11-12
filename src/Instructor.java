import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Instructor {
    public int id;
    public String name;
    public int max_minutes;
    public List<Integer> compatible_courses;
    public List<DaySched> scheds; // You can customize teacher availability by modifying the period_start and period_end of each schedule (create your own constructor)
    private int total_minutes = 0;
    public static int count = 0;

    public Instructor(String name, int[] compatible_courses) {
        this.id = count;
        count++;
        this.name = name;
        this.max_minutes = Integer.MAX_VALUE;
        this.compatible_courses = Arrays.stream(compatible_courses).boxed().toList();
        scheds = new ArrayList<>();
        for(int i = 1; i <= 5; i++) { // Adds Monday to Friday schedule
            scheds.add(new DaySched(i, Timetable.period_start, Timetable.period_end));
        }
        if(Timetable.include_saturday) {
            scheds.add(new DaySched(6, Timetable.period_start, Timetable.period_end)); // Adds Saturday schedule if include_saturday is enabled
        }
    }

    public Instructor(String name, int[] compatible_courses, int max_minutes) {
        this.id = count;
        count++;
        this.name = name;
        this.max_minutes = max_minutes;
        this.compatible_courses = Arrays.stream(compatible_courses).boxed().toList();
        scheds = new ArrayList<>();
        for(int i = 1; i <= 5; i++) { // Adds Monday to Friday schedule
            scheds.add(new DaySched(i, Timetable.period_start, Timetable.period_end));
        }
        if(Timetable.include_saturday) {
            scheds.add(new DaySched(6, Timetable.period_start, Timetable.period_end)); // Adds Saturday schedule if include_saturday is enabled
        }
    }

    public Instructor(String name, int period_start, int period_end, int[] compatible_courses) {
        this.id = count;
        count++;
        this.name = name;
        this.max_minutes = Integer.MAX_VALUE;
        this.compatible_courses = Arrays.stream(compatible_courses).boxed().toList();
        scheds = new ArrayList<>();
        for(int i = 1; i <= 5; i++) { // Adds Monday to Friday schedule
            scheds.add(new DaySched(i, period_start, period_end));
        }
        if(Timetable.include_saturday) {
            scheds.add(new DaySched(6, period_start, period_end)); // Adds Saturday schedule if include_saturday is enabled
        }
    }

    public Instructor(String name, int period_start, int period_end, int[] compatible_courses, int max_minutes) {
        this.id = count;
        count++;
        this.name = name;
        this.max_minutes = max_minutes;
        this.compatible_courses = Arrays.stream(compatible_courses).boxed().toList();
        scheds = new ArrayList<>();
        for(int i = 1; i <= 5; i++) { // Adds Monday to Friday schedule
            scheds.add(new DaySched(i, period_start, period_end));
        }
        if(Timetable.include_saturday) {
            scheds.add(new DaySched(6, period_start, period_end)); // Adds Saturday schedule if include_saturday is enabled
        }
    }

    public boolean addMinutes(int minutes) {
        if(total_minutes + minutes <= max_minutes) {
            total_minutes += minutes;
            return true;
        }
        return false;
    }

    public void printInstructorSchedule() {
        System.out.println(name + " (" + total_minutes + ") {");
        for(DaySched sched : scheds) {
            System.out.println("\tDAY " + sched.day_of_week + " [");
            for(Activity a : sched.activities) {
                System.out.println("\t\t" + a.course.name + "-" + a.instance + " (" + a.start_time + "-" + a.getEndTime() + ") [" + a.room.id + "]");
            }
            System.out.println("\t]");
        }
        System.out.println("}");
    }
}
