package com.comitialsign.api.service;

import com.comitialsign.api.domain.note.Note;
import com.comitialsign.api.domain.user.User;
import com.comitialsign.api.dtos.note.NoteDTO;
import com.comitialsign.api.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    AuthService authService;

    @Autowired
    private NoteRepository noteRepository;

    public void createNote(NoteDTO data, UserDetails userDetails){
        var user = getCurrentUser(userDetails);
        LocalDateTime now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Note note = new Note(data.title(), data.content(), now, now, (User) user);
        noteRepository.save(note);
    }

    public void updateNote(NoteDTO data, UUID id){
        LocalDateTime now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toLocalDateTime();

        noteRepository.findById(id).map(noteUpdated -> {
            noteUpdated.setTitle(data.title());
            noteUpdated.setContent(data.content());
            noteUpdated.setUpdatedAt(now);
            return noteRepository.save(noteUpdated);
        }).orElseThrow(() -> new RuntimeException("Nota não encontrada."));
    }

    private UserDetails getCurrentUser(UserDetails userDetails) {
        return authService.loadUserByUsername(userDetails.getUsername());
    }
}
