package com.payment.microservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.payment.microservice.model.PaymentDetails;
import com.payment.microservice.repository.PaymentDetailsRepo;

@Service
public class PaymentUpdateService {

	 @Autowired
	    PaymentDetailsRepo paymentDetailsRepo;
	    
	    
		public PaymentDetails updateAccountBalance(Integer creditnumber, int prCharge ) {
	    	PaymentDetails obj = paymentDetailsRepo.findByCardNumber(creditnumber);
	    	obj.setCreditLimit(obj.getCreditLimit()+prCharge);
	    	obj.setProcessingCharge(obj.getProcessingCharge()-prCharge);
	    	paymentDetailsRepo.save(obj);
	   
	    	return obj;
	    }
}
