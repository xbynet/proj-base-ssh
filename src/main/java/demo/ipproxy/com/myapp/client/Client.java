package demo.ipproxy.com.myapp.client;


import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import demo.ipproxy.com.myapp.crawer.ProxyIpCrawer;
import demo.ipproxy.com.myapp.crawer.impl.ProxyIpCrawerImpl;
import demo.ipproxy.com.myapp.entity.ProxyIp;
import demo.ipproxy.com.myapp.proxy.ProxyPool;

/**
 * Created by gaorui on 16/12/26.
 */
public class Client implements Job {

    public ProxyIpCrawer proxyIpCrawer =  new ProxyIpCrawerImpl();
    private static int count = 0;

    public static ProxyPool proxyPool  = new ProxyPool();


   /* public Client(ProxyIpCrawer proxyIpCrawer) {

        this.proxyIpCrawer = proxyIpCrawer;
    }*/




    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        count++;
        System.out.println("#####第" + count + "次开始爬取#####");
        this.proxyIpCrawer.fetchProxyIp();
        List<ProxyIp> allProxyIps = this.proxyIpCrawer.allProxyIps;
        for (ProxyIp Proxyip : allProxyIps) {

            System.out.println("proxyPool:" + Proxyip.getIp() + ":" + Proxyip.getPort());

            proxyPool.add(Proxyip.getIp(), Proxyip.getPort());


        }
        System.out.println("#####爬取完毕#####");
    }
}
