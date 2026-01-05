package server.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatbotService {

    private final ChatClient chatClient;


    public ChatbotService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String generateResponse(String username, String userPrompt) {
        try {
            return chatClient
                    .prompt()

                    .system("Ești Meditatio, un asistent AI prietenos și de ajutor pentru studenți și profesori.")
                    .user("Utilizatorul " + username + " spune: " + userPrompt)
                    .call()
                    .content();
        } catch (Exception e) {
            System.err.println("Eroare la apelarea modelului Ollama: " + e.getMessage());
            return "Oops! Nu am putut răspunde acum.";
        }
    }
}
