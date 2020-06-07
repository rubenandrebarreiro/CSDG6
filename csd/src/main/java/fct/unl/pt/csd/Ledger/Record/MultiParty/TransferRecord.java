package fct.unl.pt.csd.Ledger.Record.MultiParty;

import java.util.Set;

import fct.unl.pt.csd.Entities.BankEntity;
import fct.unl.pt.csd.Ledger.Record.MultiPartyRecord;
import fct.unl.pt.csd.Ledger.Record.Utils.RecordType;

public class TransferRecord extends MultiPartyRecord {
	
	public TransferRecord(Long id, RecordType recordType, Set<BankEntity> bankEntities) {
		
		super(id, recordType, bankEntities);
	
	}
}
