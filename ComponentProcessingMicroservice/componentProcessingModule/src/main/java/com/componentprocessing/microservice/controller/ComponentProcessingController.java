package com.componentprocessing.microservice.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.componentprocessing.microservice.client.AuthClient;
import com.componentprocessing.microservice.dto.PaymentStatusDTO;
import com.componentprocessing.microservice.exceptions.ComponentTyepNotFoundException;
import com.componentprocessing.microservice.exceptions.InvalidTokenException;
import com.componentprocessing.microservice.model.ProcessRequest;
import com.componentprocessing.microservice.model.ProcessResponse;
import com.componentprocessing.microservice.service.ProcessResponseService;
import com.componentprocessing.microservice.service.RepairServiceImpl;
import com.componentprocessing.microservice.service.ReplacementServiceImpl;

import feign.FeignException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/componentprocessingmodule")
@Api(value = "Component Processing Resource endpoint")
public class ComponentProcessingController {

	@Autowired
	private RepairServiceImpl repairServiceImplObj;
	@Autowired
	private ReplacementServiceImpl replacementServiceImplObj;
	@Autowired
	private ProcessResponse processResponseObj;
	@Autowired
	private AuthClient authClient;

	@PostMapping(path = "/ProcessDetail", produces = MediaType.APPLICATION_JSON_VALUE)

	@ApiOperation(value = "to send the processRequestObj to the user", response = ProcessRequest.class, httpMethod = "POST")
	public ResponseEntity<ProcessResponse> processResponseDetails(@RequestBody ProcessRequest processRequestObj,
			@RequestHeader(name = "Authorization", required = true) String token) throws InvalidTokenException {

		log.info(token);
		try {
			if (!authClient.getsValidity(token).isValidStatus()) {

				throw new InvalidTokenException("Token is either expired or invalid...");
			}

		} catch (FeignException e) {
			throw new InvalidTokenException("Token is either expired or invalid...");

		}

		log.info("Checking if component is Integral or Accessory - BEGINS");
		if (("Integral").equalsIgnoreCase(processRequestObj.getComponentType())) {
			log.info("Retrieving the ProcessResponse object for Integral - BEGINS");

			processResponseObj = repairServiceImplObj.processService(processRequestObj, token);

			log.info("Retrieving the ProcessResponse object for Integral - ENDS");
		} else if (("Accessory").equalsIgnoreCase(processRequestObj.getComponentType())) {
			log.info("Retrieving the ProcessResponse object for Accessory - BEGINS");

			processResponseObj = replacementServiceImplObj.processService(processRequestObj, token);

			log.info("Retrieving the ProcessResponse object for Accessory - ENDS");
		} else {
			throw new ComponentTyepNotFoundException("Wrong Component Type");
		}

		log.info("Checking if component is Integral or Accessory - ENDS");

		return new ResponseEntity<>(processResponseObj, HttpStatus.OK);

	}

	@PostMapping(path = "/completeProcessing/{requestId}/{creditCardNumber}/{creditLimit}/{processingCharge}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PaymentStatusDTO> messageConfirmation(@PathVariable String requestId,
			@PathVariable Integer creditCardNumber, @PathVariable Integer creditLimit,
			@PathVariable Integer processingCharge,
			@RequestHeader(name = "Authorization", required = true) String token) throws InvalidTokenException {

		log.info(token);
		try {
			if (!authClient.getsValidity(token).isValidStatus()) {

				throw new InvalidTokenException("Token is either expired or invalid...");
			}

		} catch (FeignException e) {
			throw new InvalidTokenException("Token is either expired or invalid...");

		}
		log.info("Controller Component");
		try {
			return new ResponseEntity<>(new PaymentStatusDTO(replacementServiceImplObj.messageConfirmation(requestId,
					creditCardNumber, creditLimit, processingCharge, token)), HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(new PaymentStatusDTO(replacementServiceImplObj.messageConfirmation(requestId,
					creditCardNumber, creditLimit, processingCharge, token)), HttpStatus.FORBIDDEN);
		}

	}

	@GetMapping(path = "/health-check")
	public ResponseEntity<String> healthCheck() {
		log.info("ComponentProcessing Microservice is Up and Running....");
		return new ResponseEntity<>("OK", HttpStatus.OK);
	}

	@Autowired
	ProcessResponseService processResponseService;

	@GetMapping("/TrackRequest/{requestId}")
	public ResponseEntity<String> trackRequest(@PathVariable String requestId) {

		ProcessResponse obj = processResponseService.loadByRequestId(requestId);
		if (obj != null && requestId.equals(obj.getRequestId())) {
			return new ResponseEntity<>(obj.getDateOfDelivery(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Request ID not found", HttpStatus.BAD_REQUEST);
		}
	}
/*
	@PostMapping("/CancelRequest/{requestId}/{creditCardNumber}")
	public ResponseEntity<String> cancelRequest(@PathVariable String requestId,
			@PathVariable Integer creditCardNumber) {
		ProcessResponse obj = processResponseService.loadByRequestId(requestId);

		if (obj != null && requestId.equals(obj.getRequestId())) {
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			try {

				if (Math.abs(df.parse(obj.getDateOfDelivery()).getTime()
						- df.parse(df.format(new Date())).getTime()) > 1000 * 24 * 60 * 60) {
					processResponseService.deleteRequest(obj);
					return new ResponseEntity<>(requestId + " deleted", HttpStatus.OK);

				} else {
					return new ResponseEntity<>(requestId + " cannot be deleted as the date of delivery is in 1 day.",
							HttpStatus.FORBIDDEN);

				}
			} catch (ParseException e) {

			}
		}
		return new ResponseEntity<>("Request ID not found", HttpStatus.BAD_REQUEST);
	}
	*/
	@PostMapping("/cancelRequest/{requestId}")
	public ResponseEntity<String> cancelRequest(@PathVariable String requestId
			) throws InvalidTokenException  {
	
		ProcessResponse processResponse = processResponseService.loadByRequestId(requestId);
		
		if (processResponse != null && requestId.equals(processResponse.getRequestId())) {
				if (isValidToCancelRequest(processResponse.getDateOfDelivery())) {
					processResponseService.deleteRequest(processResponse);
					return new ResponseEntity<>(processResponseService.messageConfirmation(requestId),
							HttpStatus.OK);
				} else {
					return new ResponseEntity<>(requestId + " cannot be deleted as the date of delivery is in 1 day.",
							HttpStatus.FORBIDDEN);
				}
		}
		
		return new ResponseEntity<>("Request ID not found", HttpStatus.BAD_REQUEST);
	}

	/**
	 * Requests cannot be cancelled 1 day prior to delivery date.
	 * @param dateOfDelivery
	 * @return boolean
	 */
	public boolean isValidToCancelRequest(String dateOfDelivery) {
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		try {
			return (Math.abs(df.parse(dateOfDelivery).getTime() - df.parse(df.format(new Date())).getTime()) > 1000 * 24
					* 60 * 60);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	} 
	
}
