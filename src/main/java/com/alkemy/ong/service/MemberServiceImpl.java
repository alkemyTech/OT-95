package com.alkemy.ong.service;

import com.alkemy.ong.common.converter.ConvertUtils;
import com.alkemy.ong.model.entity.Member;
import com.alkemy.ong.model.entity.News;
import com.alkemy.ong.model.request.CreateCommentRequest;
import com.alkemy.ong.model.request.DetailsMemberRequest;
import com.alkemy.ong.model.response.DetailsMemberResponse;
import com.alkemy.ong.model.response.ListMemberResponse;
import com.alkemy.ong.repository.IMemberRepository;
import com.alkemy.ong.service.abstraction.ICreateMemberService;
import com.alkemy.ong.service.abstraction.IListMembersService;
import java.util.List;
import java.util.Optional;
import com.alkemy.ong.service.abstraction.IDeleteMembersService;
import com.alkemy.ong.service.abstraction.IListMembersService;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl
    implements IListMembersService, ICreateMemberService, IDeleteMembersService {


  @Autowired
  IMemberRepository memberRepository;

  @Autowired
  ConvertUtils convertUtils;

  @Override
  public ListMemberResponse list() {
    List<Member> members = memberRepository.findBySoftDeleteFalse();
    List<DetailsMemberResponse> detailsMemberResponses = convertUtils.toResponseList(members);
    return new ListMemberResponse(detailsMemberResponses);
  }

  @Override
  public Member create(DetailsMemberRequest detailsMemberRequest) {
    Member member = new Member();
    member.setName(detailsMemberRequest.getName());
    member.setImage(detailsMemberRequest.getImage());
    member.setDescription(detailsMemberRequest.getDescription());
    member.setSoftDelete(false);
    member.setFacebookUrl(detailsMemberRequest.getFacebookUrl());
    member.setLinkedinUrl(detailsMemberRequest.getLinkedinUrl());
    member.setInstagramUrl(detailsMemberRequest.getInstagramUrl());

    return memberRepository.save(member);
  }
  @Override
  public void deleteBy(long id) throws EntityNotFoundException {
    Member member = memberRepository.getById(id);
    validateMember(member);
    member.setSoftDelete(true);
    memberRepository.save(member);
  }

  private void validateMember(Member member) {
    if (member == null) {
      throw new EntityNotFoundException("The requested resource could not be found.");
    }

  }

}
