package net.xby1993.test.crawl;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import net.xby1993.common.webmagic.downloader.DynamicTest;
import net.xby1993.common.webmagic.downloader.MaoyanTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;  
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;  
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;  
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;  

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring-app.xml" })
@WebAppConfiguration
@Transactional
public class Test1 {
	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;  

	@Before
	public void setup() {
		// webAppContextSetup 注意上面的static import
		// webAppContextSetup 构造的WEB容器可以添加fileter 但是不能添加listenCLASS
		// WebApplicationContext context =
		// ContextLoader.getCurrentWebApplicationContext();
		// 如果控制器包含如上方法 则会报空指针
		this.mockMvc = webAppContextSetup(this.wac).build();
	}

//	@Test
	// 有些单元测试你不希望回滚
	@Rollback(false)
	public void ownerId() throws Exception {
		mockMvc.perform((get("/spring/rest/4.do"))).andExpect(status().isOk()).andDo(print());
	}
	
	@Test
	public void test1(){
		new DynamicTest().start();
		try {
			Thread.sleep(100000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
