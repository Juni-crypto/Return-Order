package com.payment.microservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.payment.microservice.model.PaymentDetails;

@Repository
public interface PaymentDetailsRepo extends JpaRepository<PaymentDetails, String> {

	PaymentDetails findByCardNumber(Integer creditnumber);

}
