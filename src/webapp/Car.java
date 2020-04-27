package webapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.inject.Inject;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Car")
public class Car extends HttpServlet {
	private static final long serialVersionUID = 1L;

	Random rand = new Random();
	private Integer oilPressure;
	private Integer engineTemperature;
	private Integer gearPressure;
	private Boolean isRaceOn;
	Timer timer = new Timer();

	//...
	
    public Car() {
        super();
        isRaceOn = true;
        setOilPressure();
        setEngineTemperature();
        setGearPressure();
    }

    public void setOilPressure(){
    	this.oilPressure = rand.nextInt(999);
    }
    
    public void setEngineTemperature(){
    	this.engineTemperature = rand.nextInt(100);
    }
    
    public void setGearPressure(){
    	this.gearPressure = rand.nextInt(1000);
    }
    
    public void endRace(){
    	this.isRaceOn = false;
    }
    
    public String getCurrentTime(){
    	String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
    	return timestamp;
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stubhe
		PrintWriter writer = response.getWriter();

		try {
			Context context = new InitialContext();
			Topic topic = (Topic)context.lookup("java:/jms/topic/BolideParamsTopic");
		
			TopicConnectionFactory tcf = (TopicConnectionFactory)context.lookup("java:/ConnectionFactory");
		
			TopicConnection tc = tcf.createTopicConnection();
			TopicSession ts = tc.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
			while (isRaceOn)
			{
				this.setEngineTemperature();
				this.setGearPressure();
				this.setOilPressure();
				String msg = "Oil Pressure; Engine temperature; Gear pressure; Timestamp;" + 
				this.oilPressure.toString() + ";" + 
				this.engineTemperature.toString() + ";" + 
				this.gearPressure + ";" + 
				getCurrentTime();
				TextMessage textMessage = ts.createTextMessage(msg);
				TopicPublisher tp = ts.createPublisher(topic);
				tp.send(textMessage);
				writer.write(msg);
				Thread.sleep(15000);
			}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
		 	if (writer != null) {
		 		writer.close();
		}
		 	}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	

}
