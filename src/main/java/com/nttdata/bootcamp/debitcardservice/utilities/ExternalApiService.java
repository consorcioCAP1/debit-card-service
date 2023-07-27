package com.nttdata.bootcamp.debitcardservice.utilities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.nttdata.bootcamp.debitcardservice.dto.CustomerBankDto;
import reactor.core.publisher.Flux;

@Component
public class ExternalApiService {

   @Value("${customer-bank-account.api.url}")
   private String customerBankAccountBankUrl;

   //metodo para obtener document creditBankAccount
   public Flux<CustomerBankDto> getBankAccountByNumberDocument(String numberDocument) {
		WebClient webClient = WebClient.create(customerBankAccountBankUrl); 
		 return webClient.get()
	                .uri("/findByNumberDocument/{numberDocument}", numberDocument)
	                .retrieve()
	                .bodyToFlux(CustomerBankDto.class);
	}
}
