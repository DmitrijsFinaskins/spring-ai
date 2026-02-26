package dev.finashkin.springai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final ChatClient chatClient;
    private final MessageSource messageSource;

    public AiService(ChatClient.Builder builder, MessageSource messageSource){
        this.chatClient = builder.build();
        this.messageSource = messageSource;
    }

    public String chat(String message) {
        return chatClient.prompt(message)
                .call()
                .content();
    }

    public CodeReview codereview(String code) {
        return chatClient.prompt()
                .user("Review this code. Be specific and concise: " + code)
                .call()
                .entity(CodeReview.class);
    }

    public String chatWithTools(String message){

        return chatClient.prompt(message)
                .tools(new WeatherTool())
                .call()
                .content();
    }
}
