package com.returnorder.portal.controller;

import javax.annotation.Generated;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

import com.returnorder.portal.client.AuthClient;

import com.returnorder.portal.client.ComponentClient;
import com.returnorder.portal.dto.PaymentStatusDTO;
import com.returnorder.portal.model.ChangePasswordModel;
import com.returnorder.portal.model.FinalPaymentmodel;
import com.returnorder.portal.model.ProcessRequest;
import com.returnorder.portal.model.ProcessResponse;
import com.returnorder.portal.model.TrackRequest;
import com.returnorder.portal.service.ProcessRequestServiceImpl;
import com.returnorder.portal.service.ProcessResponseService;

import java.text.ParseException;
import java.text.SimpleDateFormat;  
import java.util.Date;



@Controller
@Slf4j
public class ProcessController {

	@Autowired
	private ProcessRequestServiceImpl processRequestServiceImplObj;
	@Autowired
	ComponentClient componentClient;
	@Autowired
	ProcessResponse processResponse;
	@Autowired
	AuthClient authClient;
	

	
	PaymentStatusDTO paymentStatusDTO = new PaymentStatusDTO();
	FinalPaymentmodel payment=new FinalPaymentmodel();

	@GetMapping("/order")
	public ModelAndView showProcessing() {
		ModelAndView mv = new ModelAndView("orderDetails");
		mv.addObject("model", new ProcessRequest());
		return mv;
	}

	@PostMapping("/order")
	public ModelAndView performLogin(@Valid @ModelAttribute("model") ProcessRequest model, BindingResult result,
			HttpServletRequest request) throws FeignException{
		ModelAndView mv = new ModelAndView("orderDetails");
		if (result.hasErrors()) {
			return mv;
		}
		try {
			processResponse = processRequestServiceImplObj.processRequestSaveService(model,(String)request.getSession().getAttribute("token"));

			mv.addObject("response", processResponse);
			payment.setContactNumber(model.getContactNumber());
			payment.setCustomername(model.getUserName());
			payment.setDateofDelivery(processResponse.getDateOfDelivery().substring(0, 10));
			payment.setRequestid(processResponse.getRequestId());
			payment.setComponent(model.getComponentName());
			payment.setCost(processResponse.getPackagingAndDeliveryCharge()+processResponse.getProcessingCharge());
			mv.addObject("date",processResponse.getDateOfDelivery().substring(0, 10));
			paymentStatusDTO = processRequestServiceImplObj.statusDetails(model,(String)request.getSession().getAttribute("token"));
			mv.addObject("payment", paymentStatusDTO);
			log.info(model.toString());
      
				mv.setViewName("cart");
				return mv;

		} catch (Exception e) {
			mv.setViewName("cart");
				return mv;			
		}
	}
	
	@GetMapping("/paid")
	public ModelAndView payment()
	{
		ModelAndView mv= new ModelAndView("orderDetails");
		mv.addObject("ContactNumber",payment.getContactNumber());
		mv.addObject("Customername",payment.getCustomername());
		mv.addObject("DateofDelivery",payment.getDateofDelivery());
		mv.addObject("Requestid",payment.getRequestid());
		mv.addObject("Component",payment.getComponent());
		mv.addObject("cost",payment.getCost());
		mv.addObject("paymentmsg","Your Return Order Has Been Succesfully made with an Tracking id of "+payment.getRequestid());
		mv.setViewName("paid");
		return mv;
	}
	
	
	
	@Autowired
	ProcessResponseService processResponseService;
		
	@GetMapping("/orderrequest/*")
	public ModelAndView showStatus(@RequestParam("requestId")String requestId) throws ParseException {
		ModelAndView mv = new ModelAndView("requestStatus");
		ProcessResponse obj = processResponseService.findByRequestId(requestId);
		if(obj!=null) {
			
			mv.addObject("request", "Request Id: " + obj.getRequestId());
			mv.addObject("date", "Date: "+ obj.getDateOfDelivery().substring(0, 10));
			mv.addObject("charge", "Packaging and Delivery Charge: "+ obj.getPackagingAndDeliveryCharge());
			mv.addObject("prCharge","Processing Charge: "+ obj.getProcessingCharge());
			mv.addObject("id",obj.getRequestId());
			return mv;
			
		}
		mv.addObject("errmsg", "Request ID not found");
		return mv;
		
	}
	
	@GetMapping("*/cancelrequest/{requestId}")
	public ModelAndView cancelRequest(@PathVariable("requestId")String requestId) {
		ModelAndView mv = new ModelAndView("cancelOrder");
		ProcessResponse obj = processResponseService.findByRequestId(requestId);
		if(obj!=null) {
			mv.addObject("msg",requestId+" has been sucessfully deleted.");
			mv.addObject("price",(obj.getPackagingAndDeliveryCharge()+obj.getProcessingCharge())+"$ Will be Reverted back to your accounts shortly");
			processResponseService.delete(obj);
			return mv;
			
		}
		mv.addObject("msg", "Request ID not found");
		return mv;
		
	}
	@GetMapping("/trackrequest")
	public ModelAndView trackrequest()
	{
		ModelAndView mv = new ModelAndView("trackRequest");
		mv.addObject("trackrequest", new TrackRequest());
		log.info("Request Tracked");			
		log.info("payment Reverted to main user");

		return mv;
	}
	@GetMapping("/passwordchange")
	public ModelAndView changepassword()
	{
		ModelAndView mv = new ModelAndView("passwordchange");
		mv.addObject("passchange", new ChangePasswordModel());
		mv.setViewName("passchange");
		return mv;
	}
	
@PostMapping("/passwordchange")
public ModelAndView passchange(@ModelAttribute("passchange") ChangePasswordModel pass,BindingResult result,
		HttpServletRequest request) throws FeignException
{
	ModelAndView mv=new ModelAndView("passwordchange");
	//System.out.println(pass.getUsername()+pass.getNewpassword()+pass.getOldpassword());
try {	authClient.changePassword(pass);
}
catch (Exception e) {
	mv.addObject("message","your password has not been succesfully updated");
	mv.setViewName("passchange2");
	return mv;
}
	mv.addObject("message","your password has been succesfully updated");
	mv.setViewName("passchange2");
	return mv;
	
}




}
