import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Section {
    public String id;
    public int year_level;
    public List<Integer> section_courses;
    public List<DaySched> scheds;

    public Section(String id) { // For testing purpose only
        this.id = id;
    }

    public Section(String id, int[] section_courses) {
        this.id = id;
        this.section_courses = Arrays.stream(section_courses).boxed().toList();
        scheds = new ArrayList<>();
        for(int i = 1; i <= 5; i++) { // Adds Monday to Friday schedule
            scheds.add(new DaySched(i, Timetable.period_start, Timetable.period_end));
        }
        if(Timetable.include_saturday) {
            scheds.add(new DaySched(6, Timetable.period_start, Timetable.period_end)); // Adds Saturday schedule if include_saturday is enabled
        }
    }

    public void printSectionSchedule() {
        System.out.println(id + " () {");
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
