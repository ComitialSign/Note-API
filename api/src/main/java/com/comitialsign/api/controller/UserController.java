package com.comitialsign.api.controller;

import com.comitialsign.api.dtos.note.NoteDTO;
import com.comitialsign.api.service.UserService;
import jakarta.validation.Valid;
import jdk.jfr.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@Description("User notes controller")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/create")
    public void createNote(@RequestBody @Valid NoteDTO data) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        userService.createNote(data, userDetails);
    }

    @PutMapping("/update/{id}")
    public void updateNote(@RequestBody @Valid NoteDTO data,@PathVariable UUID id) {
        userService.updateNote(data, id);
    }
}
