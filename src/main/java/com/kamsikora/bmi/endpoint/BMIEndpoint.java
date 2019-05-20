package com.kamsikora.bmi.endpoint;

import com.kamsikora.bmi.service.BMIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import com.kamsikora.wsbmi.BMI;
import com.kamsikora.wsbmi.GetBMIRequest;
import com.kamsikora.wsbmi.GetBMIResponse;
import com.kamsikora.wsbmi.ServiceStatus;
import com.kamsikora.wsbmi.Unit;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


@Endpoint
public class BMIEndpoint {

    @Autowired
    private BMIService bmiService;

    @PayloadRoot(namespace = "http://kamsikora.com/wsbmi",
            localPart = "getBMIRequest")
    @ResponsePayload
    public GetBMIResponse GetBMIRequest(@RequestPayload GetBMIRequest request) {
        GetBMIResponse response = new GetBMIResponse();
        ServiceStatus serviceStatus = new ServiceStatus();

        if (request.getHeight() == 0) {
            serviceStatus.setMessage("Missing value height");
        }
        else if (request.getMass() == 0) {
            serviceStatus.setMessage("Missing value mass");
        }
        else if (request.getUnit() != Unit.METRIC && request.getUnit() != Unit.IMPERIAL) {
            serviceStatus.setMessage("Missing value unit");
        }
        else {
            BMI bmi = new BMI();
            bmi.setHeight(request.getHeight());
            bmi.setMass(request.getMass());
            bmi.setUnit(request.getUnit());
            double value = 0;

            if(bmi.getUnit() == Unit.METRIC)
            {
                value = bmi.getMass()/(bmi.getHeight()*bmi.getHeight());
                serviceStatus.setValue(value);
            }
            else if(bmi.getUnit() == Unit.IMPERIAL)
            {
                value = (bmi.getMass()/(bmi.getHeight()*bmi.getHeight()))*703;
                serviceStatus.setValue(value);
            }

            if(value < 15)
            {
                serviceStatus.setMessage("Very severely underweight");
            }
            else if(value <= 16 && value >= 15)
            {
                serviceStatus.setMessage("Severely underweight");
            }
            else if(value <= 18.5 && value > 16)
            {
                serviceStatus.setMessage("Underweight");
            }
            else if(value <= 25 && value > 18.5)
            {
                serviceStatus.setMessage("Normal (healthy weight)");
            }
            else if(value <= 30 && value > 25)
            {
                serviceStatus.setMessage("Overweight");
            }
            else if(value <= 35 && value > 30)
            {
                serviceStatus.setMessage("Obese Class I (Moderately obese)");
            }
            else if(value <= 40 && value > 35)
            {
                serviceStatus.setMessage("Obese Class II (Severely obese)");
            }
            else if(value <= 45 && value > 40)
            {
                serviceStatus.setMessage("Obese Class III (Very severely obese)");
            }
            else if(value <= 50 && value > 45)
            {
                serviceStatus.setMessage("Obese Class IV (Morbidly Obese)");
            }
            else if(value <= 60 && value > 50)
            {
                serviceStatus.setMessage("Obese Class V (Super Obese)");
            }
            else if(value > 60)
            {
                serviceStatus.setMessage("Obese Class VI (Hyper Obese)");
            }
        }

        response.setServiceStatus(serviceStatus);
        return response;
    }
}
