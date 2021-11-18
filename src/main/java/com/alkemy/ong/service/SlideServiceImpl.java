package com.alkemy.ong.service;

import com.alkemy.ong.common.s3.S3ObjectHelper;
import com.alkemy.ong.exception.ExternalServiceException;
import com.alkemy.ong.model.entity.Slide;
import com.alkemy.ong.model.request.CreateSlideRequest;
import com.alkemy.ong.repository.ISlideRepository;
import com.alkemy.ong.service.abstraction.ICreateSlideService;
import com.alkemy.ong.service.abstraction.IDeleteSlideService;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import javax.persistence.EntityNotFoundException;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SlideServiceImpl implements IDeleteSlideService, ICreateSlideService {

  @Autowired
  private ISlideRepository slideRepository;

  @Transactional
  @Override
  public void delete(long id) throws EntityNotFoundException {
    if (!slideRepository.existsById(id)) {
      throw new EntityNotFoundException("Slide not found!");
    }
    slideRepository.deleteById(id);
  }

  @Transactional
  @Override
  public Slide create(ContentType contentType, String fileName, CreateSlideRequest createSlideRequest)
      throws EntityNotFoundException, ExternalServiceException, NullPointerException {

    if (createSlideRequest.getImage() == null) throw new NullPointerException();

    byte[] decodedBytes = Base64.getDecoder().decode(createSlideRequest.getImage());
    InputStream inputStream = new ByteArrayInputStream(decodedBytes);

    S3ObjectHelper s3ObjectHelper = new S3ObjectHelper();

    Slide slide = new Slide();

    String image_Url = s3ObjectHelper.upload(inputStream,fileName, contentType);
    slide.setImage_Url(image_Url);
    slide.setText(createSlideRequest.getText());
    if(createSlideRequest.getOrder() == 0) {
      slide.setOrder(slideRepository.getMaxOrder() + 1);
    }
    slide.setOrder(createSlideRequest.getOrder());
    return slideRepository.save(slide);
  }
}
