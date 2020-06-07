package fct.unl.pt.csd.Ledger.Record;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import fct.unl.pt.csd.Common.DataFormatConverter;
import fct.unl.pt.csd.Entities.BankEntity;
import fct.unl.pt.csd.Ledger.Record.Utils.RecordType;

public abstract class Record {

	private Long id;
	
	private Long timestamp;
	
	private Date date;
	
	private RecordType type;
	
	
	public Record(Long id, RecordType type) {
		
		this.id = id;
		
		this.timestamp = System.currentTimeMillis();
		
		this.date = new Date(this.timestamp);
		
		this.type = type;
		
	}
	
	public Long getId() {
		
		return this.id;
		
	}
	
	public Long getTimestamp() {
		
		return this.timestamp;
		
	}
	
	public Date getDate() {
		
		return this.date;
		
	}
	
	public String getDateStringFormat() {
		
		return DataFormatConverter.getDataStringFormat(this.date);
		
	}
	
	public RecordType getType() {
		
		return this.type;
		
	}
	
}
