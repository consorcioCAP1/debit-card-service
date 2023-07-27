package com.nttdata.bootcamp.debitcardservice.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Builder;
import lombok.Data;

@Document(collection = "debitCard")
@Data
@Builder
public class DebitCard {

	@Id
	private String id;
	private String[] accountNumbers;
	private String primaryAccount;
	private String numberDocument;
	private String numberCard;
}
