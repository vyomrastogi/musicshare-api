package com.musicshare.controller;

import com.musicshare.entity.Playlist;
import com.musicshare.service.PlaylistService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {PlaylistController.class})
class PlaylistControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlaylistService service;

    @Test
    @DisplayName("createPlaylist - with a name")
    public void testCreatePlaylistWithName() throws Exception {
        when(service.createPlaylist(any())).thenReturn(Playlist.builder().name("playList1").id(1l).songs(new ArrayList<>()).build());
        mockMvc.perform(post("/api/v1/playlist/{playListName}", "playList1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.name").value("playList1"))
                .andExpect(jsonPath("$.data.songs").isEmpty())
                .andExpect(jsonPath("$.message").value("playlist created successfully."));
        verify(service, times(1)).createPlaylist(any());
    }

    @Test
    @DisplayName("createPlaylist - without a name")
    public void testCreatePlaylistWithoutName() throws Exception {
        mockMvc.perform(post("/api/v1/playlist/{playListName}", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.message").value("playlist name is required."));
        verify(service, times(0)).createPlaylist(any());
    }

    @Test
    @DisplayName("createPlaylist - with a name as a blank space")
    public void testCreatePlaylistWithNameAsBlankSpace() throws Exception {
        mockMvc.perform(post("/api/v1/playlist/{playListName}", "   "))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.message").value("playlist name is required."));
        verify(service, times(0)).createPlaylist(any());
    }
}