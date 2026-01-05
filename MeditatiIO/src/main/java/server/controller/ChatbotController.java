package server.controller; // 🔹 adaptează pachetul la structura proiectului tău real


import org.springframework.web.bind.annotation.*;
import server.service.ChatbotService;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatbotController {

    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping
    public String chat(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String message = request.get("message");

        System.out.println("CHATBOT: User '" + username + "' says: '" + message + "'");

        return chatbotService.generateResponse(username, message);
    }
}
