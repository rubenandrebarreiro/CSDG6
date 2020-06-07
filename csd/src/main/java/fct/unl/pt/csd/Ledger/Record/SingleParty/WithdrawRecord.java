package fct.unl.pt.csd.Ledger.Record.SingleParty;

import fct.unl.pt.csd.Entities.BankEntity;
import fct.unl.pt.csd.Ledger.Record.SinglePartyRecord;
import fct.unl.pt.csd.Ledger.Record.Utils.RecordType;

public class WithdrawRecord extends SinglePartyRecord {
	
	public WithdrawRecord(Long id, RecordType recordType, BankEntity bankEntity) {
		
		super(id, recordType, bankEntity);
	
	}
	
}
