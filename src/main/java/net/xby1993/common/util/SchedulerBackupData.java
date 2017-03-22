package net.xby1993.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时备份doukuwiki数据
 * @author Administrator
 *
 */
@Component
public class SchedulerBackupData {
	private static final Logger log=LoggerFactory.getLogger(SchedulerBackupData.class);

	@Scheduled(cron="0 0 9 * * ?") //采用cron配置。
	public void dokuwikiBack(){
		log.info("任务调度开始");
//		SchedulerService service=new DokuwikiBackService();
//		service.executeTask();
		log.info("备份任务调度执行成功");
	}
//	@Scheduled(fixedDelay=1*60*1000) //采用cron配置。
//	public void dokuwikiBackTest(){
//		log.info("任务调度开始");
//		ApplicationContext ctx=SpringContextUtil.getContext();
////		SchedulerService service=new DokuwikiBackService();
////		SchedulerService service=new Test();
////		service.executeTask();
//		log.info("备份任务调度执行成功");
//	}
}
