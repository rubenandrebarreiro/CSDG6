package fct.unl.pt.csd.Ledger.Operations;

public abstract class Operation {

	private Long id;
	
	public Operation(Long id) {
		
		this.id = id;
		
	}
	
	
	public Long getId() {
		
		return this.id;
		
	}
	
}
