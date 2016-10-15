/*
 *
 * this class process eth tunnel request from user.
 * it can return different result by different input.
 * when the input is valid it can send request to tsdn
 * application. After the server return answer,it will build
 * reponse to user.
 */
package com.huawei.tsdn.sampleapp.ethsrv.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.huawei.tsdn.sampleapp.ethsrv.model.EthTunnel;
import com.huawei.tsdn.sampleapp.ethsrv.model.ServiceDataStruct;
import com.huawei.tsdn.sampleapp.ethsrv.service.EthServiceProc;
import com.huawei.tsdn.sampleapp.ethsrv.util.EthTopologyReadUtil;
import com.huawei.tsdn.sampleapp.ethsrv.util.TsdnException;
import com.huawei.tsdn.sampleapp.util.GlobalResourceLoaderServlet;
import com.huawei.tsdn.sampleapp.util.RestLog;
import com.huawei.tsdn.sampleapp.util.TsdnClient;
import com.opensymphony.xwork2.ActionSupport;

/**
 * EthSrvActionSupport srvlet class
 * 
 * @author tWX301955
 *
 */
public class EthSrvActionSupport extends ActionSupport {

    /**
     * this init the class version
     */
    private static final long serialVersionUID = 1L;
    private final org.apache.logging.log4j.Logger log = LogManager.getLogger();
    private String httpAddr;
    private String selectServiceString;// service to delete
    private ServiceDataStruct serviceData;
    private List<EthTunnel> ethTunnels;// to save all the services
    private String sResponseText;
    private String requestHistory;
    private EthServiceProc ethServiceProc;
    private final String ETH_TOPOLOGY_CONFIG = "config/topology_config.xml";
    
    private String st;
    private String et;

    public String getHttpAddr() {
        if (httpAddr == null) {
            httpAddr = GlobalResourceLoaderServlet.getServerAddress();
        }
        return httpAddr;
    }

    public void setHttpAddr(String httpAddr) {
        this.httpAddr = httpAddr;
    }

    public void setSelectServiceString(String selectServiceString) {
        this.selectServiceString = selectServiceString;
    }

    public String getsResponseText() {
        return sResponseText;
    }

    public void setsResponseText(String sResponseText) {
        this.sResponseText = sResponseText;
    }

    public List<EthTunnel> getEthTunnels() {
        return ethTunnels;
    }

    public void setEthTunnels(List<EthTunnel> ethTunnels) {
        this.ethTunnels = ethTunnels;
    }
 
	public String getRequestHistory() {
        return requestHistory;
    }

    public ServiceDataStruct getServiceData() {
        return serviceData;
    }

    public void setServiceData(ServiceDataStruct serviceData) {
        this.serviceData = serviceData;
    }

    /**
     * get config info when init
     */
    public EthSrvActionSupport() {
        String webRootPath = this.getClass().getClassLoader().getResource("/").getPath();
        EthTopologyReadUtil.LoaderTopologyInfo(webRootPath + ETH_TOPOLOGY_CONFIG);
        ethServiceProc = new EthServiceProc(true, new RestLog());
    }

    /**
     * this get configuration
     * 
     * @return method result
     * @throws Exception
     */
    public String getNeConfig() throws Exception {
        boolean result = writeResultToResonpse(EthTopologyReadUtil.buildConfigJsonString());
        if (!result) {
            return ERROR;
        }
        return SUCCESS;
    }

    @Override
    public String execute() throws Exception {
        
        getTunnelList();
        return super.execute();
    }
    
    public String execute1() throws Exception {
        
    	getLinkOchList();
        return super.execute();
    }

    /**
     * the other operator can enuser the service list is new, so just return
     * service list from local
     * 
     * @return
     * @throws Exception
     */
    public String getTunnelList() throws Exception {
    	
        requestHistory = RestLog.getLocalRequestHistory();
 
        queryEthTunnelList(getHttpAddr());
        ethTunnels = ethServiceProc.getEthTunnels();
        return SUCCESS;
    }
    
    public String getLinkOchList() throws Exception {
    	
        requestHistory = RestLog.getLocalRequestHistory();
 
        queryEthTunnelList(getHttpAddr());
        ethTunnels = ethServiceProc.getEthTunnels();
        return SUCCESS;
    }

    /**
     * process the create eth tunnel requset
     * 
     * @return method result
     * @throws Exception
     */

	
	boolean bret = true;
    public void createTunnel() throws Exception {
    	Timer timer = new Timer();
		
//		SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 
		 Date time = getTime();    
		 
//		 Calendar calendar = Calendar.getInstance();
//		 calendar.set(Calendar.SECOND, 0);    // 控制秒
//		 Date time = calendar.getTime(); 
//		 String timeString = ymdhm.format(time);
		 
//		 System.out.println("timeString----->"+timeString);
		 timer.schedule(new TimerTask1(), time);
//		 timer.schedule(new TimerTask2(), time, 1000 * 60);
    }
    
    
//    public void cancelTunnel() throws Exception {
////    	timer.cancel();
//    	
//    	if (timer != null) {
//    		timer.cancel();
//    		timer.purge();
//    		timer = null;
//    		System.out.println("timercancel----->");
//    		}
//    	
//    }

    public Date getTime() {
    	
    	Calendar calendar = Calendar.getInstance();
		 calendar.set(Calendar.SECOND, 0);    // 控制秒
		 Date time = calendar.getTime(); 
		 System.out.println("starttimegetTime----->"+time);
		 return time;
    }
    	
    
    public class TimerTask1 extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String uuid = null;
			String operResult = "Create service success!";
			Date time = getTime();
			SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timeString = ymdhm.format(time);
			
//			if(){//consider the same and more starttime
// 
//				uuid = 
				try{
					uuid = ethServiceProc.createEthTunnel(getHttpAddr(), serviceData);
					System.out.println("starttimeserviceData----->"+serviceData);
					Calendar calendar = Calendar.getInstance();
					Date creatTime = new Date();
					creatTime = calendar.getTime(); 
					System.out.println("starttimeCreateTrue----->"+creatTime);
					
					if(!ethServiceProc.isRunOk())
					{
						operResult = "Create service failed!";
						bret = false;
					}
				} catch (TsdnException ex)
				{
					operResult = "Create service failed!";
					ex.printStackTrace();
					bret = false;
				}
			//	System.out.println("flag-id-->"+id);
//				System.out.println("createUuid-->"+uuid);
//				
//				Object[] options = { "确定", "取消", "帮助" };
//		        int response = JOptionPane.showOptionDialog(null,
//		        		operResult, "选项对话框标题", JOptionPane.YES_OPTION,
//		               JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
//		        if (response == 0) {
//		            System.out.println("您按下了第OK按钮 ");
//		        }
//			}else{
//				cancel();
//			}
		}
    }
    
	public class TimerTask2 extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String uuid = null;
			String operResult = "Delete service success!";
			
			Date time = getTime();
			SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timeString = ymdhm.format(time);
			
//			if(){//consider the same and more endtime
			try {
				ethServiceProc.deleteEthTunnel(getHttpAddr(), uuid);
				Calendar calendar = Calendar.getInstance();
				Date deleteTime = new Date();
				deleteTime = calendar.getTime(); 
				System.out.println("endtimeCreateTrue----->"+deleteTime);
				
			} catch (TsdnException ex)
			{
				log.error(ex.getMessage());
				log.error("Delete service ID:" + serviceData.getUuid() + " failed!");
				operResult = "Delete some service failed!";
			}
			
			System.out.println("deleteUuid-->"+uuid);
//			Object[] options = { "确定", "取消", "帮助" };
//	        int response = JOptionPane.showOptionDialog(null,
//	        		operResult, "选项对话框标题", JOptionPane.YES_OPTION,
//	               JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
//	        if (response == 0) {
//	            System.out.println("您按下了第OK按钮 ");
//	        }else{
//				cancel();
//			}
		    }
//		}
	}
    /**
     * process the create eth tunnel requset
     * 
     * @return method result
     * @throws Exception
     */
    public String deleteTunnel() throws Exception {
    	
        log.info("del service string is " + selectServiceString);
        
        String operResult = "Delete service success!";
        
        String[] selArrString = null;
        boolean bret = false;
        if (selectServiceString != null) 
		{
			selArrString = selectServiceString.split(",");
			for (int i = 0; i < selArrString.length; i++) 
			{
				
				String tmp = selArrString[i];
				try {
					ethServiceProc.deleteEthTunnel(getHttpAddr(), tmp);
					
				} catch (TsdnException ex)
				{
					log.error(ex.getMessage());
					log.error("Delete service ID:" + tmp + " failed!");
					operResult = "Delete some service failed!";
					break;
				}
				
			}
		}
        
        boolean result = writeResultToResonpse(operResult);
        
        if (!result) {
            return ERROR;
        }
        
        // get the new service list after the delete operator
        ethServiceProc.setIsRecord(false);
        queryEthTunnelList(getHttpAddr());
        return SUCCESS;
    }

    
	/**
	 * send request to server and get eth service detail
	 * 
	 * @param httpAddress
	 */
	public void queryEthTunnelList(String httpAddress) 
	{
		
		try {
		    if (ethServiceProc.getEthTunnel(httpAddress) == true) {
               log.debug("Get eth service success!");
            } else {
               log.error("Get eth service failed!");
           }
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TsdnException e) {
			e.printStackTrace();
		}
	}

	
    /**
     * response result to user page
     * 
     * @param result
     */
    public boolean writeResultToResonpse(String result) {
        HttpServletResponse reponse = ServletActionContext.getResponse();
        reponse.setContentType("text/html;charset=UTF-8");  
        PrintWriter writer = null;
        try {
            writer = reponse.getWriter();
        } catch (IOException e) {
            log.error(e.toString());
            return false;
        }
        writer.write(result);
        writer.close();
        return true;
    }
    
    /**
     * @description modify the serviceName or BW
     * @return
     * @throws Exception
     */
    public String modifyTunnel() throws Exception{
        if(!serviceData.isModifyValid()){
         return "Invalid input parameter !";
        }
        boolean processResult = true;
        String operResult = "Modify service success !";
        try{
         ethServiceProc.modifyEthTunnel(getHttpAddr(), serviceData);
        }catch(TsdnException ex){
         processResult = false;
         operResult = "Modify service failed !";
         ex.printStackTrace();
        }
        boolean result = writeResultToResonpse(operResult);
        if(!result || !processResult){//回复给用户界面结果成功为true且修改成功才能真正修改完成。
         return ERROR;
        }
        ethServiceProc.setIsRecord(false);
        queryEthTunnelList(getHttpAddr());
        return SUCCESS;
       } 
}
