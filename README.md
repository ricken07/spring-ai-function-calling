# Function Calling avec Spring AI - Démo Multi RAG

Ce projet est une démonstration de l'implémentation de Function Calling des modèles de langage (LLM) avec Spring AI, dans un contexte Multi RAG (Retrieval-Augmented Generation). Il met en œuvre une architecture basée sur Spring Boot pour intégrer des fonctionnalités avancées permettant l'exécution de fonctions spécifiques via des outils contextuels.

## Fonctionnalités

1. **Récupération des Commandes Clients** :
   - Permet de récupérer les détails d'une commande en fonction du code produit.
   
2. **Support Technique** :
   - Fournit des informations de dépannage pour un produit donné, ainsi que des politiques de garantie associées.

3. **Gestion des Produits** :
   - Accède aux informations sur les produits, y compris les problèmes connus et la période de garantie.

4. **Multi RAG** :
   - Utilise une combinaison de mémoire contextuelle et de fonctions pour améliorer la précision des réponses générées par les modèles de langage.

## Structure du Projet

- **FcApplication** : Point d'entrée principal de l'application Spring Boot.
- **FcController** : Contrôleur REST qui gère les requêtes utilisateur et les passe au client de chat Spring AI.
- **MultiRAGDemo** : Configuration des fonctions personnalisées pour gérer les commandes clients, le support technique et les informations produits.

### Installation

1. Clonez le dépôt :
   ```bash
   git clone https://github.com/ricken07/spring-ai-function-calling.git
   cd spring-ai-function-calling

Un exemple : J'ai acheté un lave-vaisselle le mois dernier, il ne fonctionne plus, que puis-je faire ?
