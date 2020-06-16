package smartContracts.verifiers.policy;

import java.security.BasicPermission;
import java.security.PermissionCollection;

public class AuctionCreatorSmartContractPermissionVerifier implements PermissionVerifier {

	private Class<?> auctionCreatorSmartContractClass;
	
	private SecurityManager securityManager;
	
	PermissionCollection permissionCollection;
	
	public AuctionCreatorSmartContractPermissionVerifier(Class<?> auctionCreatorSmartContractClass, PermissionCollection permissionCollection) {
		
		this.auctionCreatorSmartContractClass = auctionCreatorSmartContractClass;
		
		this.permissionCollection = permissionCollection;
		
	}
	
	@Override
	public void verifyPackageAccess() {

		BasicPermission basicPermission;
		
		PermissionCollection permissionCollection;
		
		
		
	}

}
