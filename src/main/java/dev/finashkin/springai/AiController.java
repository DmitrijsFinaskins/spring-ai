package dev.finashkin.springai;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {

        return aiService.chat(message);
    }

    @PostMapping("/review")
    public CodeReview review(@RequestBody String code) {
        return aiService.codereview(code);
    }

    @GetMapping("/chat-with-tools")
    public String chatWithTools(@RequestParam String message) {
        return aiService.chatWithTools(message);
    }
}
