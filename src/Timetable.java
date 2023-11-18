import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Timetable {
    public String name = "Sample Timetable";
    List<Room> rooms;
    List<Course> courses;
    List<Instructor> instructors;

    static int interval = 15; // interval in minutes (15, 30, 60)
    static int period_start = 420; // (7:00AM) - 7:30AM
    static int period_end = 1140; // 6:30PM - (7:00PM)
    static List<Integer> excluded_periods = Arrays.stream(new int[] {720, 750}).boxed().toList(); // 12:00PM, 12:30PM
    static final int[] one_per_week_priority = {3, 6, 1, 5, 2, 4}; // WED (MON to FRI except WED for 4 per week priority)
    static final int[][] two_per_week_priority = {{2, 4}, {1, 5}, {3, 6}}; // T-Th (high priority), M-F, W-S
    static final int[][] three_per_week_priority = {{1, 3, 5}, {2, 4, 6}}; // 5 per week priority would be M-W-F then T-Th-S
    static boolean include_saturday = true; // If false, W-S and T-Th-S are nada
    static int number_of_room_types = 4; // 0 - Normal, 1 - Large, 2 - ScienceLab, 3 - ComLab

    public Timetable(List<Room> rooms, List<Course> courses, List<Instructor> instructors) {
        this.rooms = rooms;
        this.courses = courses;
        this.instructors = instructors;
        // Distribute Courses to Rooms
        List<Course> temp0 = new ArrayList<>(courses);
        for(int i = 1; i <= Timetable.number_of_room_types || !temp0.isEmpty(); i++) { // Prioritizes courses with fewer compatible rooms
            for(int j = 0; j < temp0.size(); j++) {
                Course c = temp0.get(j);
                if(c.number_of_compatible_rooms == i) {
                    putCourseToRooms(c, rooms);
                    temp0.remove(c);
                    j--;
                }
            }
        }
        // Distribute Instructors to Classes
        List<Instructor> temp1 = new ArrayList<>(instructors);
        for(int i = 0; i < Course.count || !temp1.isEmpty(); i++) { // Prioritizes instructors with fewer compatible courses
            for(int j = 0; j < temp1.size(); j++) {
                Instructor ins = temp1.get(j);
                if(ins.compatible_courses.size() == i) {
                    putInstructorToRooms(ins, rooms, courses);
                    temp1.remove(ins);
                    j--;
                }
            }
        }
    }

    private static void putCourseToRooms(Course c, List<Room> rooms) {
        int classes_offered = c.classes_offered;
        int instance = 1;
        switch(c.weekly_meetings) {
            case 1:
                for(int i = 0; i < c.number_of_compatible_rooms; i++) {
                    for(Room r : rooms) {
                        if(r.type == c.compatible_rooms.get(i)) { // Loops through all compatible rooms based on the order of the course's compatible_rooms list
                            for(int j = 0; j < one_per_week_priority.length; j++) { // Loops through all available days based on priority and once the priority date is filled, it will proceed to the next priority
                                if(!include_saturday && one_per_week_priority[j] == 6) { // Skips saturday if include_saturday is disabled
                                    continue;
                                }
                                DaySched sched = r.scheds.get(Timetable. one_per_week_priority[j] - 1); // -1 because Sunday was not included in the scheds array of Room
                                while(classes_offered != 0) { // Add classes of the same course to the rooms until all classes have been added to the schedule
                                    boolean success = sched.addActivity(c, c.minutes, instance);
                                    if(success) {
                                        classes_offered--;
                                        instance++;
                                    } else { // Runs the following code if adding of classes is unsuccessful
                                        break;
                                    }
                                }
                                if(classes_offered == 0) break; // Proceeds to next priority day if day is full
                            }
                        }
                        if(classes_offered == 0) break; // Proceeds to next priority room of the same type if room is full
                    }
                    if(classes_offered == 0) break; // Proceeds to the next priority type of rooms if the current type of rooms are all occupied
                }
                break;
            case 2:
                int duration = c.minutes / 2; // Divides the total hours per week to the number of meetings per week
                for(int i = 0; i < c.number_of_compatible_rooms; i++) {
                    for(Room r : rooms) {
                        if(r.type == c.compatible_rooms.get(i)) {
                            for(int j = 0; j < two_per_week_priority.length; j++) {
                                if(!include_saturday && (two_per_week_priority[j][0] == 6 || two_per_week_priority[j][1] == 6)) {
                                    continue;
                                }
                                DaySched sched1 = r.scheds.get(Timetable.two_per_week_priority[j][0] - 1);
                                DaySched sched2 = r.scheds.get(Timetable.two_per_week_priority[j][1] - 1);
                                while(classes_offered != 0) {
                                    boolean success1 = sched1.checkConflict(duration); // Check conflicts before adding to make sure that all meetings in a particular class of the course are added
                                    boolean success2 = sched2.checkConflict(duration);
                                    if(success1 & success2) {
                                        sched1.addActivity(c, duration, instance);
                                        sched2.addActivity(c, duration, instance);
                                        classes_offered--;
                                        instance++;
                                    } else {
                                        break;
                                    }
                                }
                                if(classes_offered == 0) break;
                            }
                        }
                        if(classes_offered == 0) break;
                    }
                    if(classes_offered == 0) break;
                }
                break;
            case 3:
                duration = c.minutes / 3;
                for(int i = 0; i < c.number_of_compatible_rooms; i++) {
                    for(Room r : rooms) {
                        if(r.type == c.compatible_rooms.get(i)) {
                            for(int j = 0; j < three_per_week_priority.length; j++) {
                                if(!include_saturday && (three_per_week_priority[j][0] == 6 || three_per_week_priority[j][1] == 6 || three_per_week_priority[j][2] == 6)) {
//                                    System.out.println("Skip");
//                                    System.out.println(!include_saturday + " " + (three_per_week_priority[j][0] == 6) + " " + (three_per_week_priority[j][1] == 6) + " " + (three_per_week_priority[j][2] == 6));
                                    continue;
                                }
                                DaySched sched1 = r.scheds.get(Timetable.three_per_week_priority[j][0] - 1);
                                DaySched sched2 = r.scheds.get(Timetable.three_per_week_priority[j][1] - 1);
                                DaySched sched3 = r.scheds.get(Timetable.three_per_week_priority[j][2] - 1);
                                while(classes_offered != 0) {
                                    boolean success1 = sched1.checkConflict(duration);
                                    boolean success2 = sched2.checkConflict(duration);
                                    boolean success3 = sched3.checkConflict(duration);
                                    if(success1 & success2 & success3) {
                                        sched1.addActivity(c, duration, instance);
                                        sched2.addActivity(c, duration, instance);
                                        sched3.addActivity(c, duration, instance);
                                        classes_offered--;
                                        instance++;
                                    } else {
//                                        System.out.println(c.name + "-" + (c.classes_offered - classes_offered) + ": " + success1 + " " + success2 + " " + success3);
                                        break;
                                    }
                                }
                                if(classes_offered == 0) break;
                            }
                        }
                        if(classes_offered == 0) break;
                    }
                    if(classes_offered == 0) break;
                }
                break;
            case 4:
                duration = c.minutes / 4;
                for(int i = 0; i < c.number_of_compatible_rooms; i++) {
                    for(Room r : rooms) {
                        if(r.type == c.compatible_rooms.get(i)) {
                            for(int j = 0; j < one_per_week_priority.length; j++) {
                                DaySched[] sched = new DaySched[4];
                                for(int k = 0, temp = 0; k < 4; k++, temp++) { // Loops through Monday to Friday but skips the current one_per_week_priority day
                                    if(temp == Timetable.one_per_week_priority[j] - 1) {
                                        temp++;
                                    }
                                    sched[k] = r.scheds.get(temp);
                                }
                                while(classes_offered != 0) {
                                    boolean success1 = sched[0].checkConflict(duration);
                                    boolean success2 = sched[1].checkConflict(duration);
                                    boolean success3 = sched[2].checkConflict(duration);
                                    boolean success4 = sched[3].checkConflict(duration);
                                    if(success1 & success2 & success3 & success4) {
                                        for(int k = 0; k < 4; k++) {
                                            sched[k].addActivity(c, duration, instance);
                                        }
                                        classes_offered--;
                                        instance++;
                                    } else {
                                        break;
                                    }
                                }
                                if(classes_offered == 0) break;
                            }
                        }
                        if(classes_offered == 0) break;
                    }
                    if(classes_offered == 0) break;
                }
                break;
            case 5:
                duration = c.minutes / 5;
                for(int i = 0; i < c.number_of_compatible_rooms; i++) {
                    for(Room r : rooms) {
                        if(r.type == c.compatible_rooms.get(i)) { // Directly add classes from Monday to Friday
                            DaySched sched1 = r.scheds.get(0);
                            DaySched sched2 = r.scheds.get(1);
                            DaySched sched3 = r.scheds.get(2);
                            DaySched sched4 = r.scheds.get(3);
                            DaySched sched5 = r.scheds.get(4);
                            while(classes_offered != 0) {
                                boolean success1 = sched1.checkConflict(duration);
                                boolean success2 = sched2.checkConflict(duration);
                                boolean success3 = sched3.checkConflict(duration);
                                boolean success4 = sched4.checkConflict(duration);
                                boolean success5 = sched5.checkConflict(duration);
                                if(success1 & success2 & success3 & success4 & success5) {
                                    sched1.addActivity(c, duration, instance);
                                    sched2.addActivity(c, duration, instance);
                                    sched3.addActivity(c, duration, instance);
                                    sched4.addActivity(c, duration, instance);
                                    sched5.addActivity(c, duration, instance);
                                    classes_offered--;
                                    instance++;
                                } else {
                                    break;
                                }
                            }
                        }
                        if(classes_offered == 0) break;
                    }
                    if(classes_offered == 0) break;
                }
                break;
            case 6:
                if(!include_saturday) break; // "Saturday" inputs should be blocked if include_saturday is disabled
                duration = c.minutes / 6;
                for(int i = 0; i < c.number_of_compatible_rooms; i++) {
                    for(Room r : rooms) {
                        if(r.type == c.compatible_rooms.get(i)) { // Directly add classes from Monday to Saturday
                            DaySched sched1 = r.scheds.get(0);
                            DaySched sched2 = r.scheds.get(1);
                            DaySched sched3 = r.scheds.get(2);
                            DaySched sched4 = r.scheds.get(3);
                            DaySched sched5 = r.scheds.get(4);
                            DaySched sched6 = r.scheds.get(5);
                            while(classes_offered != 0) {
                                boolean success1 = sched1.checkConflict(duration);
                                boolean success2 = sched2.checkConflict(duration);
                                boolean success3 = sched3.checkConflict(duration);
                                boolean success4 = sched4.checkConflict(duration);
                                boolean success5 = sched5.checkConflict(duration);
                                boolean success6 = sched6.checkConflict(duration);
                                if(success1 & success2 & success3 & success4 & success5 & success6) {
                                    sched1.addActivity(c, duration, instance);
                                    sched2.addActivity(c, duration, instance);
                                    sched3.addActivity(c, duration, instance);
                                    sched4.addActivity(c, duration, instance);
                                    sched5.addActivity(c, duration, instance);
                                    sched6.addActivity(c, duration, instance);
                                    classes_offered--;
                                    instance++;
                                } else {
                                    break;
                                }
                            }
                        }
                        if(classes_offered == 0) break;
                    }
                    if(classes_offered == 0) break;
                }
                break;
            default:
                System.out.println("Invalid weekly_meeting value!");
                break;
        }
    }

    private static void putInstructorToRooms(Instructor t, List<Room> rooms, List<Course> courses) {
//        System.out.println("~~~~~" + t.name + "~~~~~");
        for(int i = 0; i < t.compatible_courses.size(); i++) {
            Course current_course = courses.get(t.compatible_courses.get(i));
//            System.out.println(current_course.name);
            for(Room r : rooms) {
                for(DaySched sched1 : r.scheds) {
                    DaySched t_sched1 = t.scheds.get(sched1.day_of_week - 1);
                    switch (current_course.weekly_meetings) {
                        case 1:
                            for(Activity a : sched1.activities) {
                                if(
                                    a.instructor == null &&
                                    t_sched1.checkVacant(a.start_time, a.duration) &&
                                    a.course == current_course &&
                                    t.addMinutes(current_course.minutes)
                                ) {
                                    a.instructor = t; // Sets activity instructor to current teacher
                                    t_sched1.addExistingActivity(a); // Adds activity to teacher schedule
//                                    System.out.println("Successfully added teacher to " + a.course.name + "-" + a.instance);
                                }
                            }
                            break;
                        case 2:
                            int pair_day = -1;
                            for (int[] ints : two_per_week_priority) {
                                if (ints[0] == sched1.day_of_week) {
                                    pair_day = ints[1];
                                    break;
                                }
                                if (ints[1] == sched1.day_of_week) {
                                    pair_day = ints[0];
                                    break;
                                }
                            }
                            DaySched sched2 = r.scheds.get(pair_day - 1);
                            DaySched t_sched2 = t.scheds.get(pair_day - 1);
//                            System.out.println(sched1.room.id);
                            for(Activity a : sched1.activities) {
                                Activity b = null;
                                for(Activity act : sched2.activities) {
//                                    System.out.println("Passed here");
                                    if(act.instance == a.instance && act.course == a.course) {
                                        b = act;
                                        break;
                                    }
                                }
//                                System.out.println((b != null) + " " +(a.instructor == null) + " " +(b.instructor == null) + " " +(t_sched1.checkVacant(a.start_time, a.duration)) + " " +(t_sched2.checkVacant(b.start_time, b.duration)) + " " +(a.course == current_course) + " " +(b.course == current_course));
//                                if(a.instructor != null) System.out.println(a.instructor.name);
//                                if(b.instructor != null) System.out.println(b.instructor.name);
                                if(
                                    b != null &&
                                    a.instructor == null &&
                                    b.instructor == null &&
                                    t_sched1.checkVacant(a.start_time, a.duration) &&
                                    t_sched2.checkVacant(b.start_time, b.duration) &&
                                    a.course == current_course &&
                                    b.course == current_course &&
                                    t.addMinutes(current_course.minutes)
                                ) {
                                    a.instructor = t;
                                    b.instructor = t;
                                    t_sched1.addExistingActivity(a);
                                    t_sched2.addExistingActivity(b);

                                }
                            }
                            break;
                        case 3:
                            int[] trio_days = new int[2];
                            for (int[] ints : three_per_week_priority) {
                                if (ints[0] == sched1.day_of_week) {
                                    trio_days[0] = ints[1];
                                    trio_days[1] = ints[2];
                                    break;
                                }
                                if (ints[1] == sched1.day_of_week) {
                                    trio_days[0] = ints[0];
                                    trio_days[1] = ints[2];
                                    break;
                                }
                                if (ints[2] == sched1.day_of_week) {
                                    trio_days[0] = ints[0];
                                    trio_days[1] = ints[1];
                                    break;
                                }
                            }
//                            System.out.println(sched1.day_of_week + "-" + trio_days[0] + "-" + trio_days[1]);
                            sched2 = r.scheds.get(trio_days[0] - 1);
                            DaySched sched3 = r.scheds.get(trio_days[1] - 1);
                            t_sched2 = t.scheds.get(trio_days[0] - 1);
                            DaySched t_sched3 = t.scheds.get(trio_days[1] - 1);
                            for(Activity a : sched1.activities) {
                                Activity b = null;
                                Activity c = null;
                                for(Activity act : sched2.activities) {
                                    if(act.instance == a.instance && act.course == a.course) {
                                        b = act;
                                        break;
                                    }
                                }
                                for(Activity act : sched3.activities) {
                                    if(act.instance == a.instance && act.course == a.course) {
                                        c = act;
                                        break;
                                    }
                                }
                                if(
                                    b != null &&
                                    c != null &&
                                    a.instructor == null &&
                                    b.instructor == null &&
                                    c.instructor == null &&
                                    t_sched1.checkVacant(a.start_time, a.duration) &&
                                    t_sched2.checkVacant(b.start_time, b.duration) &&
                                    t_sched3.checkVacant(c.start_time, c.duration) &&
                                    a.course == current_course &&
                                    b.course == current_course &&
                                    c.course == current_course &&
                                    t.addMinutes(current_course.minutes)
                                ) {
                                    a.instructor = t;
                                    b.instructor = t;
                                    c.instructor = t;
                                    t_sched1.addExistingActivity(a);
                                    t_sched2.addExistingActivity(b);
                                    t_sched3.addExistingActivity(c);
                                }
                            }
                            break;
                        case 4: // TODO: Improve code
                            int current_day = sched1.day_of_week;
                            int[] pair_days = new int[3];
                            for(int j = 1, k = 0; j <= 5; j++) {
                                if(j == current_day) continue;
                                if(k >= 3) break;
                                for(Activity a : r.scheds.get(j - 1).activities) {
                                    if(a.course.equals(current_course)) {
                                        pair_days[k] = j;
                                        k++;
                                        break;
                                    }
                                }
                            }
                            sched2 = r.scheds.get(pair_days[0] - 1);
                            sched3 = r.scheds.get(pair_days[1] - 1);
                            DaySched sched4 = r.scheds.get(pair_days[2] - 1);
                            t_sched2 = t.scheds.get(pair_days[0] - 1);
                            t_sched3 = t.scheds.get(pair_days[1] - 1);
                            DaySched t_sched4 = t.scheds.get(pair_days[2] - 1);
                            for(Activity a : sched1.activities) {
                                Activity b = null;
                                Activity c = null;
                                Activity d = null;
                                for(Activity act : sched2.activities) {
                                    if(act.instance == a.instance && act.course == a.course) {
                                        b = act;
                                        break;
                                    }
                                }
                                for(Activity act : sched3.activities) {
                                    if(act.instance == a.instance && act.course == a.course) {
                                        c = act;
                                        break;
                                    }
                                }
                                for(Activity act : sched4.activities) {
                                    if(act.instance == a.instance && act.course == a.course) {
                                        d = act;
                                        break;
                                    }
                                }
                                if(
                                        b != null &&
                                        c != null &&
                                        d != null &&
                                        a.instructor == null &&
                                        b.instructor == null &&
                                        c.instructor == null &&
                                        d.instructor == null &&
                                        t_sched1.checkVacant(a.start_time, a.duration) &&
                                        t_sched2.checkVacant(b.start_time, b.duration) &&
                                        t_sched3.checkVacant(c.start_time, c.duration) &&
                                        t_sched4.checkVacant(d.start_time, d.duration) &&
                                        a.course == current_course &&
                                        b.course == current_course &&
                                        c.course == current_course &&
                                        d.course == current_course &&
                                        t.addMinutes(current_course.minutes)
                                ) {
                                    a.instructor = t;
                                    b.instructor = t;
                                    c.instructor = t;
                                    d.instructor = t;
                                    t_sched1.addExistingActivity(a);
                                    t_sched2.addExistingActivity(b);
                                    t_sched3.addExistingActivity(c);
                                    t_sched4.addExistingActivity(d);
                                }
                            }
                            break;
                        case 5:
                            if(sched1.day_of_week != 1) break;
                            sched2 = r.scheds.get(1);
                            sched3 = r.scheds.get(2);
                            sched4 = r.scheds.get(3);
                            DaySched sched5 = r.scheds.get(4);
                            t_sched2 = t.scheds.get(1);
                            t_sched3 = t.scheds.get(2);
                            t_sched4 = t.scheds.get(3);
                            DaySched t_sched5 = t.scheds.get(4);
                            for(Activity a : sched1.activities) {
                                Activity b = null;
                                Activity c = null;
                                Activity d = null;
                                Activity e = null;
                                for(Activity act : sched2.activities) {
                                    if(act.instance == a.instance && act.course == a.course) {
                                        b = act;
                                        break;
                                    }
                                }
                                for(Activity act : sched3.activities) {
                                    if(act.instance == a.instance && act.course == a.course) {
                                        c = act;
                                        break;
                                    }
                                }
                                for(Activity act : sched4.activities) {
                                    if(act.instance == a.instance && act.course == a.course) {
                                        d = act;
                                        break;
                                    }
                                }
                                for(Activity act : sched5.activities) {
                                    if(act.instance == a.instance && act.course == a.course) {
                                        e = act;
                                        break;
                                    }
                                }
                                if(
                                    b != null &&
                                    c != null &&
                                    d != null &&
                                    e != null &&
                                    a.instructor == null &&
                                    b.instructor == null &&
                                    c.instructor == null &&
                                    d.instructor == null &&
                                    e.instructor == null &&
                                    t_sched1.checkVacant(a.start_time, a.duration) &&
                                    t_sched2.checkVacant(b.start_time, b.duration) &&
                                    t_sched3.checkVacant(c.start_time, c.duration) &&
                                    t_sched4.checkVacant(d.start_time, d.duration) &&
                                    t_sched5.checkVacant(e.start_time, e.duration) &&
                                    a.course == current_course &&
                                    b.course == current_course &&
                                    c.course == current_course &&
                                    d.course == current_course &&
                                    e.course == current_course &&
                                    t.addMinutes(current_course.minutes)
                                ) {
                                    a.instructor = t;
                                    b.instructor = t;
                                    c.instructor = t;
                                    d.instructor = t;
                                    e.instructor = t;
                                    t_sched1.addExistingActivity(a);
                                    t_sched2.addExistingActivity(b);
                                    t_sched3.addExistingActivity(c);
                                    t_sched4.addExistingActivity(d);
                                    t_sched5.addExistingActivity(e);
                                }
                            }
                            break;
                        case 6:
                            if(sched1.day_of_week != 1) break;
                            sched2 = r.scheds.get(1);
                            sched3 = r.scheds.get(2);
                            sched4 = r.scheds.get(3);
                            sched5 = r.scheds.get(4);
                            DaySched sched6 = r.scheds.get(5);
                            t_sched2 = t.scheds.get(1);
                            t_sched3 = t.scheds.get(2);
                            t_sched4 = t.scheds.get(3);
                            t_sched5 = t.scheds.get(4);
                            DaySched t_sched6 = t.scheds.get(5);
                            for(Activity a : sched1.activities) {
                                Activity b = null;
                                Activity c = null;
                                Activity d = null;
                                Activity e = null;
                                Activity f = null;
                                for(Activity act : sched2.activities) {
                                    if(act.instance == a.instance && act.course == a.course) {
                                        b = act;
                                        break;
                                    }
                                }
                                for(Activity act : sched3.activities) {
                                    if(act.instance == a.instance && act.course == a.course) {
                                        c = act;
                                        break;
                                    }
                                }
                                for(Activity act : sched4.activities) {
                                    if(act.instance == a.instance && act.course == a.course) {
                                        d = act;
                                        break;
                                    }
                                }
                                for(Activity act : sched5.activities) {
                                    if(act.instance == a.instance && act.course == a.course) {
                                        e = act;
                                        break;
                                    }
                                }
                                for(Activity act : sched6.activities) {
                                    if(act.instance == a.instance && act.course == a.course) {
                                        f = act;
                                        break;
                                    }
                                }
                                if(
                                    b != null &&
                                    c != null &&
                                    d != null &&
                                    e != null &&
                                    f != null &&
                                    a.instructor == null &&
                                    b.instructor == null &&
                                    c.instructor == null &&
                                    d.instructor == null &&
                                    e.instructor == null &&
                                    f.instructor == null &&
                                    t_sched1.checkVacant(a.start_time, a.duration) &&
                                    t_sched2.checkVacant(b.start_time, b.duration) &&
                                    t_sched3.checkVacant(c.start_time, c.duration) &&
                                    t_sched4.checkVacant(d.start_time, d.duration) &&
                                    t_sched5.checkVacant(e.start_time, e.duration) &&
                                    t_sched6.checkVacant(f.start_time, f.duration) &&
                                    a.course == current_course &&
                                    b.course == current_course &&
                                    c.course == current_course &&
                                    d.course == current_course &&
                                    e.course == current_course &&
                                    f.course == current_course &&
                                    t.addMinutes(current_course.minutes)
                                ) {
                                    a.instructor = t;
                                    b.instructor = t;
                                    c.instructor = t;
                                    d.instructor = t;
                                    e.instructor = t;
                                    f.instructor = t;
                                    t_sched1.addExistingActivity(a);
                                    t_sched2.addExistingActivity(b);
                                    t_sched3.addExistingActivity(c);
                                    t_sched4.addExistingActivity(d);
                                    t_sched5.addExistingActivity(e);
                                    t_sched5.addExistingActivity(f);
                                }
                            }
                            break;
                        default:
                            System.out.println("Invalid weekly_meeting value!");
                            break;
                    }
                }
            }
        }
    }

    public void printTimetable() {
        for(Room r : rooms) {
            System.out.println("ROOM " + r.id + " {");
            for(DaySched sched : r.scheds) {
                System.out.println("\tDAY " + sched.day_of_week + " [");
                for(Activity a : sched.activities) {
                    if(a.instructor == null) {
                        System.out.println("\t\t" + a.course.name + "-" + a.instance + " (" + a.start_time + "-" + a.getEndTime() + ")");
                    } else {
                        System.out.println("\t\t" + a.course.name + "-" + a.instance + " (" + a.start_time + "-" + a.getEndTime() + ") [" + a.instructor.name + "]");
                    }
                }
                System.out.println("\t]");
            }
            System.out.println("}");
        }
    }

//    public String repeat(String str, int repetition){
//        if(repetition < 1) return null;
//        StringBuilder sb = new StringBuilder();
//        for(int i = 0; i < repetition; i++) sb.append(str);
//        return sb.toString();
//    }
//
//    public String leftPad(String input, char ch, int L){
//        return String.format("%" + (-L) + "s", input).replace(' ', ch);
//    }
//
//    public void printVisualize(){
//        int COLWIDTH = 15;
//
//        for(Room r : rooms){
//            System.out.println("Room " + r.id);
//
//            StringBuilder sb = new StringBuilder();
//
//            // Header
//            sb.append(repeat("-", COLWIDTH * (r.scheds.size()) + 1) + '\n');
//
//            for(DaySched d : r.scheds){
//                sb.append(leftPad("| Day " + d.day_of_week, ' ', COLWIDTH));
//            }
//            sb.append("|\n");
//            sb.append(repeat("-", COLWIDTH * (r.scheds.size()) + 1) + '\n');
//
//            // Print Activities
//
//
//            System.out.println(sb);
//        }
//
//    }
}
