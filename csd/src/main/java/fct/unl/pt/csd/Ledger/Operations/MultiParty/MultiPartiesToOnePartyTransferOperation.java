package fct.unl.pt.csd.Ledger.Operations.MultiParty;

import java.util.HashSet;
import java.util.Set;

import fct.unl.pt.csd.Entities.BankEntity;
import fct.unl.pt.csd.Ledger.Operations.Operation;
import fct.unl.pt.csd.Ledger.Record.MultiParty.TransferRecord;
import fct.unl.pt.csd.Ledger.Record.Utils.RecordType;

public class MultiPartiesToOnePartyTransferOperation extends Operation {
	
	private Long amountToEachBankEntity;
	
	private BankEntity[] fromBankEntities; 
	
	private BankEntity toBankEntity;
	
	private TransferRecord record;
	
	
	
	public MultiPartiesToOnePartyTransferOperation(Long id, Long amountToEachBankEntity,
									   			   BankEntity toBankEntity, BankEntity ... fromBankEntities) {
		
		super(id);
		
		this.amountToEachBankEntity = amountToEachBankEntity;
		
		this.fromBankEntities = fromBankEntities;
		this.toBankEntity = toBankEntity;
		
		Set<BankEntity> bankEntities = new HashSet<>();
		
		assert(bankEntities.size() == 0);
		
		for(BankEntity fromBankEntity : fromBankEntities) {
			
			bankEntities.add(fromBankEntity);
			
		}
		
		bankEntities.add(toBankEntity);
		
		assert(bankEntities.size() == ( fromBankEntities.length + 1 ));
		
		this.record = new TransferRecord(id, RecordType.TRANSFER_MULTI_ONE, bankEntities);
		
	}
	
	public Long getAmountToEachBankEntity() {
		
		return this.amountToEachBankEntity;
		
	}
	
	public BankEntity[] getFromBankEntities() {
		
		return this.fromBankEntities;
		
	}

	public BankEntity getToBankEntity() {
		
		return this.toBankEntity;
		
	}
	
	public TransferRecord getRecord() {
		
		return this.record;
		
	}
	
}
