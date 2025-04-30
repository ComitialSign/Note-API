package com.comitialsign.api.repository;

import com.comitialsign.api.domain.note.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NoteRepository extends JpaRepository<Note, UUID> {
}
