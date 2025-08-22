package com.examly.springapp.controller;

import com.examly.springapp.service.GeminiAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "http://localhost:4200")
public class GeminiAiController {
    @Autowired
    private GeminiAiService geminiAiService;

    @PostMapping("/generate")
    public ResponseEntity<String> generate(@RequestBody String prompt) {
        String aiResponse = geminiAiService.getAiResponse(prompt);
        return ResponseEntity.ok(aiResponse);
    }
}
