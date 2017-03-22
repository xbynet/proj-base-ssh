package net.xby1993.common.shiro;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import net.xby1993.common.session.SessionUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/login")
public class LoginController {
	private static final Logger log=LoggerFactory.getLogger(LoginController.class);
	
	/**
	 * 验证码识别逻辑，根据项目具体需求来实现。
	 * @param req
	 * @return
	 */
	public boolean validateKaptcha(HttpServletRequest req){
		return true;
	}

	@RequestMapping("/do")
	public ModelAndView doLogin2(ModelAndView mav,String username,String password){
		if(checkLogin(mav)){
			return mav;
		};
		UsernamePasswordToken token=new UsernamePasswordToken(username, PasswordHelper.encryptPassword(password));
		Subject curUser=SecurityUtils.getSubject();
		String msg="";
		boolean status=false;
		try{
			curUser.login(token);
			status=true;
			SessionUtil.get().setAttribute("user", curUser.getPrincipal().toString());
		} catch ( UnknownAccountException uae ) { 
			msg="用户名或密码不正确";
			log.info("用户名{}不存在",username);
		} catch ( IncorrectCredentialsException ice ) {
			msg="用户名或密码不正确";
			log.info("用户{}的密码{}不正确",username,password);
		} catch ( LockedAccountException lae ) {
			msg="用户已被锁定,请联系管理人员解锁";
			log.info("用户{}已经被锁定",username);
		} catch ( ExcessiveAttemptsException eae ) {
			msg="用户尝试登录次数过多,请稍后重试";
			log.info("用户{}重复登录次数过多",username);
		} catch ( AuthenticationException ae ) {
			msg="登录校验失败";
			log.info("用户{}登录异常",username);
			log.info("异常信息",ae);
		} 
		if(status){
			if(!setRedirectReferer(mav)){
				mav.setViewName("redirect:/");				
			}
		}else{
			mav.setViewName("auth/login");
			mav.addObject("errmsg", msg);
		}
		return mav;
	}
	@RequestMapping("/get")
	public ModelAndView getLogin(ModelAndView mav){
		if(!checkLogin(mav)){
			mav.setViewName("auth/login");
		}
		return mav;
	}
	@RequestMapping("/logout")
	public ModelAndView loginout(ModelAndView mav){
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		mav.setViewName("redirect:/");
		return mav;
	}

	public static void main(String[] args) throws Exception {
		URL  url = new  java.net.URL("http://blog.csdn.net/xbynet");
		String host = url.getHost();// 获取主机名 
		System.out.println("host:"+host);// 结果 blog.csdn.net
		System.out.println(url.getProtocol());
	}
	private boolean checkLogin(ModelAndView mav){
		if(SessionUtil.isLogined()){
			if(!setRedirectReferer(mav)){
				mav.setViewName("redirect:/");				
			}
			return true;
		}
		return false;
	}
	private boolean setRedirectReferer(ModelAndView mav){
		HttpServletRequest req=SessionUtil.getRequest();
		String referer=req.getHeader("referer");
		String domain="";
		try {
			URL url=new URL(req.getRequestURL().toString());
			domain=url.getProtocol()+"://"+url.getProtocol()+url.getHost();
		} catch (MalformedURLException e) {
			log.warn("获取域名失败",e);
		}
		if(referer.startsWith(domain) && StringUtils.isNotBlank(domain)){
			mav.setView(new RedirectView(referer));
			return true;
		}
		return false;
	}

}
