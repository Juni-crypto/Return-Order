package com.returnorder.portal.model;



import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
@Component
@ToString
public class FinalPaymentmodel {
	
	private String Customername;
	private String DateofDelivery;
	private String Requestid;
	private Long ContactNumber;
	private String Component;
	private Double cost;
	
	

}
