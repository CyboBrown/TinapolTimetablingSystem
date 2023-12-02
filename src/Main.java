import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Room> rooms = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        List<Instructor> instructors = new ArrayList<>();
        List<Section> sections = new ArrayList<>();

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
        courses.add(new Course("Math", 240, 2, 20, new int[] { 0, 1, 2, 3 })); // Course id would be 0
        courses.add(new Course("Science", 240, 2, 20, new int[] { 2 }));
        courses.add(new Course("Computer", 240, 2, 10, new int[] { 3 }));
        courses.add(new Course("English", 240, 2, 10, new int[] { 0, 1 })); // Course id would be 3
        instructors.add(new Instructor("Mrs. A", 420, 720, new int[] {0, 1, 2, 3}));
        instructors.add(new Instructor("Mrs. B", new int[] {0, 1, 2, 3})); // Array of compatible courses of instructors
        instructors.add(new Instructor("Mrs. C", new int[] {0, 1, 2, 3}));
        instructors.add(new Instructor("Mrs. D", new int[] {0, 1, 2, 3}));
        instructors.add(new Instructor("Mrs. E", new int[] {0, 1, 2, 3}));
        instructors.add(new Instructor("Mrs. F", new int[] {0, 1, 2, 3}));
        sections.add(new Section("F1", new int[] {0, 1, 2, 3})); // Array of required courses for the block section
        sections.add(new Section("F2", new int[] {0, 1, 2, 3}));
        sections.add(new Section("F3", new int[] {0, 1, 2, 3}));
        sections.add(new Section("F4", new int[] {0, 1, 2, 3}));
        sections.add(new Section("F5", new int[] {0, 1, 2, 3}));
        sections.add(new Section("F6", new int[] {0, 1, 2, 3}));
        sections.add(new Section("F7", new int[] {0, 1, 2, 3}));
        sections.add(new Section("F8", new int[] {0, 1, 2, 3}));
        sections.add(new Section("F9", new int[] {0, 1, 2, 3}));
        sections.add(new Section("F10", new int[] {0, 1, 2, 3}));

//        rooms.add(new Room("R01", 2));
//        rooms.add(new Room("R02", 1));
//        rooms.add(new Room("R03", 2));
//        rooms.add(new Room("R04", 0));
//        rooms.add(new Room("R05", 0));
//        rooms.add(new Room("R06", 3));
//        courses.add(new Course("Math", 120, 1, 53, new int[] { 0, 1, 2, 3 }));
//        courses.add(new Course("Science", 120, 1, 50, new int[] { 2 }));
//        courses.add(new Course("Computer", 120, 1, 24, new int[] { 3 }));
//        courses.add(new Course("English", 120, 1, 53, new int[] { 0, 1 }));
//        instructors.add(new Instructor("Mrs. A", new int[] {0, 1, 2, 3}, 2880)); // 2880 minutes = 48 hours
//        instructors.add(new Instructor("Mrs. B", new int[] {1, 2, 3, 0}, 2880));
//        instructors.add(new Instructor("Mrs. C", new int[] {2, 3, 1, 2}, 2880));
//        instructors.add(new Instructor("Mrs. D", new int[] {3, 0, 1, 2}, 2880));
//        instructors.add(new Instructor("Mrs. E", new int[] {0}, 2880));
//        instructors.add(new Instructor("Mrs. F", new int[] {1}, 2880));
//        instructors.add(new Instructor("Mrs. G", new int[] {2}, 2880));
//        instructors.add(new Instructor("Mrs. H", new int[] {3}, 2880));
//        instructors.add(new Instructor("Mrs. I", new int[] {0, 1, 2, 3}, 2880));

//        rooms.add(new Room("R01", 1));
//        rooms.add(new Room("R02", 0)); // 40
//        courses.add(new Course("Math", 240, 2, 20, new int[] { 0, 1 }));
//        courses.add(new Course("Science", 180, 3, 14, new int[] { 0 }));
//        instructors.add(new Instructor("Mrs. A", new int[] {0, 1}, 2880));
//        instructors.add(new Instructor("Mrs. B", new int[] {1, 0}, 2880));
//        instructors.add(new Instructor("Mrs. C", new int[] {1}, 2880));
//        instructors.add(new Instructor("Mrs. D", new int[] {0}, 2880));

//        rooms.add(new Room("R01", 1));
////        rooms.add(new Room("R02", 0));
//        courses.add(new Course("Math", 60, 1, 23, new int[] { 0, 1 }));
//        courses.add(new Course("English", 480, 4, 4, new int[] { 0, 1 }));
////        courses.add(new Course("Science", 240, 4, 10, new int[] { 0, 1 }));
//        instructors.add(new Instructor("Mrs. A", new int[] {0, 1}, 2880)); // 2880 minutes = 48 hours
//        instructors.add(new Instructor("Mrs. B", new int[] {1, 0}, 2880));
//        instructors.add(new Instructor("Mrs. C", new int[] {0}, 2880));
//        instructors.add(new Instructor("Mrs. D", new int[] {1}, 2880));

//        rooms.add(new Room("R01", 2));
//        rooms.get(0).scheds.get(2).printVacancy();
//        courses.add(new Course("Math", 120, 1, 6, new int[] { 2 }));
//        instructors.add(new Instructor("Mrs. A", new int[] {0, 1, 2, 3}));


//        //rooms
//        rooms.add(new Room("303 (Lecture)", 0));
//        rooms.add(new Room("201 (Lab)", 1))
//        //courses
//        courses.add(new Course("Automata Theory", 120, 2, 2, new int[] {0}));
//        courses.add(new Course("Intelligent Systems", 150, 2, 2, new int[] {1}));
//        courses.add(new Course("Information Management 2", 120, 3, 2, new int[] {1}));
//        courses.add(new Course("Quantitative Methods", 120, 2, 2, new int[] {0}));
//        courses.add(new Course("Systems Integration", 120, 3, 2, new int[] {1}));
//        //teachers
//        instructors.add(new Instructor("Mrs. Sta. Romana", new int[] {0}, 1440));
//        instructors.add(new Instructor("Mr. Aliac", new int[] {1}, 1440));
//        instructors.add(new Instructor("Mrs. Tulin", new int[] {2}, 1440));
//        instructors.add(new Instructor("Mrs. Bernus", new int[] {3}, 1440));
//        instructors.add(new Instructor("Mr. Padillo", new int[] {4}, 1440));

//        // Unaligned classes example
//        rooms.add(new Room("R01", 0));
//        courses.add(new Course("Math", 60, 1, 1, new int[] {0}));
//        courses.add(new Course("Science", 240, 2, 20, new int[] {0}));
//        instructors.add(new Instructor("Mr. Teach", new int[] {0, 1}));

        Timetable timetable = new Timetable(rooms, courses, instructors, sections);
//        timetable.printTimetable();
//        for(Instructor i : instructors) {
//            i.printInstructorSchedule();
//        }
//        for(Course c : courses) {
//            c.printCourseSchedule();
//        }
//        for(Section s : sections) {
//            s.printSectionSchedule();
//        }

        ModTimetable mt = new ModTimetable(timetable);
        mt.printAllRooms();
        mt.printAllInstructors();
        mt.printAllSections();
//        timetable.instructors.get(0).scheds.get(2).printVacancy();
//        timetable.rooms.get(0).scheds.get(2).printVacancy();
    }
}