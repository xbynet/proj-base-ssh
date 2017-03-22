package net.xby1993.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;


public class MailUtil {
	private static final Logger log=LoggerFactory.getLogger(MailUtil.class);

	
	private String mailto="xbynet@outlook.com";
	private String mailFrom="xbynet@163.com";
	private String subjectPrefix="backup data on ";
	public void sendSimpleMail(String content){
		JavaMailSender mailSender=SpringContextHolder.getBean("mailSender", JavaMailSender.class);
		String subject=subjectPrefix+getNowDateStr();
//		SimpleMailMessage msg=new SimpleMailMessage();
		/**为防止linux下发送的邮件中文乱码*/
		MimeMessage mime=mailSender.createMimeMessage();
		MimeMessageHelper msg=new MimeMessageHelper(mime,"utf-8");
		try {
			msg.setFrom(mailFrom);
			msg.setTo(mailto);
			msg.setSubject(subject);
			msg.setText(content);
			log.debug("邮件初始化成功");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("邮件发送失败{}",e);
		}
		
		mailSender.send(mime);
		log.debug("邮件发送成功");
	}
	public String getNowDateStr(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d=new Date();
		return sdf.format(d);
	}
}
