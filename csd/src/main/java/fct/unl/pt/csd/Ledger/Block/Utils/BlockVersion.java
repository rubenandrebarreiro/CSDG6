package fct.unl.pt.csd.Ledger.Block.Utils;

public enum BlockVersion {
	
	V0_1 ( (byte) 1),
	V0_2 ( (byte) 2),
	V0_3 ( (byte) 3),
	V0_4 ( (byte) 4);
	
	private byte versionId;
	
	private BlockVersion(byte versionId) {
		
		this.versionId = versionId;
		
	}
	
	public byte getVersionId() {
		
		return this.versionId;
		
	}
	
}
