import java.util.List;

public class ModTimetable {
    public String name;
    private List<Room> rooms;
    private List<Course> courses;
    private List<Instructor> instructors;
    private List<Section> sections;

    private int interval;
    private int period_start;
    private int period_end;
    private List<Integer> excluded_periods;
    private int[] one_per_week_priority;
    private int[][] two_per_week_priority;
    private int[][] three_per_week_priority;
    private boolean include_saturday;
    private int number_of_room_types;
    private int max_instructor_minutes_per_day;
    private int max_student_minutes_per_day;
    private int max_room_minutes_per_day;
    private  int max_consecutive_minutes;

    ModTimetable(Timetable timetable) {
        this.name = timetable.name;
        this.rooms = timetable.rooms;
        this.courses = timetable.courses;
        this.instructors = timetable.instructors;
        this.sections = timetable.sections;
        this.interval = Timetable.interval;
        this.period_start = Timetable.period_start;
        this.period_end = Timetable.period_end;
        this.excluded_periods = Timetable.excluded_periods;
        this.one_per_week_priority = Timetable.one_per_week_priority;
        this.two_per_week_priority = Timetable.two_per_week_priority;
        this.three_per_week_priority = Timetable.three_per_week_priority;
        this.include_saturday = Timetable.include_saturday;
        this.number_of_room_types = Timetable.number_of_room_types;
        this.max_instructor_minutes_per_day = Timetable.max_instructor_minutes_per_day;
        this.max_student_minutes_per_day = Timetable.max_student_minutes_per_day;
        this.max_room_minutes_per_day = Timetable.max_room_minutes_per_day;
        this.max_consecutive_minutes = Timetable.max_consecutive_minutes;
    }

    public Room getRoom(int index) {
        if(index >= rooms.size()) return null;
        return rooms.get(index);
    }

    public Course getCourse(int index) {
        if(index >= courses.size()) return null;
        return courses.get(index);
    }

    public Instructor getInstructor(int index) {
        if(index >= instructors.size()) return null;
        return instructors.get(index);
    }

    public Section getSection(int index) {
        if(index >= sections.size()) return null;
        return sections.get(index);
    }

    public boolean printRoom(int index) {
        if(index >= rooms.size()) return false;
        Room r = rooms.get(index);
        System.out.println("ROOM " + r.id + " {");
        for(DaySched sched : r.scheds) {
            System.out.println("\tDAY " + sched.day_of_week + " [");
            for(Activity a : sched.activities) {
                if(a.instructor == null) {
                    System.out.print("\t\t" + a.course.name + "-" + a.instance + " (" + a.start_time + "-" + a.getEndTime() + ")");
                } else {
                    System.out.print("\t\t" + a.course.name + "-" + a.instance + " (" + a.start_time + "-" + a.getEndTime() + ") [" + a.instructor.name + "]");
                }
                if(a.section == null) {
                    System.out.print(" [N.S.]\n");
                } else {
                    System.out.print(" [" + a.section.id + "]\n");
                }
            }
            System.out.println("\t]");
        }
        System.out.println("}");
        return true;
    }

    public boolean printCourse(int index) {
        if(index >= courses.size()) return false;
        Course c = courses.get(index);
        c.printCourseSchedule();
        return true;
    }

    public boolean printInstructor(int index) {
        if(index >= instructors.size()) return false;
        Instructor i = instructors.get(index);
        i.printInstructorSchedule();
        return true;
    }

    public boolean printSection(int index) {
        if(index >= sections.size()) return false;
        Section s = sections.get(index);
        s.printSectionSchedule();
        return true;
    }

    public void printAllRooms() {
        for(Room r : rooms) {
            System.out.println("ROOM " + r.id + " {");
            for(DaySched sched : r.scheds) {
                System.out.println("\tDAY " + sched.day_of_week + " [");
                for(Activity a : sched.activities) {
                    if(a.instructor == null) {
                        System.out.print("\t\t" + a.course.name + "-" + a.instance + " (" + a.start_time + "-" + a.getEndTime() + ")");
                    } else {
                        System.out.print("\t\t" + a.course.name + "-" + a.instance + " (" + a.start_time + "-" + a.getEndTime() + ") [" + a.instructor.name + "]");
                    }
                    if(a.section == null) {
                        System.out.print(" [N.S.]\n");
                    } else {
                        System.out.print(" [" + a.section.id + "]\n");
                    }
                }
                System.out.println("\t]");
            }
            System.out.println("}");
        }
    }

    public void printAllCourses() {
        for(Course c : courses) {
            c.printCourseSchedule();
        }
    }

    public void printAllInstructors() {
        for(Instructor i : instructors) {
            i.printInstructorSchedule();
        }
    }

    public void printAllSections() {
        for(Section s : sections) {
            s.printSectionSchedule();
        }
    }
}
