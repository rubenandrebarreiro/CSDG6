package fct.unl.pt.csd.Ledger.Record.Utils;

public enum RecordType {
	
	INITIAL_DEPOSIT ( (byte) 1, RecordTypeName.INITIAL_DEPOSIT_TYPE_NAME,
								RecordTypeCardinality.INITIAL_DEPOSIT_TYPE_CARDINALITY),
	DEPOSIT ( (byte) 2, RecordTypeName.DEPOSIT_TYPE_NAME,
							    RecordTypeCardinality.DEPOSIT_TYPE_CARDINALITY),
	WITHDRAW ( (byte) 3, RecordTypeName.WITHDRAW_TYPE_NAME,
		    					RecordTypeCardinality.WITHDRAW_TYPE_CARDINALITY),
	TRANSFER_ONE_ONE ( (byte) 4, RecordTypeName.TRANSFER_TYPE_NAME,
								RecordTypeCardinality.TRANSFER_TYPE_CARDINALITY),
	TRANSFER_ONE_MULTI ( (byte) 5, RecordTypeName.TRANSFER_TYPE_NAME,
								RecordTypeCardinality.TRANSFER_TYPE_CARDINALITY),
	TRANSFER_MULTI_ONE ( (byte) 6, RecordTypeName.TRANSFER_TYPE_NAME,
								RecordTypeCardinality.TRANSFER_TYPE_CARDINALITY),
	BID ( (byte) 7, RecordTypeName.BID_TYPE_NAME,
								RecordTypeCardinality.BID_TYPE_CARDINALITY);

	
	private byte id;
	
	private String name;
	
	private String cardinality;
	
	
	private RecordType(byte id, String name, String cardinality) {
		
		this.id = id;
		
		this.name = name;
		
		this.cardinality = cardinality;
		
	}
	
	
	public byte getID() {
		
		return this.id;
		
	}
	
	public String getName() {
		
		return this.name;
		
	}
	
	public String getCardinality() {
		
		return this.cardinality;
		
	}
	
}
