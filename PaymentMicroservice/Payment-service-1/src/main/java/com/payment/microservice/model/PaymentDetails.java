package com.payment.microservice.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
@Component
@Entity
@Table(name="paymentdetails")
public class PaymentDetails {
	

	@Id
	private String requestId;
	

	private Integer cardNumber;
	
	

	private int creditLimit;            
	

	private int processingCharge;
}
