package de.telran.todojdbc.controller;


import de.telran.todojdbc.model.ToDo;
import de.telran.todojdbc.repository.CommonRepository;
import de.telran.todojdbc.validation.ToDoValidationError;
import de.telran.todojdbc.validation.ToDoValidationErrorBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ToDoController {

    private CommonRepository<ToDo> repository;

    @Autowired
    public ToDoController(CommonRepository<ToDo> repository) {
        this.repository = repository;
    }

    //find and get all ToDos
    @GetMapping("/todos")
    public ResponseEntity<Iterable<ToDo>> getTodos() {
        return ResponseEntity.ok(repository.findAll());
    }

    //find by id
    @GetMapping("/todo/{id}")
    public ResponseEntity<ToDo> getTodoById(@PathVariable String id) {
        return ResponseEntity.ok(repository.findById(id));
    }

    //find by key (id)
    @GetMapping("/todo")
    public ResponseEntity<ToDo> getTodoById2(@RequestParam String id) {
        return ResponseEntity.ok(repository.findById(id));
    }


    //this method allows to create and update
    @RequestMapping(value = "/todo", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<?> createTodo(@Valid @RequestBody ToDo toDo, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(ToDoValidationErrorBuilder.fromBindingErrors(errors));
        }
        ToDo result = repository.save(toDo);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/todo/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        ToDo dataBaseToDo = repository.findById(id);
        if (dataBaseToDo == null) {
            return ResponseEntity.notFound().build();
        }
        repository.delete(dataBaseToDo);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/todo")
    public ResponseEntity<?> deleteToDo(@RequestBody ToDo toDo) {
        ToDo dataBaseToDo = repository.findById(toDo.getId());
        if (dataBaseToDo == null) {
            return ResponseEntity.notFound().build();
        }
        repository.delete(dataBaseToDo);
        return ResponseEntity.noContent().build();

    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ToDoValidationError handleException(Exception e) {
        return new ToDoValidationError((e.getMessage()));
    }

}
