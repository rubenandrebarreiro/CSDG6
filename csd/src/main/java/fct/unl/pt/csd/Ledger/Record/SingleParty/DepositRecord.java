package fct.unl.pt.csd.Ledger.Record.SingleParty;

import fct.unl.pt.csd.Entities.BankEntity;
import fct.unl.pt.csd.Ledger.Record.SinglePartyRecord;
import fct.unl.pt.csd.Ledger.Record.Utils.RecordType;

public class DepositRecord extends SinglePartyRecord {
	
	public DepositRecord(Long id, RecordType recordType, BankEntity bankEntity) {
		
		super(id, recordType, bankEntity);
	
	}
	
}
