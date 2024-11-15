package com.rickenbazolo.functioncalling;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * @author Ricken Bazolo
 */
@Configuration
public class MultiRAGDemo {

    Logger logger = LoggerFactory.getLogger(MultiRAGDemo.class);

    @Bean
    @Description("Utilisez cette fonction pour récupérer les détails de la commande du client.")
    public BiFunction<Request, ToolContext, Response> getCustomerOrder() {
        return (request, context) -> {
            logger.info("Calling getCustomerOrder");
            logger.info("Product code: {}", request.productCode());
            var userId = context.getContext().get("user_id");
            var result = CUSTOMER_ORDERS.values().stream()
                    .filter(order -> order.productCode().equalsIgnoreCase(request.productCode()))
                    .findFirst()
                    .orElse(null);

            if (result == null) {
                return new Response("Aucune commande trouvée pour cet utilisateur");
            }
            context.getContext().put("order_id", result.orderId());

            return new Response("Commande : " + result.orderId() + "\n" +
                    "Date de commande : " + result.orderDate() + "\n" +
                    "Statut : " + result.status());
        };
    }

    @Bean
    @Description("Utilisez cette fonction pour obtenir des informations sur le support technique du produit.")
    public BiFunction<Request, ToolContext, Response> getTechnicalSupport() {
        return (request, context) -> {
            logger.info("Calling getTechnicalSupport");
            logger.info("Product code: {}", request.productCode());
            var result = TECHNICAL_SUPPORT.values().stream()
                    .filter(support -> support.productCode().equalsIgnoreCase(request.productCode()))
                    .findFirst()
                    .orElse(null);

            if (result == null) {
                return new Response("Aucun support technique trouvé pour ce produit");
            }
            var warrantyPolicy = WARRANTY_POLICIES.values().stream()
                    .filter(policy -> policy.productCode().equalsIgnoreCase(request.productCode()))
                    .findFirst()
                    .orElse(null);
            return new Response("Garantie : " + warrantyPolicy.warrantyDescription + " - " + warrantyPolicy.warrantyTerms + "\n" +
                    "Support technique : " + String.join(", ", result.troubleshootingSteps()));
        };
    }

    @Bean
    @Description("Utilisez cette fonction pour obtenir les informations du produit à partir de son code.")
    public BiFunction<Request, ToolContext, Response> getProduct() {
        return (request, context) -> {
            logger.info("Calling getProduct");
            logger.info("Product code: {}", request.productCode());
            var result = PRODUCTS.values().stream()
                    .filter(support -> support.productCode().equalsIgnoreCase(request.productCode()))
                    .findFirst()
                    .orElse(null);

            if (result == null) {
                return new Response("Aucun produit trouvé pour ce code");
            }
            return new Response("Nom : " + result.name() + "\n" +
                    "Problème connu : " + result.knownIssue() + "\n" +
                    "Période de garantie : " + result.warrantyPeriod());
        };
    }

    // Base de données des commandes des clients
    private final Map<Integer, CustomerOrder> CUSTOMER_ORDERS = Map.of(
            1, new CustomerOrder(1,  "X123", "15/10/2024", "Livré"),
            2, new CustomerOrder(2,  "Y456", "20/09/2024", "En cours de livraison"),
            3, new CustomerOrder(3,  "X123", "01/11/2024", "Livré")
    );

    // Base de données des produits
    private final Map<String, Product> PRODUCTS = Map.of(
            "X123", new Product("X123", "Lave-vaisselle", "Problème de fuite connu", "2 ans"),
            "Y456", new Product("Y456", "Réfrigérateur", "Problème de thermostat", "1 an"),
            "Z789", new Product("Z789", "Four à micro-ondes", "Problème de porte", "1 an")
    );

    // Base de connaissances sur la politique de garantie
    private final Map<String, WarrantyPolicy> WARRANTY_POLICIES = Map.of(
            "X123", new WarrantyPolicy("X123", "2 ans de garantie constructeur", "Réparation gratuite dans les 2 ans suivant l'achat"),
            "Y456", new WarrantyPolicy("Y456", "1 an de garantie constructeur", "Remplacement ou réparation dans l'année suivant l'achat"),
            "Z789", new WarrantyPolicy("Z789", "1 an de garantie constructeur", "Réparation gratuite")
    );

    // Base de support technique
    private final Map<String, TechnicalSupport> TECHNICAL_SUPPORT = Map.of(
            "X123", new TechnicalSupport("X123", List.of("Vérifiez le tuyau de vidange", "Assurez-vous que la porte est bien fermée")),
            "Y456", new TechnicalSupport("Y456", List.of("Réglez le thermostat à une température inférieure", "Vérifiez si les ventilateurs fonctionnent correctement")),
            "Z789", new TechnicalSupport("Z789", List.of("Assurez-vous que la porte est correctement fermée", "Vérifiez l'état du joint de porte"))
    );

    public record User(int userId, String userName) {}

    public record CustomerOrder(int orderId, String productCode, String orderDate, String status) {}

    public record Product(String productCode, String name, String knownIssue, String warrantyPeriod) {}

    public record WarrantyPolicy(String productCode, String warrantyDescription, String warrantyTerms) {}

    public record TechnicalSupport(String productCode, List<String> troubleshootingSteps) {}

    public record Request(String productCode) {
    }
    public record Response(String response) {
    }
}
