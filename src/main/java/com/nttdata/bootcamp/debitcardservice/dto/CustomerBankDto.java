package com.nttdata.bootcamp.debitcardservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerBankDto {

	private String id;
	private String typeCustomer;
	private String clientId;
	private String clientName;
	private String numberDocument;
	private String openingDate;
	private Integer bankMovementLimit;
	private String accountType;
	private Double accountBalance;
	private String bankAccountNumber;
	private Double minimumMonthlyAmount;
}


