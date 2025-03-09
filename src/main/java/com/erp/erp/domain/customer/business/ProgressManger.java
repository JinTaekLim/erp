package com.erp.erp.domain.customer.business;

import com.erp.erp.domain.customer.common.dto.ProgressDto;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.Progress;
import com.erp.erp.domain.customer.common.exception.NotFoundProgressException;
import com.erp.erp.domain.customer.common.mapper.ProgressMapper;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProgressManger {

  private final ProgressCreator progressCreator;
  private final ProgressReader progressReader;
  private final ProgressDeleter progressDeleter;
  private final ProgressMapper progressMapper;

  public List<Progress> add(Customer customer, List<ProgressDto.Request> req, String accountId) {
    List<Progress> customerProgress = progressReader.findByCustomerId(customer.getId());
    if (req == null || req.isEmpty()) {
      return customerProgress;
    }
    validateCustomerOwnsProgress(customerProgress, req);

    if (!customerProgress.isEmpty()) {
      // isDeleted 가 true 인 값을 삭제하고 진도표를 업데이트
      customerProgress = deleteProgress(customerProgress, req);
      // ID 값이 존재하고, isDeleted 가 false 데이터를 반영해 진도표 업데이트 후 반환
      customerProgress = updateProgress(customerProgress, req, accountId);
    }

    // ID 값이 존재하지 않는 데이터를 진도표 리스트에 추가 후 반환
    List<Progress> progress = addProgress(customerProgress, customer, accountId, req);

    // 업데이트&신규 진도표 저장
    progress = progressCreator.saveAll(progress);

    // 내림차순 정렬 후 반환( 최신 진도표가 상단에 표시되도록 )
    progress.sort(Comparator.comparing(Progress::getId).reversed());
    return progress;

  }

  private void validateCustomerOwnsProgress(List<Progress> progresses,
      List<ProgressDto.Request> req) {
    req.stream()
        .filter(request -> request.getProgressId() != null)
        .forEach(request -> {
          Long progressId = request.getProgressId();
          if (progresses.stream().noneMatch(progress -> progress.getId().equals(progressId))) {
            throw new NotFoundProgressException();
          }
        });
  }


  private List<Progress> deleteProgress(List<Progress> progresses, List<ProgressDto.Request> req) {
    List<Long> ids = getDeletedProgressIds(req);
    progresses.removeIf(progress -> ids.contains(progress.getId()));
    progressDeleter.deleteAllById(ids);
    return progresses;
  }

  private List<Long> getDeletedProgressIds(List<ProgressDto.Request> requests) {
    return requests.stream()
        .filter(ProgressDto.Request::isDeleted)
        .map(ProgressDto.Request::getProgressId)
        .toList();
  }


  private List<Progress> addProgress(List<Progress> updateProgress, Customer customer,
      String createdId, List<ProgressDto.Request> req) {
    List<ProgressDto.Request> addProgress = getRequestsWithNullId(req);
    List<Progress> progress = progressMapper.addProgressToEntityList(addProgress, customer,
        createdId);
    return mergeProgressList(updateProgress, progress);
  }

  private List<ProgressDto.Request> getRequestsWithNullId(List<ProgressDto.Request> requests) {
    return requests.stream()
        .filter(request -> request.getProgressId() == null)
        .toList();
  }

  private List<Progress> updateProgress(List<Progress> customerProgress,
      List<ProgressDto.Request> req, String updatedId) {
    List<ProgressDto.Request> updateRequest = getUpdateProgress(req);

    return customerProgress.stream()
        .map(progress -> {
          return updateRequest.stream()
              .filter(request -> progress.getId().equals(request.getProgressId()))
              .findFirst()
              .map(request -> progress.update(request.getDate(), request.getContent(), updatedId))
              .orElse(progress);
        })
        .toList();
  }


  private List<ProgressDto.Request> getUpdateProgress(List<ProgressDto.Request> req) {
    return req.stream()
        .filter(request -> !request.isDeleted() && request.getProgressId() != null)
        .toList();
  }

  private List<Progress> mergeProgressList(List<Progress> updateProgress,
      List<Progress> addProgress) {
    return Stream.concat(updateProgress.stream(), addProgress.stream())
        .toList();
  }

}