package com.jimine.jiminebackend.service.dictionary;

import com.jimine.jiminebackend.model.dictionary.UserProjectRole;
import com.jimine.jiminebackend.repository.dictionary.UserProjectRoleRepository;
import com.jimine.jiminebackend.request.BasePageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserProjectRoleService {

    private final UserProjectRoleRepository userProjectRoleRepository;

    public Page<UserProjectRole> getPage(BasePageRequest request) {
        if(request.getPageSize() == null) {
            request.setPageSize(BasePageRequest.defaultPageSize);
        }

        Pageable pageRequest = PageRequest.of(0, request.getPageSize(), Sort.by("id"));
        return userProjectRoleRepository.findAll(pageRequest);
    }
}
