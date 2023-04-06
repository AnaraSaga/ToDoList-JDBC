package de.telran.todojdbc.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ToDo {
    @NotNull
    private String id;
    @NotNull
    @NotBlank

    private String description;
    @JsonFormat(pattern = "dd-MM-yyyy HH-mm", timezone = "Europe/Zagreb")
    private LocalDateTime created;

    @JsonFormat(pattern = "dd-MM-yyyy HH-mm", timezone = "Europe/Zagreb")
    private LocalDateTime modified;

    private boolean completed;

    public ToDo() {
        LocalDateTime date = LocalDateTime.now();
        this.created = date;
        this.modified = date;
        this.id = UUID.randomUUID().toString();

    }

    public ToDo(String description) {
        this();
        this.description = description;
    }

}
