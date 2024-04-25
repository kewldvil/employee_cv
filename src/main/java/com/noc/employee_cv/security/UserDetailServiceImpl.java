package com.noc.employee_cv.security;

import com.noc.employee_cv.repositories.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepo userRepo;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepo.findByEmail(userEmail).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
        return userRepo.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
    }

}
