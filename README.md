# Projet Dev

# FRANÇAIS 
## Comment démarrer le server

**VOUS AVEZ BESOIN DE JAVA 21**

Tout d'abord vous devez accéder au dossier `server`:
```
cd server
```

Vous pouvez ensuite démarrer le fichier ``MatchmaingServer.java``, en écrivant dans le terminal une fois que vous êtes dans le dossier server : ``java MatchmakingServer.java``

Ou si vous utilisez IntelliJ, vous pouvez simplement le démarrer en faisant clique droit sur le dossier `MatchmakingServer`

<img src="./img/startserver.png">

## Comment jouer au jeu

Vous avez simplement besoin d'aller dans le dossier ``server`` :
``cd server``

Vous pouvez ensuite démarrer le client en écrivant dans votre terminal : ``java ../client/ClientGame.java``

ou si vous êtes sur IntelliJ, en faisant clique droit sur le fichier and en cliquant sur run

<img src="./img/startclient.png">

## Qu'est ce que c'est comme jeu

Le jeu auquel vous pouvez jouer est le morpion, il y a 2 modes, vous pouvez choisir de sois jouer seul contre une "IA", ou bien contre un autre joueur en multijoueur

<img src="./img/gamemode.png">

## Joueur Solo
Si vous décidez de jouer seule, la partie commencera instantanément, et vous serez contre une "IA"

<img src="./img/sologame.png">


## Multi-joueur
Si vous décidez de jouer en multi-joueur, vous devrez d'abord entrer l'ip de la machine qui à lancer le server, si vous êtes la machine qui a lancé le serveur vous pouvez simplement écrire localhost, et vous devrez ensuite choisir votre pseudonyme

<img src="./img/multiplayermenu.png">

Si la partie ne commence pas tout de suite après avoir choisi votre nom d'utilisateur, c'est parce que vous êtes le seul joueur connecté au serveur, une fois qu'un autre joueur sera connecté et qu'il aura choisi son pseudonyme, la partie commencera tout de suite après.
