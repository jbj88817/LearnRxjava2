package us.bojie.learnrxjava2;

/**
 * Created by bojiejiang on 4/18/17.
 */

public class Course {

    private int id;
    private String name;

    public Course() {
    }

    public Course(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
