package net.xby1993.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;



public class ProcessUtils {
	   
    /**
     * 运行一个外部命令，返回状态.若超过指定的超时时间，抛出TimeoutException
     * @param command
     * @param timeout
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws TimeoutException
     */
	 public static String value="";
	 public static int executeCommand( final long timeout,final String name,final String  command) throws IOException, InterruptedException, TimeoutException {
	        Process process = Runtime.getRuntime().exec(command);
	       int state=0;
	       if(name!=null&&!name.equals("")){
	    	   state=1;
	       }
	        Worker worker = new Worker(process,state);
	        worker.start();
	        try {
	            worker.join(timeout);
	              if(state==1){
	            	  if(value.matches(name)){
	            		  value="";
	            		  return -2;
	            	  }else{
	            		  value="";
	            	  }
	              }
	            if (worker.exit != null){
	                return worker.exit;
	            } else{
	               return -1;
	            }
	        } catch (InterruptedException ex) {
	            worker.interrupt();
	            Thread.currentThread().interrupt();
	            throw ex;
	        } finally {
	            process.destroy();
	        }
	    }
    public static int executeCommand( final long timeout,final String name,final String [] command) throws IOException, InterruptedException, TimeoutException {
        Process process = Runtime.getRuntime().exec(command);
       int state=0;
       if(name!=null&&!name.equals("")){
    	   state=1;
       }
        Worker worker = new Worker(process,state);
        worker.start();
        try {
            worker.join(timeout);
              if(state==1){
            	  if(value.matches(name)){
            		  value="";
            		  return -2;
            	  }else{
            		  value="";
            	  }
              }
            if (worker.exit != null){
                return worker.exit;
            } else{
               return -1;
            }
        } catch (InterruptedException ex) {
            worker.interrupt();
            Thread.currentThread().interrupt();
            throw ex;
        } finally {
            process.destroy();
        }
    }
   

    private static class Worker extends Thread {
        private final Process process;
        private Integer exit;
        private Integer state=0;
        private Worker(Process process,Integer state) {
            this.process = process;
            this.state=state;
            
        }

        public void run() {
   
            try {
            	   final InputStream is1 = process.getInputStream();   
      			 final InputStream is2 = process.getErrorStream();  
      			 
      			   new Thread() {  
      				     public void run() {  
      				        BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));  
      				         try {  
		      				       	int key=0;
		      		    			String htmlCode="";
		      		    			char keyValue[]=new char[1000]; 
		      		    			while ((key = br1.read(keyValue))>=0)
		      		    			{
		      		    				char keyValueItem[]=new char[key]; 
		      		    			   for(int i=0;i<key;i++){
		      		    				   keyValueItem[i]=keyValue[i];
		      		    			   }
		      		    				htmlCode += new String(keyValueItem);
		      		    				keyValue=new char[1000]; 
		      		    			}
		      		    			value=htmlCode;
	      				        	 
	      				         } catch (Exception e) {  
	      				              e.printStackTrace();  
	      				         }  
      				         finally{  
      				              try {  
      				                is1.close();  
      				              } catch (IOException e) {  
      				                 e.printStackTrace();  
      				             }  
      				           }  
      				         }  
      				      }.start();  
      				   new Thread() {   
      				    	       public void  run() {   
      				    	         BufferedReader br2 = new  BufferedReader(new  InputStreamReader(is2));   
      				    	            try {   
      				    	               String line2 = null ;   
      				    	               while ((line2 = br2.readLine()) !=  null ) {   
      				    	            	  
      				    	                    if (line2 != null){}  
      				    	               }   
      				    	             } catch (IOException e) {   
      				    	                   e.printStackTrace();  
      				    	             }   
      				    	            finally{  
      				    	               try {  
      				    	                   is2.close();  
      				    	               } catch (IOException e) {  
      				    	                   e.printStackTrace();  
      				    	               }  
      				    	             }  
      				    	          }   
      				    	        }.start();	  
                exit = process.waitFor();
                process.destroy();
            } catch (Exception ignore) {
                return;
            }
        }
    }
    public static void main(String[] args)
	{
    	try
		{
			
    		ProcessUtils.executeCommand(60000, null, "/usr/local/tomcat/webapps/dongle_resource/command/btCache/DgReadVersion -f /home/duan/system.release.img.ori.v52");
			 String value= ProcessUtils.value;
			 System.out.println(value);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
}

