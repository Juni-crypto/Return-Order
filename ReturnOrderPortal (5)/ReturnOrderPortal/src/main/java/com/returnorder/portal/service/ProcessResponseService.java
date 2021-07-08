package com.returnorder.portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.returnorder.portal.model.ProcessResponse;
import com.returnorder.portal.repo.ProcessResponseRepo;

@Service
public class ProcessResponseService {

	@Autowired
	ProcessResponseRepo processResponseRepo;
	
	public ProcessResponse findByRequestId(String requestId) {
		return processResponseRepo.findByRequestId(requestId);
		}

	public void delete(ProcessResponse obj) {
		processResponseRepo.delete(obj);
		
	}

	
	
}
