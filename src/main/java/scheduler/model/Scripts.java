package scheduler.model;

import javax.persistence.*;

@Entity
public class Scripts {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "PATH", nullable = false)
    private String pathToFile;

    @Column(name = "TIME", nullable = false)
    private String launchTime;

    @Column(name = "DAY", nullable = false)
    private String launchDay;

    public Scripts() {
    }

    public Scripts( String pathToFile, String launchTime, String launchDay) {
        this.pathToFile = pathToFile;
        this.launchTime = launchTime;
        this.launchDay = launchDay;
    }

    public Long getId() {
        return id;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public String getLaunchTime() {
        return launchTime;
    }

    public String getLaunchDay() {
        return launchDay;
    }
}

