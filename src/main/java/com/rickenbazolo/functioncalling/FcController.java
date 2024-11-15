package com.rickenbazolo.functioncalling;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author rickenbazolo
 */
@RestController
public class FcController {

    private final ChatClient chatClient;

    public FcController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultFunctions("getCustomerOrder", "getTechnicalSupport")
                .defaultToolContext(Map.of("user_id", 1233))
                .defaultAdvisors(new PromptChatMemoryAdvisor(new InMemoryChatMemory()),
                        new SimpleLoggerAdvisor())
                .build();
    }

    @GetMapping("/ask")
    public String chat(@RequestParam("question") String question) {
        return chatClient.prompt()
                .system("""
                        Vous êtes un assistant virtuel chargé d’aider les clients à résoudre leurs problèmes.
                        Pour chaque question, suivez les étapes suivantes :
                        - Analyser la question du client afin de bien comprendre son besoin.
                        - Demander le code du produit si cela est nécessaire pour obtenir les détails de la commande.
                        - Utiliser le code du produit pour accéder aux informations sur le support technique du produit.
                        
                        Si une question n’est pas claire, invitez le client à la reformuler pour mieux vous aider.
                        """)
                .user(question)
                .call()
                .content();
    }
}
