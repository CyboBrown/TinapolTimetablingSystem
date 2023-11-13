public class Activity {
    public int start_time;
    public int duration;
    public Course course;
    public Instructor instructor;
    public Room room;
    public int instance;

    public Activity(int start_time, int duration, Course course) {
        this.start_time = start_time;
        this.duration = duration;
        this.course = course;
        this.room = null;
        this.instance = 0;
    }

    public Activity(int start_time, int duration, Course course, Room room, int instance) {
        this.start_time = start_time;
        this.duration = duration;
        this.course = course;
        this.room = room;
        this.instance = instance;
    }

    public int getEndTime() {
        return start_time + duration;
    }
}
