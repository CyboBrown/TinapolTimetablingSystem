import java.util.ArrayList;
import java.util.List;

public class DaySched {
    public int day_of_week; // 0 (SUN) to 6 (SAT)
    public int period_start;
    public int period_end;
    public Room room;
    public List<Activity> activities;
    public int number_of_periods;
    public boolean[] is_vacant;
    private int current_vacant; // This serves as the TOP of the STACK of classes within the schedule

    public DaySched(int day_of_week, Room room) {
        this.day_of_week = day_of_week;
        this.period_start = Timetable.period_start;
        this.period_end = Timetable.period_end;
        this.room = room;
        activities = new ArrayList<>();
        current_vacant = Timetable.period_start;
        number_of_periods = (period_end - period_start) / Timetable.interval;
        is_vacant = new boolean[number_of_periods];
        for(int i = 0; i < number_of_periods; i++) {
            is_vacant[i] = !Timetable.excluded_periods.contains((Timetable.interval * i) + period_start);
        }
    }

    public DaySched(int day_of_week, int period_start, int period_end) {
        this.day_of_week = day_of_week;
        this.period_start = period_start;
        this.period_end = period_end;
        this.room = null;
        activities = new ArrayList<>();
        current_vacant = period_start;
        number_of_periods = (period_end - period_start) / Timetable.interval;
        is_vacant = new boolean[number_of_periods];
        for(int i = 0; i < number_of_periods; i++) {
            is_vacant[i] = !Timetable.excluded_periods.contains((Timetable.interval * i) + period_start);
        }
    }

    public boolean checkConflict(int duration) { // Checks if an activity of a particular duration can be added
        int index_current_vacant = (current_vacant - period_start) / Timetable.interval;
        for(int i = 0; i < duration / Timetable.interval; i++) {
            if(index_current_vacant + i >= number_of_periods) {
                return false;
            }
            if(!is_vacant[index_current_vacant + i]) {
                index_current_vacant++;
                i = -1;
            }
        }
        return true;
    }

    public boolean addActivity(Course course, int duration, int instance) { // Returns true if activity was successfully added
        int index_current_vacant = (current_vacant - period_start) / Timetable.interval;
        for(int i = 0; i < duration / Timetable.interval; i++) {
            if(index_current_vacant + i >= number_of_periods) {
                return false;
            }
//            System.out.println(" - " + current_vacant + "~" + index_current_vacant + i); // TODO: Improve Code
            if(!is_vacant[index_current_vacant + i]) {
                current_vacant += Timetable.interval;
                index_current_vacant++;
                i = -1;
            }
        }
        activities.add(new Activity(current_vacant, duration, course, room, instance));
        int current_vacant_index = (current_vacant - period_start) / Timetable.interval;
        for(int i = 0; i < duration / Timetable.interval; i++) {
            is_vacant[current_vacant_index + i] = false;
        }
        current_vacant = current_vacant + duration;
        return true;
    }

    public void addExistingActivity(Activity a) {
        activities.add(a);
        int current_index = (a.start_time - period_start) / Timetable.interval;
        for(int i = 0; i < a.duration / Timetable.interval; i++) {
            is_vacant[current_index + i] = false;
        }
    }

    public boolean checkVacant(int start_time, int duration) { // Checks if an activity of a particular duration can be added
        int current_index = (start_time - period_start) / Timetable.interval;
        for(int i = 0; i < duration / Timetable.interval; i++) {
            if(current_index + i >= number_of_periods || !is_vacant[current_index + i]) {
                return false;
            }
        }
        return true;
    }

    public void printVacancy() { // For testing purposes, prints a list of periods showing their availability
        for(int i = 0; i < number_of_periods; i++) {
            System.out.println((Timetable.interval * i) + period_start + " - " + is_vacant[i]);
        }
    }
}
