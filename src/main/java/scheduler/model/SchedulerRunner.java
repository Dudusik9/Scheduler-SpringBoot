package scheduler.model;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import java.util.HashSet;
import java.util.Set;

public class SchedulerRunner {

    private Set<Scripts> scripts = new HashSet<>();

    public SchedulerRunner(Set<Scripts> scripts) {
        this.scripts = scripts;
    }

    public void run() {
        try {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();
            for(Scripts element : scripts){
                MyScheduler currentScheduler = new MyScheduler(element);
                scheduler.scheduleJob(currentScheduler.getJobDetail(), currentScheduler.getCronTrigger());
            }
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
