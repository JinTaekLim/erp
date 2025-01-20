package com.erp.erp.domain.customer.business;

import com.erp.erp.domain.customer.common.dto.ProgressDto;
import com.erp.erp.domain.customer.common.dto.ProgressDto.DeleteProgress;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.Progress;
import com.erp.erp.domain.customer.common.exception.NotFoundProgressException;
import com.erp.erp.domain.customer.common.mapper.ProgressMapper;
import java.util.ArrayList;
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

  public List<Progress> add(Customer customer, ProgressDto.Request req) {
    List<Progress> customerProgress = progressReader.findByCustomerId(customer.getId());
    deleteProgress(customerProgress, req.getDeleteProgresses());
    List<Progress> addProgresses = progressMapper.addProgressToEntityList(req.getAddProgresses(),
        customer);
    List<Progress> updateProgresses = validateAndFilterProgresses(req.getUpdateProgresses(),
        customerProgress);
    List<Progress> progresses = mergeProgresses(addProgresses, updateProgresses);
    progressCreator.saveAll(progresses);
    return mergeUniqueProgresses(customerProgress, progresses);
  }

  private void deleteProgress(List<Progress> progresses, List<DeleteProgress> deleteProgresses) {
    List<Long> ids = deleteProgresses.stream()
        .map(DeleteProgress::getProgressId)
        .peek(id -> {
          if (progresses.stream().noneMatch(progress -> progress.getId().equals(id))) {
            throw new NotFoundProgressException();
          }
        })
        .toList();

    progresses.removeIf(progress -> ids.contains(progress.getId()));
    progressDeleter.deleteAllById(ids);
  }



  private List<Progress> mergeProgresses(List<Progress> addProgress,
      List<Progress> updateProgress) {
    return Stream.concat(addProgress.stream(), updateProgress.stream())
        .toList();
  }

  private List<Progress> mergeUniqueProgresses(List<Progress> customerProgress, List<Progress> newProgress) {
    newProgress.forEach(progress -> {
      customerProgress.removeIf(existingProgress -> existingProgress.getId().equals(progress.getId()));
      customerProgress.add(progress);
    });
    return customerProgress;
  }

  private List<Progress> validateAndFilterProgresses(
      List<ProgressDto.UpdateProgress> updateProgresses,
      List<Progress> customerProgress
  ) {
    if (updateProgresses.isEmpty()) {
      return new ArrayList<>();
    }
    return customerProgress.stream()
        .map(customer -> {
          ProgressDto.UpdateProgress matchingUpdate = updateProgresses.stream()
              .filter(update -> update.getProgressId().equals(customer.getId()))
              .findFirst()
              .orElseThrow(NotFoundProgressException::new);

          return customer.update(matchingUpdate.getDate(), matchingUpdate.getContent());
        })
        .toList();
  }
}