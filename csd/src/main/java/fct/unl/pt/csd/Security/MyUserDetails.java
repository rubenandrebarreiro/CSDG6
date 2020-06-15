package fct.unl.pt.csd.Security;

import fct.unl.pt.csd.Entities.BankEntity;
import fct.unl.pt.csd.Repositories.BankRepo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class MyUserDetails implements UserDetails {

    private String userName;
    private String passWord;
    private List<SimpleGrantedAuthority> roles;

    public MyUserDetails(String userName,Optional<BankEntity> e1){
        this.userName = userName;
        this.roles = new ArrayList<>();
        //Optional<BankEntity> e = e1;
        this.passWord = e1.isPresent()?e1.get().getPassword():null;
        for(String s: e1.get().getRoles())
            roles.add(new SimpleGrantedAuthority(s));
}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
//        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
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
