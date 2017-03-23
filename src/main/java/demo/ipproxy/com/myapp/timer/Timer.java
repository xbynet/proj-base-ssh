package demo.ipproxy.com.myapp.timer;

import org.quartz.JobExecutionException;

import demo.ipproxy.com.myapp.client.Client;
import demo.ipproxy.com.myapp.main.main;
import demo.ipproxy.com.myapp.redis.LoadMemory;


public class Timer {
    final public static  String job_name_1 = "task_Client";
    final public static  String job_name_2 = "task2_Main";
    final public static  String job_name_3 = "task3_Redis";

    public Timer() {
    }

    public static void main(String[] args) throws InterruptedException, JobExecutionException {
//        QuartzManager.addJob(job_name_1, Client.class, "0 0/1 * * * ?");
    	new Client().execute(null);
        Thread.sleep(1000 * 10);
//        QuartzManager.addJob(job_name_2, main.class, "0 0/2 * * * ?");
        new main().execute(null);
        QuartzManager.addJob(job_name_3, LoadMemory.class, "0 0 2 * * ?");
    	




    }
}

