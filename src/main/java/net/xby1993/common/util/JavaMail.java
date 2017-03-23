package net.xby1993.common.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;   
  
import javax.activation.DataHandler;   
import javax.activation.FileDataSource;   
import javax.mail.Authenticator;   
import javax.mail.Message;   

import javax.mail.Multipart;   
import javax.mail.Part;   
import javax.mail.PasswordAuthentication;   
import javax.mail.Session;   
import javax.mail.Transport;   
  
import javax.mail.internet.InternetAddress;   
import javax.mail.internet.MimeBodyPart;   
import javax.mail.internet.MimeMessage;   
import javax.mail.internet.MimeMultipart;   
import javax.mail.internet.MimeUtility;



  
  
  
public class JavaMail {   
  
    @SuppressWarnings("static-access")   
    public void setMail(final MailBean mailBean) throws Exception {   
         
            // 设置JavaMail属性   
            Properties props = new Properties();   
            // 设置邮件服务器端口   
            props.put("mail.smtp.port", String.valueOf(mailBean.getPort()));   
            // SMTP邮件服务器IP地址或主机名   
            props.put("mail.smtp.host", mailBean.getHostName());   
            props.put("mail.smtp.auth", "true");  
            props.put("mail.debug", "true");
            props.put("mail.smtp.socketFactory.port", String.valueOf(mailBean.getPort()));
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");

            // 需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）   
            EmailAuthenticator auth = new EmailAuthenticator(mailBean.getUserName(), mailBean.getPassword());   
            // 根据已配置的JavaMail属性创建Session实例   
            
            Session mailSession = Session.getInstance(props, (Authenticator) auth);   
            
            // 你可以在控制台（console)上看到发送邮件的过程   
            mailSession.setDebug(false);   
            MimeMessage msg = new MimeMessage(mailSession);   
            msg.setHeader("Content-type", "text/html;charset=GBK");
            InternetAddress[] address = InternetAddress.parse(mailBean.getToAddress(), false);   
            // 设置邮件接收者   
            msg.setRecipients(Message.RecipientType.TO, address);   
            // 设置邮件主题   
            msg.setSubject(mailBean.getSubject(),"GBK");   
            // 设置邮件时间   
            msg.setSentDate(new Date());   
            // 设置邮件发送者   
            msg.setFrom(new InternetAddress(mailBean.getFromAddress()));   
            
            Multipart multipart = new MimeMultipart();   
            // 加入文本内容   
            MimeBodyPart mimeBodyPart = new MimeBodyPart();   

            mimeBodyPart.setHeader("Content-type", "text/html;charset=GBK");
            mimeBodyPart.setContent(mailBean.getContent(),"text/html;charset=GBK");   
            multipart.addBodyPart(mimeBodyPart);   
            
            //-------正文加图片---
            if(mailBean.getContentImg()!=null && !mailBean.getContentImg().equals("")){
            	MimeBodyPart jpgBody = new MimeBodyPart();  
            	FileDataSource fds = new FileDataSource(mailBean.getContentImg());  
            	jpgBody.setDataHandler(new DataHandler(fds));  
            	jpgBody.setContentID("logo_jpg");  
            	multipart.addBodyPart(jpgBody);
            }
            
            /*
             * 网上的方法
            	String body = "统计图表请查看附件<br><img src = \"cid:logo_jpg\">";  
	            MimeBodyPart content = createContent(body, mailBean.getContentImg());  
	            MimeMultipart allPart = new MimeMultipart("mixed");  
	            allPart.addBodyPart(content);  
	     
	            // 将上面混合型的 MimeMultipart 对象作为邮件内容并保存  
	            msg.setContent(allPart);  
	            
	            Transport.send(msg);
            */
            
            Map<String, String>   fileList = mailBean.getFileList();   
            //加入附件   
           Iterator<String> iterator= fileList.keySet().iterator();
           while (iterator.hasNext()) {
        	  String path = iterator.next();
        	  MimeBodyPart bodyPart = new MimeBodyPart();   
              //得到数据源   
              FileDataSource fileDataSource = new FileDataSource(path);   
             
              bodyPart.setDataHandler(new DataHandler(fileDataSource));   
              bodyPart.setDisposition(Part.ATTACHMENT);   
              //设置文件名   
              sun.misc.BASE64Encoder enc  =   new  sun.misc.BASE64Encoder();


             
					bodyPart.setFileName(MimeUtility.encodeWord(fileList.get(path)));
				
              multipart.addBodyPart(bodyPart);   
		}
           
            msg.setContent(multipart);   
            // 创建Transport对象   
            Transport.send(msg);
    }   
    
    /**  
     * 根据传入的邮件正文body和文件路径创建图文并茂的正文部分  
     */ 
    public MimeBodyPart createContent(String body, String fileName)  
            throws Exception {  
        // 用于保存最终正文部分  
        MimeBodyPart contentBody = new MimeBodyPart();  
        // 用于组合文本和图片，"related"型的MimeMultipart对象  
        MimeMultipart contentMulti = new MimeMultipart("related");  
 
        // 正文的文本部分  
        MimeBodyPart textBody = new MimeBodyPart();  
        textBody.setContent(body, "text/html;charset=gbk");  
        contentMulti.addBodyPart(textBody);  
 
        // 正文的图片部分  
        MimeBodyPart jpgBody = new MimeBodyPart();  
        FileDataSource fds = new FileDataSource(fileName);  
        jpgBody.setDataHandler(new DataHandler(fds));  
        jpgBody.setContentID("logo_jpg");  
        contentMulti.addBodyPart(jpgBody);  
 
        // 将上面"related"型的 MimeMultipart 对象作为邮件的正文  
        contentBody.setContent(contentMulti);  
        return contentBody;  
    }  
  
    // 邮件用户身份验证类   
    class EmailAuthenticator extends Authenticator {   
  
        private String username;   
  
        private String userpass;   
  
        EmailAuthenticator(String un, String up) {   
            super();   
            username = un;   
            userpass = up;   
        }   
  
        public PasswordAuthentication getPasswordAuthentication() {   
            return new PasswordAuthentication(username, userpass);   
        }   
    }   
  
    public static void main(String[] args) throws Exception {   
  
       JavaMail javaMail = new JavaMail();   
        MailBean mailBean = new MailBean();   
        mailBean.setSubject("teststbbbbbbbbbs");
        mailBean.setContent("<iframe  scrolling='auto'  frameborder='0'  src='http://stb.ipmacro.com:8080/channel/admin/index.htm' style=''></iframe>");
        mailBean.setToAddress("385835032@qq.com");
        javaMail.setMail(mailBean);
  
    }   
  
}  
