package fct.unl.pt.csd.Ledger.Operations.MultiParty;

import java.util.HashSet;
import java.util.Set;

import fct.unl.pt.csd.Entities.BankEntity;
import fct.unl.pt.csd.Ledger.Operations.Operation;
import fct.unl.pt.csd.Ledger.Record.MultiParty.TransferRecord;
import fct.unl.pt.csd.Ledger.Record.Utils.RecordType;

public class OnePartyToMultiPartiesTransferOperation extends Operation {
	
	private Long amountFromEachBankEntity;
	
	private BankEntity fromBankEntity; 
	
	private BankEntity[] toBankEntities;
	
	private TransferRecord record;
	
	
	
	public OnePartyToMultiPartiesTransferOperation(Long id, Long amountFromEachBankEntity,
									   			   BankEntity fromBankEntity, BankEntity ... toBankEntities) {
		
		super(id);
		
		this.amountFromEachBankEntity = amountFromEachBankEntity;
		
		this.fromBankEntity = fromBankEntity;
		this.toBankEntities = toBankEntities;
		
		Set<BankEntity> bankEntities = new HashSet<>();
		
		assert(bankEntities.size() == 0);
		
		bankEntities.add(fromBankEntity);
		
		for(BankEntity toBankEntity : toBankEntities) {
			
			bankEntities.add(toBankEntity);
			
		}
		
		assert(bankEntities.size() == ( toBankEntities.length + 1 ));
		
		this.record = new TransferRecord(id, RecordType.TRANSFER_ONE_MULTI, bankEntities);
		
	}
	
	public Long getAmountFromEachBankEntity() {
		
		return this.amountFromEachBankEntity;
		
	}
	
	public BankEntity getFromBankEntity() {
		
		return this.fromBankEntity;
		
	}

	public BankEntity[] getToBankEntities() {
		
		return this.toBankEntities;
		
	}
	
	public TransferRecord getRecord() {
		
		return this.record;
		
	}
	
}
