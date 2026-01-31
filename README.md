# HyGPT Hytale Mod
## Présentation
- Ce mod vise à intégrer un llm au jeu via une interface personnalisée.
- En tapant la commande /hygpt dans le jeu, une fenêtre de chat s'ouvrira, permettant d'écrire un message et de l'envoyer à Mistral AI
- Le LLM va se faire passer pour un personnage non joueur du jeu, un Kweebec
- Pour que le mod fonctionne, ajouter sa propre clé API Mistral AI dans la classe MistralAIService (src/java/com/megalos/HyGPT/service/MistralAIService.java) et recompiler le mod avec Gradle sous InteliJ Idea

## Installation
- Télécharger le fichier "HyGPT-1.0-SNAPSHOT.jar" et le glisser dans le répertoire des mods de votre dossier d'installation Hytale -> Hytale\UserData\Mods
- Dans l'interface de création de monde, cliquer sur la roue crantée et cocher le mod HyGPT UI, puis créer le monde
- Un fois dans le monde, se donner les permissions administrateur avec la commande /op self

## Utilisation
- Taper la commande /hygpt pour ouvrir la fenêtre de chat
- Entrer un message dans l'input de texte et cliquer sur le bouton "Send" pour envoyer le message
- Mistral AI vous répond en jouant le rôle d'un Kweebec

## Perspectives d'amélioration
Mon plan initial était que l'interface s'ouvre en interagissant avec un Kweebec personnalisé, mais le modding et la documentation du jeu ne sont pas encore assez développés pour que je comprenne comment faire.
