package com.sudoku.sudokusolver.repos;

import org.springframework.data.repository.CrudRepository;
import com.sudoku.sudokusolver.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
}
