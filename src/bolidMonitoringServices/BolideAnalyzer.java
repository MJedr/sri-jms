package bolidMonitoringServices;

import java.util.Arrays;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


@MessageDriven(
		activationConfig = { @ActivationConfigProperty(
				propertyName = "destination", propertyValue = "topic/BolideParamsTopic"), 
				@ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Topic")
		}, 
		mappedName = "CarParameterTopic")
public class BolideAnalyzer implements MessageListener {

    /**
     * Default constructor. 
     */
    public BolideAnalyzer() {
        
    }
    public String analyzeOilPressure(Integer oilPressure){
    	String warning = "low";
    	if (oilPressure > 2000 & oilPressure < 5000){
    		warning = "medium";
    	}
    	else if (oilPressure >= 5000){
    		warning = "high";
    	}
    	return warning;
    }
    
    public String analyzeEngineTemperature(Integer engineTemperature){
    	String warning = "low";
        	if (engineTemperature > 200 & engineTemperature < 500){
        		warning = "medium";
        	}
        	else if (engineTemperature >= 500){
        		warning = "high";
        	}
        	return warning;
    }
	
    public String analyzeGearPressure(Integer gearPressure){
    	String warning = "low";
    	if (gearPressure > 200 & gearPressure < 500){
    		warning = "medium";
    	}
    	else if (gearPressure >= 500){
    		warning = "high";
    	}
    	return warning;
}

    public void onMessage(Message message) {
        TextMessage msg = (TextMessage) message;
        try {
        	String values = msg.getText().replaceAll("\\D+", "-");
        	String [] paramsArray = values.split("-");
        	Integer oilPressure = Integer.parseInt(paramsArray[1]);
        	Integer engineTemperature = Integer.parseInt(paramsArray[2]);
        	Integer gearPressure = Integer.parseInt(paramsArray[3]);
        	
        	String oilPressureWarning = this.analyzeOilPressure(oilPressure);
        	String engineTemperatureWarning = this.analyzeOilPressure(engineTemperature);
        	String gearPressureWarning = this.analyzeOilPressure(gearPressure);
        	
        	// System.out.println(Arrays.toString(paramsArray));
			System.out.println("--- MSG: " + oilPressureWarning + " " + engineTemperatureWarning + " " + gearPressureWarning);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

}