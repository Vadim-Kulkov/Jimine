package com.jimine.jiminebackend.service.dictionary;

import com.jimine.jiminebackend.model.entity.dictionary.UserTaskRole;
import com.jimine.jiminebackend.repository.dictionary.UserTaskRoleRepository;
import com.jimine.jiminebackend.model.request.BasePageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserTaskRoleService {

    private final UserTaskRoleRepository userTaskRoleRepository;

    public Page<UserTaskRole> getPage(BasePageRequest request) {
        if(request.getPageSize() == null) {
            request.setPageSize(BasePageRequest.DEFAULT_PAGE_SIZE);
        }

        Pageable pageRequest = PageRequest.of(0, request.getPageSize(), Sort.by("id"));
        return userTaskRoleRepository.findAll(pageRequest);
    }
}

