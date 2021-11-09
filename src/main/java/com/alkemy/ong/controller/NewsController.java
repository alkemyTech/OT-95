package com.alkemy.ong.controller;

import com.alkemy.ong.common.PaginatedResultsHeaderUtils;
import com.alkemy.ong.common.converter.ConvertUtils;
import com.alkemy.ong.exception.PageOutOfRangeException;
import com.alkemy.ong.model.entity.News;
import com.alkemy.ong.model.request.CreateNewsRequest;
import com.alkemy.ong.model.response.NewsDetailsResponse;
import com.alkemy.ong.service.abstraction.ICreateNewsService;
import com.alkemy.ong.service.abstraction.IDeleteNewsService;
import com.alkemy.ong.service.abstraction.IGetNewsService;
import com.alkemy.ong.service.abstraction.IListNewsService;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/news")
public class NewsController {

  @Autowired
  private ICreateNewsService createNewsService;

  @Autowired
  private IDeleteNewsService deleteNewsService;

  @Autowired
  private IGetNewsService getNewsService;

  @Autowired
  private IListNewsService listNewsService;

  @Autowired
  private ConvertUtils convertUtils;

  @Autowired
  private PaginatedResultsHeaderUtils paginatedResultsHeaderUtils;

  private static final int PAGE_SIZE = 10;

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<NewsDetailsResponse> create(
      @RequestBody(required = true) @Valid CreateNewsRequest createNewsRequest)
      throws EntityNotFoundException {
    NewsDetailsResponse newsDetailsResponse = convertUtils.createToResponse(
        createNewsService.create(createNewsRequest));
    return new ResponseEntity<>(newsDetailsResponse, HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable("id") long id)
      throws EntityNotFoundException {
    deleteNewsService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<NewsDetailsResponse> getBy(@PathVariable("id") long id)
      throws EntityNotFoundException {
    NewsDetailsResponse newsDetailsResponse = convertUtils.getToResponse(getNewsService.getBy(id));
    return new ResponseEntity<>(newsDetailsResponse, HttpStatus.OK);
  }

  @GetMapping(params = "page")
  public ResponseEntity<?> getPage(@RequestParam("page") int page, UriComponentsBuilder uriBuilder,
      HttpServletResponse response) throws PageOutOfRangeException {
    Page<News> pageResponse = listNewsService.list(page, PAGE_SIZE);

    if (page > pageResponse.getTotalPages()) {
      throw new PageOutOfRangeException("Page " + page + " out of range");
    }

    paginatedResultsHeaderUtils.addLinkHeaderOnPagedResult(uriBuilder, response, page,
        pageResponse.getTotalPages(), "/news");

    return new ResponseEntity<>(convertUtils.listToResponse(pageResponse.getContent()),
        HttpStatus.OK);
  }
}
