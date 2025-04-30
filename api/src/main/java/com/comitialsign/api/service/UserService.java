package com.comitialsign.api.service;

import com.comitialsign.api.repository.NoteRepository;
import com.comitialsign.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteRepository noteRepository;


}
