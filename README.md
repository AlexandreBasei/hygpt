# HyGPT Hytale Mod

- Ce mod vise à intégrer un llm au jeu via une interface personnalisée.
- En tapant la commande /hygpt dans le jeu, une fenêtre de chat s'ouvrira, permettant d'écrire un message et de l'envoyer à Mistral AI
- Le LLM va se faire passer pour un personnage non joueur du jeu, un Kweebec
- Pour que le mod fonctionne, ajouter sa propre clé API Mistral AI dans la classe MistralAIService (src/java/com/megalos/HyGPT/service/MistralAIService.java) et recompiler le mod avec Gradle sous InteliJ Idea

## Perspectives d'amélioration
Mon plan initial était que l'interface s'ouvre en interagissant avec un Kweebec personnalisé, mais le modding et la documentation du jeu ne sont pas encore assez développés pour que je comprenne comment faire.
