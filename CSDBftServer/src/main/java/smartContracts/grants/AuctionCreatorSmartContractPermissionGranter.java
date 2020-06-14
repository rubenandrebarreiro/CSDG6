package smartContracts.grants;

import java.io.FilePermission;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;

public class AuctionCreatorSmartContractPermissionGranter implements PermissionGranter {
	
	
	private PermissionCollection auctionCreatorSmartContractPermissions;
	
	
	public AuctionCreatorSmartContractPermissionGranter() {
		
		this.auctionCreatorSmartContractPermissions = new Permissions();
		
	}
	
	
	@Override
	public void grantPermissions() {

		this.grantFilePermissions();
		
	}

	@Override
	public PermissionCollection getPermissions() {
		
		return this.auctionCreatorSmartContractPermissions;

	}

	private void grantFilePermissions() {
		
		//Permission filePermissionToWriteFile = new FilePermission(path, actions);
		//this.auctionCreatorSmartContractPermissions.add(filePermissionToWriteFile);
		
		Permission filePermissionToWriteReportFile = new FilePermission("" /* TODO */, "write");
		this.auctionCreatorSmartContractPermissions.add(filePermissionToWriteReportFile);


		
		
	}
	
}
