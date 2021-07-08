package com.returnorder.portal.controller;

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
import com.returnorder.portal.model.ProcessRequest;
import com.returnorder.portal.model.ProcessResponse;
import com.returnorder.portal.model.TrackRequest;
import com.returnorder.portal.service.ProcessRequestServiceImpl;
import com.returnorder.portal.service.ProcessResponseService;



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
	
	@Autowired
	ProcessResponseService processResponseService;
		
	@GetMapping("/orderrequest/*")
	public ModelAndView showStatus(@RequestParam("requestId")String requestId) {
		ModelAndView mv = new ModelAndView("requestStatus");
		ProcessResponse obj = processResponseService.findByRequestId(requestId);
		if(obj!=null) {
			mv.addObject("request", "Request Id:" + obj.getRequestId());
			mv.addObject("date", "Date:"+ obj.getDateOfDelivery());
			mv.addObject("charge", "Packaging and Delivery Charge:"+ obj.getPackagingAndDeliveryCharge());
			mv.addObject("prCharge","Processing Charge:"+ obj.getProcessingCharge());
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
	





}
