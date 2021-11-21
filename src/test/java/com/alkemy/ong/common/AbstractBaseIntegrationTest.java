package com.alkemy.ong.common;

import com.alkemy.ong.model.entity.Role;
import com.alkemy.ong.model.entity.User;
import com.alkemy.ong.repository.IUserRepository;
import com.alkemy.ong.service.abstraction.IRoleService;
import org.assertj.core.util.Lists;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;

public class AbstractBaseIntegrationTest {

  protected TestRestTemplate restTemplate = new TestRestTemplate();
  protected HttpHeaders headers = new HttpHeaders();

  @MockBean
  protected IUserRepository userRepository;

  @MockBean
  protected AuthenticationManager authenticationManager;

  @MockBean
  protected IRoleService roleService;

  @LocalServerPort
  private int port;

  protected String createURLWithPort(String uri) {
    return "http://localhost:" + port + uri;
  }

  protected void login(String role) {
    String jwt = SecurityTestConfig.createToken("johnny@gmail.com", role);
    headers.set("Authorization", jwt);
  }

  protected Role stubRole(String name) {
    Role role = new Role();
    role.setName(name);
    return role;
  }

  protected User stubUser(String role) {
    return new User(1L,
        "John",
        "Doe",
        "johnny@gmail.com",
        "123456789",
        "https://foo.jpg",
        Lists.list(stubRole(role)),
        null,
        false);
  }
  
  protected String extractURIByRel(String linkHeader, String rel) {
    String uriWithSpecifiedRel = null;
    String[] links = linkHeader.split(", ");
    String linkRelation;
    for (String link : links) {
      int positionOfSeparator = link.indexOf(';');
      linkRelation = link.substring(positionOfSeparator + 1, link.length()).trim();
      if (extractTypeOfRelation(linkRelation).equals(rel)) {
        uriWithSpecifiedRel = link.substring(1, positionOfSeparator - 1);
        break;
      }
    }

    return uriWithSpecifiedRel;
  }

  private String extractTypeOfRelation(final String linkRelation) {
    final int positionOfEquals = linkRelation.indexOf('=');
    return linkRelation.substring(positionOfEquals + 2, linkRelation.length() - 1).trim();
  }
}
