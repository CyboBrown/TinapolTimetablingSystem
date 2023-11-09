import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Room> rooms = new ArrayList<>();
        List<Course> courses = new ArrayList<>();

        // User set slots in Timetable (these attributes must be final before timetable generation and adding rooms/courses)
        // User Added Rooms and Courses

        rooms.add(new Room("R01", 2)); // capacity is removed since it can be merged with room type
        rooms.add(new Room("R02", 1)); // (optional) add feature to block certain periods in specific rooms
        rooms.add(new Room("R03", 2));
        rooms.add(new Room("R04", 0));
        rooms.add(new Room("R05", 0));
        rooms.add(new Room("R06", 3));
        // Only minutes divisible by interval are accepted and weekly_meetings max is 6
        // duration divided by weekly_meetings should also be divisible by interval //8
        courses.add(new Course("M", "Math", 240, 2, 20, new int[] { 0, 1, 2, 3 }));
        courses.add(new Course("S", "Science", 240, 2, 20, new int[] { 2 }));
        courses.add(new Course("C", "Computer", 240, 2, 10, new int[] { 3 }));
        courses.add(new Course("E", "English", 240, 2, 10, new int[] { 0, 1 }));

//        rooms.add(new Room("R01", 2));
//        rooms.add(new Room("R02", 1));
//        rooms.add(new Room("R03", 2));
//        rooms.add(new Room("R04", 0));
//        rooms.add(new Room("R05", 0));
//        rooms.add(new Room("R06", 3));
//        courses.add(new Course("M", "Math", 120, 1, 53, new int[] { 0, 1, 2, 3 }));
//        courses.add(new Course("S", "Science", 120, 1, 50, new int[] { 2 }));
//        courses.add(new Course("C", "Computer", 120, 1, 24, new int[] { 3 }));
//        courses.add(new Course("E", "English", 120, 1, 53, new int[] { 0, 1 }));

//        rooms.add(new Room("R01", 1));
//        rooms.add(new Room("R02", 0));
//        courses.add(new Course("M", "Math", 240, 2, 5, new int[] { 0, 1 }));
//        courses.add(new Course("M", "Math", 240, 2, 5, new int[] { 0, 1 }));
//        courses.add(new Course("S", "Science", 180, 3, 10, new int[] { 0 }));

//        rooms.add(new Room("R01", 1));
////        rooms.add(new Room("R02", 0));
//        courses.add(new Course("M", "Math", 60, 1, 23, new int[] { 0, 1 }));
//        courses.add(new Course("E", "English", 480, 4, 4, new int[] { 0, 1 }));
////        courses.add(new Course("S", "Science", 240, 4, 10, new int[] { 0, 1 }));

        Timetable timetable = new Timetable(rooms, courses);
        timetable.printTimetable();
    }
}