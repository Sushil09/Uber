package com.sjsushil09.security;

import com.sjsushil09.model.Account;
import com.sjsushil09.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<Account> account=accountRepository.findFirstByUserName(userName);
        if(account.isEmpty()){
            throw new UsernameNotFoundException("No such user "+userName);
        }
        return new CustomUserDetails(account.get());
    }
}
