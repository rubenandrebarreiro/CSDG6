package fct.unl.pt.csd.Ledger.Operations.SingleParty;

import fct.unl.pt.csd.Entities.BankEntity;
import fct.unl.pt.csd.Ledger.Operations.Operation;
import fct.unl.pt.csd.Ledger.Record.SingleParty.WithdrawRecord;
import fct.unl.pt.csd.Ledger.Record.Utils.RecordType;

public class DepositOperation extends Operation {

	private Long amount;
	
	private WithdrawRecord record;
	
	public DepositOperation(Long id, Long amount, BankEntity bankEntity) {
		
		super(id);
		
		this.record = new WithdrawRecord(id, RecordType.WITHDRAW, bankEntity);
		
	}
	
	public Long getAmount() {
		
		return this.amount;
		
	}
	
	public WithdrawRecord getRecord() {
		
		return this.record;
		
	}
	
}
