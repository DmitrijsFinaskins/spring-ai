# spring-ai

A Spring Boot starter showing how to build production AI apps with Spring AI 2.x — chat memory, structured output, and tool calling against an OpenAI-compatible model.

## What it does

- **Conversational chat with persistent memory** — a rolling 10-message window backed by PostgreSQL via `spring-ai-starter-model-chat-memory-repository-jdbc`, scoped per `conversationId` so multiple users can hold independent conversations.
- **Structured output from free-form prompts** — a `/review` endpoint that takes raw source code and returns a typed `CodeReview` record (`summary`, `issues`, `suggestions`, `qualityScore`) parsed directly from the model response.
- **Tool calling with `@Tool`** — a `/chat-with-tools` endpoint wired to a `WeatherTool` the model can invoke on demand, demonstrating how Spring AI bridges chat prompts to plain Java methods.

## Why this exists

Part of my **AI Baltics** content — practical AI for Baltic developers. Most Spring AI material out there stops at "hello world, here's a `ChatClient`." This repo goes one layer deeper into the three things every real app actually needs — memory, structure, and tools — in the smallest amount of code that still compiles and runs. It's the reference I wish I'd had when I started.

## The interesting bit

Structured output turns the model into a typed API. Define a record, ask for it, get it back:

```java
public record CodeReview(
    String summary,
    List<String> issues,
    List<String> suggestions,
    int qualityScore
) {}

public CodeReview codereview(String code) {
    return chatClient.prompt()
            .user("Review this code. Be specific and concise: " + code)
            .call()
            .entity(CodeReview.class);
}
```

No prompt engineering for JSON, no manual parsing, no "please respond in the following format." Spring AI handles the schema negotiation and deserialization.

## Run it locally

**Prerequisites:** Java 21, Docker, and an OpenAI API key.

1. Start PostgreSQL:
   ```bash
   docker compose up -d
   ```

2. Export your API key:
   ```bash
   export OPENAI_API_KEY=sk-...
   ```

3. Run the app:
   ```bash
   ./mvnw spring-boot:run
   ```

4. Try the endpoints:
   ```bash
   # Chat with memory — same conversationId remembers context
   curl "http://localhost:8080/chat?message=My+name+is+Dmitriy&conversationId=demo"
   curl "http://localhost:8080/chat?message=What+is+my+name?&conversationId=demo"

   # Structured code review
   curl -X POST http://localhost:8080/review \
        -H "Content-Type: text/plain" \
        -d 'public int add(int a, int b) { return a - b; }'

   # Tool calling
   curl "http://localhost:8080/chat-with-tools?message=What+is+the+weather+in+Riga?"
   ```

The JDBC schema for chat memory is auto-initialized on startup (`initialize-schema: always`), so there's no manual migration step.
