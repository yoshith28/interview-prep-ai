package com.examly.springapp.controller;

import com.examly.springapp.model.AnswerRequest;
import com.examly.springapp.service.GeminiAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnswerController {
    @Autowired
    private GeminiAiService geminiAiService;

    @PostMapping("/answer")
    public ResponseEntity<String> submitAnswer(@RequestBody AnswerRequest request) {
    String prompt = "You are an expert technical interviewer. Review the following interview question and the user's answer. Respond in this exact JSON format (no extra text): {\"summary\":\"<single-line feedback>\", \"correctAnswer\":\"<2-3 line correct answer>\", \"score\":<score out of 10>, \"details\":\"<1-2 line explanation>\"}.\nQuestion: "
        + request.getQuestion() + "\nAnswer: " + request.getAnswer() + "\n";
        String feedback = geminiAiService.getAiResponse(prompt);
        return ResponseEntity.ok(feedback);
    }
}
