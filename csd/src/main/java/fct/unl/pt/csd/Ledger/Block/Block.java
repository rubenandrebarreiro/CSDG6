package fct.unl.pt.csd.Ledger.Block;

import java.util.List;

import fct.unl.pt.csd.Ledger.Block.Utils.BlockHeader;
import fct.unl.pt.csd.Ledger.Block.Utils.BlockVersion;
import fct.unl.pt.csd.Ledger.Operations.Operation;

public class Block {

	private BlockHeader blockHeader;
	
	// Merkle Root
	
	private byte[] hashOfPreviousBlock;
	
	private List<Operation> operationsList;
	
	
	public Block(Long id, BlockVersion blockVersion, Long nonce,
				 byte[] hashOfPreviousBlock, List<Operation> operationsList) {
		
		this.blockHeader = new BlockHeader(id, blockVersion, nonce);
		
		this.hashOfPreviousBlock = hashOfPreviousBlock;
		
		this.operationsList = operationsList;
		
	}
	
	
	
}
