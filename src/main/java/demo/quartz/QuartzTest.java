package demo.quartz;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

/**
 *Quartz API核心接口有：
StdSchedulerFactory
Scheduler – 与scheduler交互的主要API；
Job – 你通过scheduler执行任务，你的任务类需要实现的接口；
JobDetail – 定义Job的实例；
Trigger – 触发Job的执行；
JobBuilder – 定义和创建JobDetail实例的接口;
TriggerBuilder – 定义和创建Trigger实例的接口；
XxxScheduleBuilder
JobStore类：任务持久化，可选的有在内存中，在数据库中。

Scheduler被创建后，可以增加、删除和列举Job和Trigger，以及执行其它与调度相关的操作（如暂停Trigger）。但是，Scheduler只有在调用start()方法后，才会真正地触发trigger（即执行job）

从DSL里静态导入的语句如下：
    import static org.quartz.JobBuilder.*;
    import static org.quartz.SimpleScheduleBuilder.*;
    import static org.quartz.CronScheduleBuilder.*;
    import static org.quartz.CalendarIntervalScheduleBuilder.*;
    import static org.quartz.TriggerBuilder.*;
    import static org.quartz.DateBuilder.*;
 ScheduleBuilder接口的各种实现类，可以定义不同类型的调度计划（schedule）；
 DateBuilder类包含很多方法，可以很方便地构造表示不同时间点的java.util.Date实例
 
  *一个job就是一个实现了Job接口的类
 *传递给execute()方法的JobExecutionContext对象中保存着该job运行时的一些信息 ，执行job的scheduler的引用，触发job的trigger的引用，JobDetail对象引用，以及一些其它信息。
 *JobDetail对象是在将job加入scheduler时，由客户端程序（你的程序）创建的。它包含job的各种属性设置，以及用于存储job实例状态信息的JobDataMap。
 *Trigger用于触发Job的执行。当你准备调度一个job时，你创建一个Trigger的实例，然后设置调度相关的属性。Trigger也有一个相关联的JobDataMap，用于给Job传递一些触发相关的参数。Quartz自带了各种不同类型的Trigger，最常用的主要是SimpleTrigger和CronTrigger。
 *
 *SimpleTrigger主要用于一次性执行的Job（只在某个特定的时间点执行一次），或者Job在特定的时间点执行，重复执行N次，每次执行间隔T个时间单位。
 *CronTrigger在基于日历的调度上非常有用，如“每个星期五的正午”，或者“每月的第十天的上午10:15”等。
 *
 *在开发Quartz的时候，我们认为将调度和要调度的任务分离是合理的。在我们看来，这可以带来很多好处。
例如，Job被创建后，可以保存在Scheduler中，与Trigger是独立的，同一个Job可以有多个Trigger；这种松耦合的另一个好处是，当与Scheduler中的Job关联的trigger都过期时，可以配置Job稍后被重新调度，而不用重新定义Job；还有，可以修改或者替换Trigger，而不用重新定义与之关联的Job。

Key
将Job和Trigger注册到Scheduler时，可以为它们设置key，配置其身份属性。Job和Trigger的key（JobKey和TriggerKey）可以用于将Job和Trigger放到不同的分组（group）里，然后基于分组进行操作。同一个分组下的Job或Trigger的名称必须唯一，即一个Job或Trigger的key由名称（name）和分组（group）组成。

如何给job实例增加属性或配置呢？如何在job的多次执行中，跟踪job的状态呢？答案就是:JobDataMap，JobDetail对象的一部分。



如果你使用的是持久化的存储机制（本教程的JobStore部分会讲到），在决定JobDataMap中存放什么数据的时候需要小心，因为JobDataMap中存储的对象都会被序列化，因此很可能会导致类的版本不一致的问题；Java的标准类型都很安全，如果你已经有了一个类的序列化后的实例，某个时候，别人修改了该类的定义，此时你需要确保对类的修改没有破坏兼容性；更多细节，参考现实中的序列化问题。另外，你也可以配置JDBC-JobStore和JobDataMap，使得map中仅允许存储基本类型和String类型的数据，这样可以避免后续的序列化问题。
在Job执行时，JobExecutionContext中的JobDataMap为我们提供了很多的便利。它是JobDetail中的JobDataMap和Trigger中的JobDataMap的并集，但是如果存在相同的数据，则后者会覆盖前者的值。

 */
public class QuartzTest {

    public static void main(String[] args) {

        try {
            // Grab the Scheduler instance from the Factory 
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            
         // define the job and tie it to our HelloJob class
            JobDetail job = newJob(HelloJob.class)
                .withIdentity("job1", "group1")
                .build();

            // Trigger the job to run now, and then repeat every 40 seconds
            Trigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(40)
                        .repeatForever())            
                .build();

            // Tell quartz to schedule the job using our trigger
            scheduler.scheduleJob(job, trigger);
            // and start it off
            

            scheduler.shutdown();

        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }
}