import java.util.Arrays;
import java.util.List;

public class Timetable {
    public String name = "Sample Timetable";
    List<Room> rooms;
    List<Course> courses;

    static int interval = 60; // interval in minutes (15, 30, 60)
    static int period_start = 420; // 7:00AM - 7:30AM
    static int period_end = 1110; // 6:30PM - 7:00PM
    static List<Integer> excluded_periods = Arrays.stream(new int[] {720, 750}).boxed().toList(); // 12:00PM, 12:30PM
    static final int[] one_per_week_priority = {3, 6, 1, 5, 2, 4}; // WED (MON to FRI except WED for 4 per week priority)
    static final int[][] two_per_week_priority = {{2, 4}, {1, 5}, {3, 6}}; // T-Th (high priority), M-F, W-S
    static final int[][] three_per_week_priority = {{1, 3, 5}, {2, 4, 6}}; // 5 per week priority would be M-W-F then T-Th-S
    static boolean include_saturday = false; // If false, W-S and T-Th-S are nada
    static int number_of_room_types = 4; // 0 - Normal, 1 - Large, 2 - ScienceLab, 3 - ComLab

    public Timetable(List<Room> rooms, List<Course> courses) {
        this.rooms = rooms;
        this.courses = courses;
        // Distribute Courses to Rooms
        List<Course> temp = courses;
        for(int i = 1; i <= Timetable.number_of_room_types || !temp.isEmpty(); i++) { // Prioritizes rooms with fewer compatible rooms
            for(int j = 0; j < temp.size(); j++) {
                Course c = temp.get(j);
                if(c.number_of_compatible_rooms == i) {
                    putCourseToRooms(c, rooms);
                    temp.remove(c);
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
                                if(!include_saturday && two_per_week_priority[j][0] == 6 || two_per_week_priority[j][1] == 6) {
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
                                if(!include_saturday && three_per_week_priority[j][0] == 6 || three_per_week_priority[j][1] == 6 || three_per_week_priority[j][2] == 6) {
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
        }
    }

    public void printTimetable() {
        for(Room r : rooms) {
            System.out.println("ROOM " + r.id + " {");
            for(DaySched sched : r.scheds) {
                System.out.println("\tDAY " + sched.day_of_week + " [");
                for(Activity a : sched.activities) {
                    System.out.println("\t\t" + a.course.name + "-" + a.instance + " (" + a.start_time + "-" + a.getEndTime() + ")");
                }
                System.out.println("\t]");
            }
            System.out.println("}");
        }
    }
}
