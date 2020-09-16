package scheduler.model;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.io.IOException;


public class ScriptImpJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            String tmp = jobExecutionContext.getJobDetail().getJobDataMap().getString("id");
            String resultCommand = String.format("start %s", tmp);
            Process process = new ProcessBuilder("cmd.exe","/c", resultCommand).start();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Script don't find!");
        }
    }
}
