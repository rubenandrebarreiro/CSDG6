package smartContracts;

import java.security.BasicPermission;

public class CreateAuctionSmartContractPermission extends BasicPermission {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CreateAuctionSmartContractPermission(String name) {
		super(name);
    }

    // note that actions is ignored and not used,
    // but this constructor is still needed
    public CreateAuctionSmartContractPermission(String name, String actions) {
    	super(name, actions);
    }

}
