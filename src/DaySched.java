import java.util.ArrayList;
import java.util.List;

public class DaySched {
    public int day_of_week; // 0 (SUN) to 6 (SAT)
    public List<Activity> activities;
    private int current_vacant; // This serves as the TOP of the STACK of classes within the schedule

    public DaySched(int day_of_week) {
        this.day_of_week = day_of_week;
        activities = new ArrayList<>();
        current_vacant = Timetable.period_start;
    }

    public boolean checkConflict(int duration) { // Checks if an activity of a particular duration can be added
        int temp_current_vacant = current_vacant;
        for(int i = temp_current_vacant; i < temp_current_vacant + duration; i += Timetable.interval) {
            if(Timetable.excluded_periods.contains(i)) {
                temp_current_vacant = i + Timetable.interval;
                i = temp_current_vacant;
                continue;
            }
            if(i > Timetable.period_end) {
                return false;
            }
        }
        return true;
    }

    public boolean addActivity(Course course, int duration, int instance) { // Returns true if activity was successfully added
        for(int i = current_vacant; i < current_vacant + duration; i += Timetable.interval) {
            if(Timetable.excluded_periods.contains(i)) { // Jumps to next interval if period is excluded
                current_vacant = i + Timetable.interval;
                i = current_vacant;
                continue;
            }
            if(i > Timetable.period_end) {
                return false;
            }
        }
        activities.add(new Activity(current_vacant, duration, course, instance));
        current_vacant = current_vacant + duration;
        return true;
    }
}
