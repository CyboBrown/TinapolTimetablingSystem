public class Activity {
    public int start_time;
    public int duration;
    public Course course;
    public Instructor instructor;
    public int instance;

    public Activity(int start_time, int duration, Course course) {
        this.start_time = start_time;
        this.duration = duration;
        this.course = course;
    }

    public Activity(int start_time, int duration, Course course, int instance) {
        this.start_time = start_time;
        this.duration = duration;
        this.course = course;
        this.instance = instance;
    }

    public int getEndTime() {
        return start_time + duration;
    }
}
