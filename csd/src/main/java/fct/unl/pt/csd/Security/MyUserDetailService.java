package fct.unl.pt.csd.Security;

import fct.unl.pt.csd.Repositories.BankRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {
    private final BankRepo bankRepo;

    @Autowired
    public MyUserDetailService(final BankRepo bankRepo) {
        this.bankRepo = bankRepo;
    }
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return new MyUserDetails(s,bankRepo);
    }
}
