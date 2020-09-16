package scheduler.model;

import org.quartz.*;

import java.io.IOException;
import java.util.Arrays;

public class MyScheduler {
    private final Long id;
    private final String pathToFile;
    private final String launchTime;
    private final String launchDay;
    private JobDetail jobDetail;
    private CronTrigger cronTrigger;


    public MyScheduler(Scripts scripts) {
        this.id = scripts.getId();
        this.pathToFile = scripts.getPathToFile();
        this.launchTime = scripts.getLaunchTime();
        this.launchDay = scripts.getLaunchDay();
        this.initMethod();
    }

    private void initMethod(){
        try {
            this.triggerDefenition();
        } catch (IOException e) {
            System.out.println("Error while definition cron expression!");
        }
        this.jobDefinition();
    }

    private void jobDefinition(){
        jobDetail = JobBuilder.newJob(ScriptImpJob.class)
                .withIdentity("quartzJobName" + id, "quartzGroupName")
                .usingJobData("id", pathToFile)
                .build();
    }

    private void triggerDefenition() throws IOException {
        cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity("myCronTriggerName" + id, "myCronTriggerGroup")
                .withSchedule(CronScheduleBuilder.cronSchedule(convertToCronFormat()))
                .forJob("quartzJobName" + id,"quartzGroupName")
                .build();
    }

    public String convertToCronFormat() throws IOException {
        StringBuilder timeInCronFormat = new StringBuilder("");
        // Adding time
        String[] arrayTime = launchTime.split(":");
        if(arrayTime.length != 2)
            throw new IOException("Invalid time!");
        // Adding seconds
        timeInCronFormat.append("0");
        //Adding minutes
        if(arrayTime[1].equals("00"))
            timeInCronFormat.append(" 0");
        else
            timeInCronFormat.append(" " + arrayTime[1]);
        //Adding hours
        if(arrayTime[0].equals("10") || arrayTime[0].equals("20"))
            timeInCronFormat.append(" " + arrayTime[0]);
        else
            timeInCronFormat.append(" " + arrayTime[0].replace("0", ""));
        // Adding day
        String[] arrayString = launchDay.split(",");
        arrayString =  Arrays.stream(arrayString).map((a) -> a = a.trim()).toArray(String[]::new);
        if(arrayString.length == 0)
            throw new IOException("Invalid data!");
        // Everyday
        if(arrayString.length == 1 && arrayString[0].equals("?")){
            return timeInCronFormat.append(" ? * * *").toString();
        }
        // One exact day
        else if(arrayString.length == 1){
            return timeInCronFormat.append(" ? * " + parseDayOfWeek(arrayString[0]) + " *").toString();
        }
        // Several days
        if(arrayString.length > 1){
            timeInCronFormat.append(" ? * ");
            String TMP = "";
            for(int i = 0; i < arrayString.length; i++){
                if(i == 0)
                    timeInCronFormat.append(parseDayOfWeek(arrayString[i]));
                else
                    timeInCronFormat.append("," + parseDayOfWeek(arrayString[i]));
            }
            timeInCronFormat.append(" *");
        }
        return timeInCronFormat.toString();
    }

    public String parseDayOfWeek(String dayOfWeek){
        switch (dayOfWeek){
            case "SUNDAY":
                return "SUN";
            case "MONDAY":
                return "MON";
            case "TUESDAY":
                return "TUE";
            case "WEDNESDAY":
                return "WED";
            case "THURSDAY":
                return "THU";
            case "FRIDAY":
                return "FRI";
            case "SATURDAY":
                return "SAT";
            default:
                return "Day of week don't find!";
        }
    }

    public JobDetail getJobDetail() {
        return jobDetail;
    }

    public CronTrigger getCronTrigger() {
        return cronTrigger;
    }

}
