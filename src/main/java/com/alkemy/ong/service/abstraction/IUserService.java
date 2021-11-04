package com.alkemy.ong.service.abstraction;

import com.alkemy.ong.exception.UserNotFoundException;
import com.alkemy.ong.model.entity.User;

public interface IUserService {
  User getUserById(Long id);
  void deleteUserById(Long id) throws UserNotFoundException;
}
