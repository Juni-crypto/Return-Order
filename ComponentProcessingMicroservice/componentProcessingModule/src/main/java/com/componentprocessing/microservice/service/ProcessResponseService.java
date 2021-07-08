package com.componentprocessing.microservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.componentprocessing.microservice.client.PaymentClient;
import com.componentprocessing.microservice.controller.ComponentProcessingController;
import com.componentprocessing.microservice.model.ProcessResponse;
import com.componentprocessing.microservice.repository.ProcessResponseRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProcessResponseService {
	
	@Autowired
	ProcessResponseRepo processResponseRepo;

	@Autowired
	private PaymentClient paymentClient;

	public ProcessResponse loadByRequestId(String requestId) {
		
		return processResponseRepo.findByRequestId(requestId);
	}

	public void deleteRequest(ProcessResponse obj) {
		// TODO Auto-generated method stub
		processResponseRepo.delete(obj);
	}
	public String messageConfirmation( String requestId) {
		log.info("Inside Service");
		
    	 if(requestId!="0") {
      	   
      	   log.info("Successful Operation Message displayed");
      	   return "Operation Successful! "+requestId +" Deleted";
         }
         else {
      	   return "Operation Not Successful! "+requestId +"not Deleted";
         }
	}

}
