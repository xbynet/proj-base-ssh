package com.myapp.timer; /**
 * Created by gaorui on 17/1/9.
 */
/**
 * @Description:
 *
 * @Title: QuartzManager.java
 * @Package com.joyce.quartz
 * @Copyright: Copyright (c) 2014
 *
 * @author Comsys-LZP
 * @date 2014-6-26 下午03:15:52
 * @version V2.0
 */

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @Description: 定时任务管理类
 *
 * @ClassName: QuartzManager
 * @Copyright: Copyright (c) 2014
 *
 * @author Comsys-LZP
 * @date 2014-6-26 下午03:15:52
 * @version V2.0
 */
public class QuartzManager {
    private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
    private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";
    private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";

    /**
     * @Description: 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
     *
     * @param jobName
     *            任务名
     * @param cls
     *            任务
     * @param time
     *            时间设置，参考quartz说明文档
     *
     * @Title: QuartzManager.java
     * @Copyright: Copyright (c) 2014
     *
     * @author Comsys-LZP
     * @date 2014-6-26 下午03:47:44
     * @version V2.0
     */
    @SuppressWarnings("unchecked")
    public static void addJob(String jobName, Class cls, String time) {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(jobName, JOB_GROUP_NAME).build();
            // 触发器
            CronTrigger trigger =TriggerBuilder.newTrigger()
            		.withIdentity(jobName, TRIGGER_GROUP_NAME).withSchedule(CronScheduleBuilder.cronSchedule(time)).startNow().build(); 
            sched.scheduleJob(jobDetail, trigger);
            // 启动
            if (!sched.isShutdown()) {
                sched.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * @Description: 修改一个任务的触发时间
     *
     * @param triggerName
     * @param triggerGroupName
     * @param time
     *
     * @Title: QuartzManager.java
     * @Copyright: Copyright (c) 2014
     *
     * @author Comsys-LZP
     * @date 2014-6-26 下午03:49:37
     * @version V2.0
     */
    public static void modifyJobTime(String triggerName,
                                     String triggerGroupName, String time) {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            TriggerKey triggerKey=new TriggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {
            	
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * @Description: 移除一个任务
     *
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     *
     * @Title: QuartzManager.java
     * @Copyright: Copyright (c) 2014
     *
     * @author Comsys-LZP
     * @date 2014-6-26 下午03:50:01
     * @version V2.0
     */
    public static void removeJob(String jobName ) {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            TriggerKey triggerkey=new TriggerKey(jobName, TRIGGER_GROUP_NAME);
            sched.pauseTrigger(triggerkey);// 停止触发器
            sched.unscheduleJob(triggerkey);// 移除触发器
            JobKey jobKey=new JobKey(jobName,JOB_GROUP_NAME);
            sched.deleteJob(jobKey);// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description:启动所有定时任务
     *
     *
     * @Title: QuartzManager.java
     * @Copyright: Copyright (c) 2014
     *
     * @author Comsys-LZP
     * @date 2014-6-26 下午03:50:18
     * @version V2.0
     */
    public static void startJobs() {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            sched.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description:关闭所有定时任务
     *
     *
     * @Title: QuartzManager.java
     * @Copyright: Copyright (c) 2014
     *
     * @author Comsys-LZP
     * @date 2014-6-26 下午03:50:26
     * @version V2.0
     */
    public static void shutdownJobs() {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            if (!sched.isShutdown()) {
                sched.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 停止调度Job任务
     * @param
     * @return
     * @throws
     */
    public static void pauseTrigger(String jobName){
        Scheduler sched = null;
        try {
            sched = gSchedulerFactory.getScheduler();
//            JobKey jobKey = JobKey.jobKey(jobName,TRIGGER_GROUP_NAME);
            TriggerKey triggerkey=new TriggerKey(jobName, TRIGGER_GROUP_NAME);
            sched.pauseTrigger(triggerkey);


        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return ;
    }

    public static void rescheduleJob(String jobName) {

        Scheduler sched = null;
        try {
            sched = gSchedulerFactory.getScheduler();
            TriggerKey triggerkey=new TriggerKey(jobName, TRIGGER_GROUP_NAME);
            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerkey);
//          sched.rescheduleJob(jobName,TRIGGER_GROUP_NAME,trigger);
            sched.rescheduleJob(triggerkey, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return ;
    }

}
