package com.examly.springapp.controller;

import com.examly.springapp.model.Question;
import com.examly.springapp.service.GeminiAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class QuestionController {

    @Autowired
    private GeminiAiService geminiAiService;

    @GetMapping("/questions")
    public ResponseEntity<List<Question>> getQuestions(@RequestParam String topic) {
        // Use Gemini AI to generate 5 interview questions for the topic
        String prompt = "Generate 5 unique, technical interview questions for the topic in just simple one or two lines and answers should be one or two word answers: '" + topic + "'. Return only the questions as a numbered list.";
        String aiResponse = geminiAiService.getAiResponse(prompt);
        List<Question> questions = Stream.of(aiResponse.split("\\n"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(line -> {
                    // Remove leading number and dot if present
                    String qText = line.replaceFirst("^\\d+\\.\\s*", "");
                    return new Question(qText, topic);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(questions);
    }

    private Question createQuestion(String question, String topic) {
    Question q = new Question();
    q.setQuestion(question);
    q.setTopic(topic);
    return q;
    }
}
