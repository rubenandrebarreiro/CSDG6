package fct.unl.pt.csdw1.Security;

import fct.unl.pt.csdw1.Entities.BankEntity;
import fct.unl.pt.csdw1.Repositories.BankRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class MyUserDetails implements UserDetails {

    private String userName;
    private String passWord;

    public MyUserDetails(String userName,final BankRepo bankRepo){
        this.userName = userName;
        Optional<BankEntity> e = bankRepo.findByUserName(userName);
        this.passWord = e.isPresent()?e.get().getPassword():null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.passWord;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
