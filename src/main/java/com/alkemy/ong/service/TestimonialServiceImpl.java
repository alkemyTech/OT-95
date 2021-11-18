package com.alkemy.ong.service;

import com.alkemy.ong.model.entity.Testimonial;
import com.alkemy.ong.model.request.CreateTestimonialRequest;
import com.alkemy.ong.repository.ITestimonialRepository;
import com.alkemy.ong.service.abstraction.ICreateTestimonialService;
import com.alkemy.ong.service.abstraction.IDeleteTestimonialService;
import com.alkemy.ong.service.abstraction.IListTestimonialsService;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestimonialServiceImpl implements ICreateTestimonialService, IListTestimonialsService,
    IDeleteTestimonialService {

  @Autowired
  private ITestimonialRepository testimonialRepository;

  @Transactional
  @Override
  public Testimonial create(CreateTestimonialRequest createTestimonialRequest) {
    Testimonial testimonial = new Testimonial();
    testimonial.setName(createTestimonialRequest.getName());
    testimonial.setImage(createTestimonialRequest.getImage());
    testimonial.setContent(createTestimonialRequest.getContent());
    testimonial.setSoftDelete(false);
    return testimonialRepository.save(testimonial);

  }

  @Override
  public Page<Testimonial> list(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return testimonialRepository.findBySoftDeleteIsFalse(pageable);
  }

  @Override
  public void deleteBy(Long id) throws EntityNotFoundException {
    Optional<Testimonial> testimonial = testimonialRepository.findById(id);
    if (testimonial.isEmpty() || testimonial.get().isSoftDelete()) {
      throw new EntityNotFoundException("Testimonial not found.");
    }
    testimonial.get().setSoftDelete(true);
    testimonialRepository.save(testimonial.get());
  }

}
