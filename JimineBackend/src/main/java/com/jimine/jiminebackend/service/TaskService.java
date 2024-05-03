package com.jimine.jiminebackend.service;

import com.jimine.jiminebackend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository repository;

//    public ResponseEntity<String> createTask(CreateTaskRequest request) {
////        Task task = Task.builder()
////                .name()
//        return null;
//    }
//
//    public ResponseEntity<String> deleteTask() {
//
//    }
//
//    public ResponseEntity<String> updateTask() {
//
//    }
}
