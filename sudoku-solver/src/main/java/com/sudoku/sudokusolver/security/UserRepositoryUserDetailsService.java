package com.sudoku.sudokusolver.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.
        UserDetailsService;
import org.springframework.security.core.userdetails.
        UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.sudoku.sudokusolver.model.User;
import com.sudoku.sudokusolver.repos.UserRepository;

/** The point of this class is to either return a UserDetails object or
 *  throw a UsernameNotFoundException if the given username doesn't exist
 */
@Service
public class UserRepositoryUserDetailsService
        implements UserDetailsService {

    //Use UserRepository to find user
    private UserRepository userRepo;

    //Constructor is injected with an instance of UserRepository
    @Autowired
    public UserRepositoryUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    //Find the user and return it or throw exception if not found
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user != null) {
            return user;
        }
        throw new UsernameNotFoundException(
                "User '" + username + "' not found");
    }

}
