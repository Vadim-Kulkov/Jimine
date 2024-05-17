package com.jimine.jiminebackend.request.task;

import com.jimine.jiminebackend.request.TaskWorkerRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AddUserRequestWrapper {

    List<TaskWorkerRequest> workerRequestList;
}
