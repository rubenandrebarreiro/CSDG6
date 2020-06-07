package fct.unl.pt.csd.Ledger.Block.Utils;

public class BlockHeader {

	private Long id;
	
	private BlockVersion blockVersion;
	
	private Long nonce;
	
	
	public BlockHeader(Long id, BlockVersion blockVersion, Long nonce) {
		
		this.id = id;
		
		this.blockVersion = blockVersion;
		
		this.nonce = nonce;
		
	}
	
	public Long getId() {
		
		return this.id;
		
	}
	
	public BlockVersion getBlockVersion() {
		
		return this.blockVersion;
		
	}
	
	public Long getNonce() {
		
		return this.nonce;
		
	}
	
}
