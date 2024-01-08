import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Timetable {
    public String name = "Unnamed Timetable";
    List<Room> rooms;
    List<Course> courses;
    List<Instructor> instructors;
    List<Section> sections;

    static int interval = 30; // interval in minutes (15, 30, 60)
    static int period_start = 420; // (7:00AM) - 7:30AM
    static int period_end = 1140; // 6:30PM - (7:00PM)3
    static List<Integer> excluded_periods = Arrays.stream(new int[] {720, 750}).boxed().toList(); // 12:00PM, 12:30PM
    static final int[] one_per_week_priority = {3, 6, 1, 5, 2, 4}; // WED (MON to FRI except WED for 4 per week priority)
    static final int[][] two_per_week_priority = {{2, 4}, {1, 5}, {3, 6}}; // T-Th (high priority), M-F, W-S
    static final int[][] three_per_week_priority = {{1, 3, 5}, {2, 4, 6}}; // 5 per week priority would be M-W-F then T-Th-S
    static boolean include_saturday = true; // If false, W-S and T-Th-S are nada
    static int number_of_room_types = 4; // 0 - Normal, 1 - Large, 2 - ScienceLab, 3 - ComLab
    static int max_instructor_minutes_per_day = 480; // 8 hours (480) or 10 hours (600) or MAX
    static int max_student_minutes_per_day = Integer.MAX_VALUE; // 8 hours (480) or 10 hours (600) or MAX
    static int max_room_minutes_per_day = Integer.MAX_VALUE; // 8 hours (480) or 10 hours (600) or MAX
    static int max_consecutive_minutes = 480; // (for instructors) 4 hours or MAX // TODO: Not yet implemented

    public Timetable(List<Room> rooms, List<Course> courses, List<Instructor> instructors, List<Section> sections) {
        this.rooms = rooms;
        this.courses = courses;
        this.instructors = instructors;
        this.sections = sections;

        for(int h = 0; h < 10; h++) {
            // Distribute Courses to Rooms
            List<Course> temp0 = new ArrayList<>(courses);
            for(int i = 1; i <= Timetable.number_of_room_types || !temp0.isEmpty(); i++) { // Prioritizes courses with fewer compatible rooms
                for(int j = 0; j < temp0.size(); j++) {
                    Course c = temp0.get(j);
                    if(c.number_of_compatible_rooms == i) {
                        putCourseToRooms(c);
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
                        putInstructorToRooms(ins);
                        temp1.remove(ins);
                        j--;
                    }
                }
            }
            // Distribute Section to Classes
            for(Section s : sections) {
                putSectionToRooms(s);
            }
            // Checks if all sections have complete courses
            if(checkSectionCompletion()) {
                // Remove all activities with no sections
                removeNoSection();
                System.out.println("Completed after " + h + " increment(s).");
                break;
            }
            // Resets all Rooms, Courses, Instructors, and Sections
            for(Room r : rooms) {
                r.scheds.clear();
                for(int i = 1; i <= 5; i++) {
                    r.scheds.add(new DaySched(i, r));
                }
                if(Timetable.include_saturday) {
                    r.scheds.add(new DaySched(6, r));
                }
            }
            for(Course c : courses) {
                c.course_classes.clear();
                c.classes_offered++; // Increment the number of course offering per course;
            }
            for(Instructor ins : instructors) {
                ins.scheds.clear();
                ins.total_minutes = 0;
                for(int i = 1; i <= 5; i++) {
                    ins.scheds.add(new DaySched(i, period_start, period_end));
                }
                if(Timetable.include_saturday) {
                    ins.scheds.add(new DaySched(6, period_start, period_end));
                }
            }
            for(Section s : sections) {
                s.scheds.clear();
                for(int i = 1; i <= 5; i++) {
                    s.scheds.add(new DaySched(i, period_start, period_end));
                }
                if(Timetable.include_saturday) {
                    s.scheds.add(new DaySched(6, period_start, period_end));
                }
            }
        }
    }

    private void putCourseToRooms(Course c) {
        putCourseToRooms(c, null);
    }

    private void putCourseToRooms(Course c, Course lab) {
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
                                DaySched sched = r.scheds.get(Timetable.one_per_week_priority[j] - 1); // -1 because Sunday was not included in the scheds array of Room
                                while(classes_offered != 0) { // Add classes of the same course to the rooms until all classes have been added to the schedule
                                    boolean success = sched.checkViolation(r, c.minutes) && sched.addActivity(c, c.minutes, instance); // Checks if it violates the maximum minutes per day before adding activity
//                                    lab.
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
                                    boolean success1 = sched1.checkConflict(duration) && sched1.checkViolation(r, duration); // Check conflicts before adding to make sure that all meetings in a particular class of the course are added
                                    boolean success2 = sched2.checkConflict(duration) && sched2.checkViolation(r, duration); // Also checks if it violates the maximum minutes per day
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
                                    continue;
                                }
                                DaySched sched1 = r.scheds.get(Timetable.three_per_week_priority[j][0] - 1);
                                DaySched sched2 = r.scheds.get(Timetable.three_per_week_priority[j][1] - 1);
                                DaySched sched3 = r.scheds.get(Timetable.three_per_week_priority[j][2] - 1);
                                while(classes_offered != 0) {
                                    boolean success1 = sched1.checkConflict(duration) && sched1.checkViolation(r, duration);
                                    boolean success2 = sched2.checkConflict(duration) && sched2.checkViolation(r, duration);
                                    boolean success3 = sched3.checkConflict(duration) && sched3.checkViolation(r, duration);
                                    if(success1 & success2 & success3) {
                                        sched1.addActivity(c, duration, instance);
                                        sched2.addActivity(c, duration, instance);
                                        sched3.addActivity(c, duration, instance);
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
                                    boolean success1 = sched[0].checkConflict(duration) && sched[0].checkViolation(r, duration);
                                    boolean success2 = sched[1].checkConflict(duration) && sched[1].checkViolation(r, duration);
                                    boolean success3 = sched[2].checkConflict(duration) && sched[2].checkViolation(r, duration);
                                    boolean success4 = sched[3].checkConflict(duration) && sched[3].checkViolation(r, duration);
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
                                boolean success1 = sched1.checkConflict(duration) && sched1.checkViolation(r, duration);
                                boolean success2 = sched2.checkConflict(duration) && sched2.checkViolation(r, duration);
                                boolean success3 = sched3.checkConflict(duration) && sched3.checkViolation(r, duration);
                                boolean success4 = sched4.checkConflict(duration) && sched4.checkViolation(r, duration);
                                boolean success5 = sched5.checkConflict(duration) && sched5.checkViolation(r, duration);
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
                                boolean success1 = sched1.checkConflict(duration) && sched1.checkViolation(r, duration);
                                boolean success2 = sched2.checkConflict(duration) && sched2.checkViolation(r, duration);
                                boolean success3 = sched3.checkConflict(duration) && sched3.checkViolation(r, duration);
                                boolean success4 = sched4.checkConflict(duration) && sched4.checkViolation(r, duration);
                                boolean success5 = sched5.checkConflict(duration) && sched5.checkViolation(r, duration);
                                boolean success6 = sched6.checkConflict(duration) && sched6.checkViolation(r, duration);
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
                System.err.println("Error: Invalid weekly_meeting value for course distribution!");
                break;
        }
//        if(c.lecture_component != null && c.weekly_meetings <= 3) {
//            putCourseToRooms(c.lecture_component, c);
//        }
    }

    private void putInstructorToRooms(Instructor t) {
        for(int i = 0; i < t.compatible_courses.size(); i++) { // Traverses all instructor priority/compatible courses
            Course current_course = courses.get(t.compatible_courses.get(i));
            for(Room r : rooms) {
                for(DaySched sched1 : r.scheds) { // Traverses all daily schedules of the room from Monday to Saturday
                    DaySched t_sched1 = t.scheds.get(sched1.day_of_week - 1); // Gets the teacher schedule for the currently traversed day
                    switch (current_course.weekly_meetings) {
                        case 1:
                            for(Activity a : sched1.activities) {
                                if(
                                    a.instructor == null && // Only assigns instructor is current activity instructor is unassigned
                                    t_sched1.checkVacant(a.start_time, a.duration) &&
                                    a.course == current_course &&
                                    t.addMinutes(current_course.minutes) &&
                                    t_sched1.checkViolation(t, a.duration)
                                ) {
                                    a.instructor = t; // Sets activity instructor to current teacher
                                    t_sched1.addExistingActivity(a); // Adds activity to teacher schedule
                                }
                            }
                            break;
                        case 2: // If the course meets two times a week
                            int pair_day = -1;
                            for (int[] ints : two_per_week_priority) { // Searches for the pair schedule
                                if (ints[0] == sched1.day_of_week) {
                                    pair_day = ints[1];
                                    break;
                                }
                                if (ints[1] == sched1.day_of_week) {
                                    pair_day = ints[0];
                                    break;
                                }
                            }
                            DaySched sched2 = r.scheds.get(pair_day - 1); // Gets the room schedule for the pair day
                            DaySched t_sched2 = t.scheds.get(pair_day - 1); // Get the teacher schedule for the pair day
                            for(Activity a : sched1.activities) {
                                Activity b = null;
                                for(Activity act : sched2.activities) {
                                    if(act.instance == a.instance && act.course == a.course) { // Checks if the activity is indeed the "pair" of the current activity
                                        b = act;
                                        break;
                                    }
                                }
                                if(
                                    b != null &&
                                    a.instructor == null && // Only adds to teacher when both classes are vacant and unassigned
                                    b.instructor == null &&
                                    t_sched1.checkVacant(a.start_time, a.duration) &&
                                    t_sched2.checkVacant(b.start_time, b.duration) &&
                                    a.course == current_course &&
                                    b.course == current_course &&
                                    t.addMinutes(current_course.minutes) &&
                                    t_sched1.checkViolation(t, a.duration) &&
                                    t_sched2.checkViolation(t, b.duration)
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
                                    t.addMinutes(current_course.minutes) &&
                                    t_sched1.checkViolation(t, a.duration) &&
                                    t_sched2.checkViolation(t, b.duration) &&
                                    t_sched3.checkViolation(t, c.duration)
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
                        case 4:
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
                                        t.addMinutes(current_course.minutes) &&
                                        t_sched1.checkViolation(t, a.duration) &&
                                        t_sched2.checkViolation(t, b.duration) &&
                                        t_sched3.checkViolation(t, c.duration) &&
                                        t_sched4.checkViolation(t, d.duration)
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
                                    t.addMinutes(current_course.minutes) &&
                                    t_sched1.checkViolation(t, a.duration) &&
                                    t_sched2.checkViolation(t, b.duration) &&
                                    t_sched3.checkViolation(t, c.duration) &&
                                    t_sched4.checkViolation(t, d.duration) &&
                                    t_sched5.checkViolation(t, e.duration)
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
                                    t.addMinutes(current_course.minutes) &&
                                    t_sched1.checkViolation(t, a.duration) &&
                                    t_sched2.checkViolation(t, b.duration) &&
                                    t_sched3.checkViolation(t, c.duration) &&
                                    t_sched4.checkViolation(t, d.duration) &&
                                    t_sched5.checkViolation(t, e.duration) &&
                                    t_sched6.checkViolation(t, f.duration)
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
                            System.err.println("Error: Invalid weekly_meeting value for instructor distribution!");
                            break;
                    }
                }
            }
        }
    }

    private void putSectionToRooms(Section s) {
        for(Integer c : s.section_courses) { // Traverses all courses required for the section
            Course curr_c = courses.get(c);
            int weekly_meetings = curr_c.weekly_meetings;
            for(Activity a : curr_c.course_classes) { // Sets section of the first available class/activity instance
                DaySched curr_sched = s.scheds.get(a.sched.day_of_week - 1);
                if (a.section == null && weekly_meetings > 0 && curr_sched.checkVacant(a.start_time, a.duration) && curr_sched.checkViolation(s, a.duration)) {
                    a.section = s;
                    s.scheds.get(a.sched.day_of_week - 1).addExistingActivity(a);
                    weekly_meetings--;
                } else if(weekly_meetings <= 0) {
                    break;
                }
            }
        }
    }

    private boolean checkSectionCompletion() {
        for(Section s : sections) {
            for(int c_id : s.section_courses) {
                Course c = courses.get(c_id);
                boolean hasSection = false;
                for(Activity a : c.course_classes) {
                    if(a.section == s) {
                        hasSection = true;
                        break;
                    }
                }
                if(!hasSection) {
                    return false;
                }
            }
        }
        return true;
    }

    private void removeNoSection() { // removes all courses with no sections
        for(Course c : courses) {
            for(int i = c.course_classes.size() - 1; i >= 0; i--) { // Traverse all activities in course to check if they have sections as it would be easier
                Activity a = c.course_classes.get(i);
                if(a.section == null) { // Remove all references to this section
                    if(a.room != null) { // this doesn't actually happen pero nanigurado lang XD
                        for(DaySched sched : a.room.scheds) {
                            sched.activities.remove(a);
                        }
                    }
                    if(a.instructor != null) {
                        for(DaySched sched : a.instructor.scheds) {
                            sched.activities.remove(a);
                        }
                    }
                    if(a.section != null) {
                        for(DaySched sched : a.section.scheds) {
                            sched.activities.remove(a);
                        }
                    }
                    c.course_classes.remove(a);
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
}
