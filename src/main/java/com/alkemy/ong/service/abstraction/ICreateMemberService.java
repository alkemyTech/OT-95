package com.alkemy.ong.service.abstraction;

import com.alkemy.ong.model.entity.Member;
import com.alkemy.ong.model.request.DetailsMemberRequest;

public interface ICreateMemberService {

  Member create(DetailsMemberRequest createMemberRequest);

}
