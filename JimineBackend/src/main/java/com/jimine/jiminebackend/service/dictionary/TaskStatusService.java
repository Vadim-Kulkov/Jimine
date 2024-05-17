package com.jimine.jiminebackend.service.dictionary;

import com.jimine.jiminebackend.model.dictionary.TaskStatus;
import com.jimine.jiminebackend.repository.dictionary.TaskStatusRepository;
import com.jimine.jiminebackend.request.BasePageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TaskStatusService {

    private final TaskStatusRepository repository;

    public Page<TaskStatus> getPage(BasePageRequest request) {
        if(request.getPageSize() == null) {
            request.setPageSize(BasePageRequest.DEFAULT_PAGE_SIZE);
        }

        Pageable pageRequest = PageRequest.of(0, request.getPageSize(), Sort.by("id"));
        return repository.findAll(pageRequest);
    }
}