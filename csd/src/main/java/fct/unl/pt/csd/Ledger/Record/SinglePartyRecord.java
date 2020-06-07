package fct.unl.pt.csd.Ledger.Record;

import fct.unl.pt.csd.Entities.BankEntity;
import fct.unl.pt.csd.Ledger.Record.Utils.RecordType;

public abstract class SinglePartyRecord extends Record {

	private BankEntity bankEntity;
	
	
	public SinglePartyRecord(Long id, RecordType recordType, BankEntity bankEntity) {
		
		super(id, recordType);
		
		this.bankEntity = bankEntity;
		
	}
	
	public BankEntity getBankEntity() {
		
		return this.bankEntity;
		
	}
	
}
