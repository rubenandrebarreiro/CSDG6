package fct.unl.pt.csd.Ledger.Operations.MultiParty;

import java.util.HashSet;
import java.util.Set;

import fct.unl.pt.csd.Entities.BankEntity;
import fct.unl.pt.csd.Ledger.Operations.Operation;
import fct.unl.pt.csd.Ledger.Record.MultiParty.TransferRecord;
import fct.unl.pt.csd.Ledger.Record.Utils.RecordType;

public class OnePartyToOnePartyTransferOperation extends Operation {
	
	private Long amount;
	
	private BankEntity fromBankEntity; 
	
	private BankEntity toBankEntity;
	
	private TransferRecord record;
	
	
	
	public OnePartyToOnePartyTransferOperation(Long id, Long amount,
   							   			       BankEntity fromBankEntity, BankEntity toBankEntity) {
		
		super(id);
		
		this.amount = amount;
		
		this.fromBankEntity = fromBankEntity;
		this.fromBankEntity = fromBankEntity;
		
		Set<BankEntity> bankEntities = new HashSet<>();
		
		assert(bankEntities.size() == 0);
		
		bankEntities.add(fromBankEntity);
		
		bankEntities.add(toBankEntity);
		
		assert(bankEntities.size() == 2);
		
		this.record = new TransferRecord(id, RecordType.TRANSFER_ONE_ONE, bankEntities);
		
	}
	
	public Long getAmount() {
		
		return this.amount;
		
	}
	
	public BankEntity getFromBankEntity() {
		
		return this.fromBankEntity;
		
	}

	public BankEntity getToBankEntity() {
		
		return this.toBankEntity;
		
	}
	
	public TransferRecord getRecord() {
		
		return this.record;
		
	}
	
}
