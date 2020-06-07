package fct.unl.pt.csd.Ledger.Operations.SingleParty;

import fct.unl.pt.csd.Entities.BankEntity;
import fct.unl.pt.csd.Ledger.Operations.Operation;
import fct.unl.pt.csd.Ledger.Record.SingleParty.InitialDepositRecord;
import fct.unl.pt.csd.Ledger.Record.Utils.RecordType;

public class InitialDepositOperation extends Operation {

	private Long amount;
	
	private InitialDepositRecord record;
	
	public InitialDepositOperation(Long id, Long amount, BankEntity bankEntity) {
		
		super(id);
		
		this.record = new InitialDepositRecord(id, RecordType.INITIAL_DEPOSIT, bankEntity);
		
	}
	
	public Long getAmount() {
		
		return this.amount;
		
	}
	
	public InitialDepositRecord getRecord() {
		
		return this.record;
		
	}
	
}
