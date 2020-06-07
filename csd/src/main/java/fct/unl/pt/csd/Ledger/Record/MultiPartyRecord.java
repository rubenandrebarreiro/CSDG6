package fct.unl.pt.csd.Ledger.Record;

import java.util.Set;

import fct.unl.pt.csd.Entities.BankEntity;
import fct.unl.pt.csd.Ledger.Record.Utils.RecordType;

public abstract class MultiPartyRecord extends Record {

	private Set<BankEntity> bankEntities;
	
	
	public MultiPartyRecord(Long id, RecordType recordType, Set<BankEntity> bankEntities) {
		
		super(id, recordType);
		
		this.bankEntities = bankEntities;
		
	}
	
	public Set<BankEntity> getBankEntities() {
		
		return this.bankEntities;
		
	}
	
}
