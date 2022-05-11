package com.codegym.g2m6appmusicbe.controller;

import com.codegym.g2m6appmusicbe.model.dto.SongForm;
import com.codegym.g2m6appmusicbe.model.entity.Song;
import com.codegym.g2m6appmusicbe.service.song.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/songs")
public class SongController {
    @Autowired
    private ISongService songService;

    @Value("${file-upload}")
    private String uploadPath;

    @GetMapping()
    public ResponseEntity<Iterable<Song>> getAll(){
        Iterable<Song> songs = songService.findAll();
        return new ResponseEntity<>(songs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song>findById(@PathVariable Long id){
        Optional<Song> songOptional = songService.findById(id);
        return new ResponseEntity<>(songOptional.get(), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Song> save(@ModelAttribute SongForm songForm) {
        MultipartFile image = songForm.getImage();
        if (image.getSize() != 0) {
            String fileName = songForm.getImage().getOriginalFilename();
            try {
                FileCopyUtils.copy(songForm.getImage().getBytes(), new File(uploadPath + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Song song = new Song(songForm.getId(), songForm.getName(), songForm.getDescription(), songForm.getMp3File(), fileName, songForm.getAuthor(),songForm.getArtists(),songForm.getUser(),songForm.getCategory(),songForm.getAlbum(),songForm.getPlaylists(),songForm.getTag());
            return new ResponseEntity<>(songService.save(song), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
