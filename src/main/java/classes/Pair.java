package classes;

public class Pair {
    private int courseId;
    private int credits;

    public Pair(int courseId, int credits) {
        this.courseId = courseId;
        this.credits = credits;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "courseId=" + courseId +
                ", credits=" + credits +
                '}';
    }
}