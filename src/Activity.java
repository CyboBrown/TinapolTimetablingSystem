public class Activity {
    public int start_time;
    public int duration;
    public Course course;
    public Room room;
    public DaySched sched;
    public Instructor instructor;
    public Section section;
    public int instance;

    public Activity(int start_time, int duration, Course course, DaySched sched) {
        this.start_time = start_time;
        this.duration = duration;
        this.course = course;
        this.room = null;
        this.sched = sched;
        this.instructor = null;
        this.section = null;
        this.instance = 0;
    }

    public Activity(int start_time, int duration, Course course, Room room, DaySched sched, int instance) {
        this.start_time = start_time;
        this.duration = duration;
        this.course = course;
        this.room = room;
        this.sched = sched;
        this.instructor = null;
        this.section = null;
        this.instance = instance;
    }

    public int getEndTime() {
        return start_time + duration;
    }
}
