package net.xby1993.common.util;



import java.util.ArrayList;   
import java.util.HashMap;
import java.util.List;   
import java.util.Map;
  
public class MailBean {   
  
    // 发送邮件服务器   
    private String hostName = "";   
  
    // 发送邮件服务器端口   
    private int port = 994;   
  
    // 发送者邮箱用户名   
    private String userName = "";   
  
    // 发送者邮箱密码   
    private String password = "";   
  
    // 邮件接收者   
     private String toAddress = "";   
  
    // 邮件主题   
    private String subject = "";   
  
    // 邮件内容   
    private String content = "你好";   
    
    //邮件图片
    private String contentImg = "";
  
    // 邮件发送者   
    private String fromAddress = "";   
       
    //附件路径列表   
    private Map<String, String>   fileList=new HashMap<String, String>();   
  
       
     
  
    public Map<String, String> getFileList() {
		return fileList;
	}

	public void setFileList(Map<String, String> fileList) {
		this.fileList = fileList;
	}

	public String getFromAddress() {   
        return fromAddress;   
    }   
  
    public void setFromAddress(String fromAddress) {   
        this.fromAddress = fromAddress;   
    }   
  
    public int getPort() {   
        return port;   
    }   
  
    public void setPort(int port) {   
        this.port = port;   
    }   
  
    public String getHostName() {   
        return hostName;   
    }   
  
    public void setHostName(String hostName) {   
        this.hostName = hostName;   
    }   
  
    public String getUserName() {   
        return userName;   
    }   
  
    public void setUserName(String userName) {   
        this.userName = userName;   
    }   
  
    public String getPassword() {   
        return password;   
    }   
  
    public void setPassword(String password) {   
        this.password = password;   
    }   
  
    public String getToAddress() {   
        return toAddress;   
    }   
  
    public void setToAddress(String toAddress) {   
        this.toAddress = toAddress;   
    }   
  
    public String getSubject() {   
        return subject;   
    }   
  
    public void setSubject(String subject) {   
        this.subject = subject;   
    }   
  
    public String getContent() {   
        return content;   
    }   
  
    public void setContent(String content) {   
        this.content = content;   
    }

	public String getContentImg() {
		return contentImg;
	}

	public void setContentImg(String contentImg) {
		this.contentImg = contentImg;
	}   
  
}  
