package smartContracts.grants;

import java.security.PermissionCollection;

public interface PermissionGranter {
	
	void grantPermissions();
	
	PermissionCollection getPermissions();
	
}
