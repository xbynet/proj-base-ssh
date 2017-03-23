package demo.ipproxy.com.myapp.main;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import demo.ipproxy.com.myapp.client.Client;
import demo.ipproxy.com.myapp.proxy.HttpProxy;
import demo.ipproxy.com.myapp.proxy.ProxyPool;
import demo.ipproxy.com.myapp.util.HttpStatus;
import demo.ipproxy.com.myapp.util.ProxyIpCheck;

/**
 * Created by gaorui on 16/12/28.
 */
public class main implements Job {
    ProxyPool proxyPool = null;




    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        proxyPool = Client.proxyPool;
        System.out.println("#####爬虫ip池开始测试#####");
        int idleNum = proxyPool.getIdleNum();
        while(proxyPool.getIdleNum()>0) {
            HttpProxy httpProxy = proxyPool.borrow();
            HttpStatus code = ProxyIpCheck.Check(httpProxy.getProxy());
            System.err.println(httpProxy.getProxy()+":"+code);

            proxyPool.reback(httpProxy, code); // 使用完成之后，归还 Proxy,并将请求结果的 http 状态码一起传入


        }

        System.out.println("#####爬虫ip池测试完成#####");
        proxyPool.allProxyStatus();  // 可以获取 ProxyPool 中所有 Proxy 的当前状态
    }
}
