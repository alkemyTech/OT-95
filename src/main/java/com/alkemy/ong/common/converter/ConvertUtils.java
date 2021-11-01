package com.alkemy.ong.common.converter;

import com.alkemy.ong.model.entity.Category;
import com.alkemy.ong.model.entity.User;
import com.alkemy.ong.model.response.CreateCategoryResponse;
import com.alkemy.ong.model.response.UserRegisterResponse;
import org.springframework.stereotype.Component;

@Component("convertUtils")
public class ConvertUtils {

  private ConvertUtils() {}

  public static UserRegisterResponse toResponse(User user, String jwt) {
    UserRegisterResponse userRegisterResponse = new UserRegisterResponse();
    userRegisterResponse.setId(user.getId());
    userRegisterResponse.setFirstsName(user.getFirstName());
    userRegisterResponse.setLastName(user.getLastName());
    userRegisterResponse.setEmail(user.getEmail());
    userRegisterResponse.setJwt(jwt);
    return userRegisterResponse;
  }

  public CreateCategoryResponse toResponse(Category category) {
    CreateCategoryResponse createCategoryResponse = new CreateCategoryResponse();
    createCategoryResponse.setId(category.getId());
    createCategoryResponse.setName(category.getName());
    return createCategoryResponse;
  }

}
