import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Room> rooms = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        List<Instructor> instructors = new ArrayList<>();

        // User set slots in Timetable (these attributes must be final before timetable generation and adding rooms/courses)
        // User Added Rooms and Courses

//        rooms.add(new Room("R01", 2)); // capacity is removed since it can be merged with room type
//        rooms.add(new Room("R02", 1)); // (optional) add feature to block certain periods in specific rooms
//        rooms.add(new Room("R03", 2));
//        rooms.add(new Room("R04", 0));
//        rooms.add(new Room("R05", 0));
//        rooms.add(new Room("R06", 3));
//        // Only minutes divisible by interval are accepted and weekly_meetings max is 6
//        // duration divided by weekly_meetings should also be divisible by interval //8
//        courses.add(new Course("Math", 240, 2, 20, new int[] { 0, 1, 2, 3 })); // Course id would be 0
//        courses.add(new Course("Science", 240, 2, 20, new int[] { 2 }));
//        courses.add(new Course("Computer", 240, 2, 10, new int[] { 3 }));
//        courses.add(new Course("English", 240, 2, 10, new int[] { 0, 1 })); // Course id would be 3
//        instructors.add(new Instructor("Mrs. A", 420, 720, new int[] {0, 1, 2, 3}));
//        instructors.add(new Instructor("Mrs. B", new int[] {0, 1, 2, 3}));
//        instructors.add(new Instructor("Mrs. C", new int[] {0, 1, 2, 3}));
//        instructors.add(new Instructor("Mrs. D", new int[] {0, 1, 2, 3}));
//        instructors.add(new Instructor("Mrs. E", new int[] {0, 1, 2, 3}));
//        instructors.add(new Instructor("Mrs. F", new int[] {0, 1, 2, 3}));

        rooms.add(new Room("R01", 2));
        rooms.add(new Room("R02", 1));
        rooms.add(new Room("R03", 2));
        rooms.add(new Room("R04", 0));
        rooms.add(new Room("R05", 0));
        rooms.add(new Room("R06", 3));
        courses.add(new Course("Math", 120, 1, 53, new int[] { 0, 1, 2, 3 }));
        courses.add(new Course("Science", 120, 1, 50, new int[] { 2 }));
        courses.add(new Course("Computer", 120, 1, 24, new int[] { 3 }));
        courses.add(new Course("English", 120, 1, 53, new int[] { 0, 1 }));
        instructors.add(new Instructor("Mrs. A", new int[] {0, 1, 2, 3}, 2880)); // 2880 minutes = 48 hours
        instructors.add(new Instructor("Mrs. B", new int[] {1, 2, 3, 0}, 2880));
        instructors.add(new Instructor("Mrs. C", new int[] {2, 3, 1, 2}, 2880));
        instructors.add(new Instructor("Mrs. D", new int[] {3, 0, 1, 2}, 2880));
        instructors.add(new Instructor("Mrs. E", new int[] {0}, 2880));
        instructors.add(new Instructor("Mrs. F", new int[] {1}, 2880));
        instructors.add(new Instructor("Mrs. G", new int[] {2}, 2880));
        instructors.add(new Instructor("Mrs. H", new int[] {3}, 2880));
        instructors.add(new Instructor("Mrs. I", new int[] {0, 1, 2, 3}, 2880));
//        instructors.add(new Instructor("Mrs. J", new int[] {0, 1, 2, 3}, 2880));
//        instructors.add(new Instructor("Mrs. K", new int[] {0, 1, 2, 3}, 2880));
//        instructors.add(new Instructor("Mrs. L", new int[] {0, 1, 2, 3}, 2880));
//        instructors.add(new Instructor("Mrs. M", new int[] {0, 1, 2, 3}, 2880));

//        rooms.add(new Room("R01", 1));
//        rooms.add(new Room("R02", 0));
//        courses.add(new Course("Math", 240, 2, 5, new int[] { 0, 1 }));
//        courses.add(new Course("Math", 240, 2, 5, new int[] { 0, 1 }));
//        courses.add(new Course("Science", 180, 3, 10, new int[] { 0 }));

//        rooms.add(new Room("R01", 1));
////        rooms.add(new Room("R02", 0));
//        courses.add(new Course("Math", 60, 1, 23, new int[] { 0, 1 }));
//        courses.add(new Course("English", 480, 4, 4, new int[] { 0, 1 }));
////        courses.add(new Course("Science", 240, 4, 10, new int[] { 0, 1 }));

//        rooms.add(new Room("R01", 2));
//        rooms.get(0).scheds.get(2).printVacancy();
//        courses.add(new Course("Math", 120, 1, 6, new int[] { 2 }));
//        instructors.add(new Instructor("Mrs. A", new int[] {0, 1, 2, 3}));


        Timetable timetable = new Timetable(rooms, courses, instructors);
        timetable.printTimetable();
        for(Instructor i : instructors) {
            i.printInstructorSchedule();
        }
//        timetable.instructors.get(0).scheds.get(2).printVacancy();
//        timetable.rooms.get(0).scheds.get(2).printVacancy();
    }
}