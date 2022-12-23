package com.example.server.service;

import com.example.server.dao.TaskRepository;
import com.example.server.entity.Task;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List getAllTasks() {
        return taskRepository.findAll();
    }

    public Task updateById(Long taskId, Task newTask) {
        return taskRepository.findById(taskId)
                .map(task -> {
                    task.setTaskName(newTask.getTaskName());
                    task.setTaskText(newTask.getTaskText());
                    task.setRelease_time(newTask.getRelease_time());
                    task.setFinish_time(newTask.getFinish_time());
                    task.setState(newTask.getState());
                    task.setrPersonId(newTask.getrPersonId());
                    task.setfPersonId(newTask.getfPersonId());
                    task.setTaskText(newTask.getTaskText());
                    return taskRepository.save(task);
                }).orElseThrow(() -> new ArticleNotFoundException(taskId));
    }

    public void addTask(Task task) {
        taskRepository.save(task);
    }

    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }
}
