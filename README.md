🃏 Kata — Comparaison de Mains de Poker
🎯 Description du problème

Votre tâche consiste à comparer plusieurs paires de mains de poker et à indiquer laquelle, le cas échéant, a un rang plus élevé.

🂡 Règles du poker

Un jeu de poker contient 52 cartes :

Couleurs :

Piques (P),

Carreaux (D),

Cœurs (C),

Trèfles (T).

Valeurs :
2, 3, 4, 5, 6, 7, 8, 9, T (10), V (Valet), D (Dame), R (Roi), A (As).

Pour le score :

les couleurs ne sont pas ordonnées,

les valeurs sont ordonnées : 2 → ... → A.

Une main de poker se compose de 5 cartes.
Les mains sont classées selon l’ordre suivant (du plus bas au plus élevé) :

♤ Carte haute

Les mains qui ne rentrent dans aucune catégorie supérieure sont classées selon la valeur de la carte la plus haute, puis de la suivante, etc.
Exemple : 4P 7D 9C VT RP

♧ Paire

Deux cartes ont la même valeur.
Classement basé sur la valeur de la paire, puis sur les autres cartes en ordre décroissant.
Exemple : RD RC 3T 6P 8P

♢ Deux paires

Deux paires distinctes.
Classement basé sur la paire la plus haute, puis la seconde, puis la cinquième carte.
Exemple : 8D 8C VP VT 4D

♡ Brelan

Trois cartes de même valeur.
Classement basé sur la valeur du brelan.
Exemple : AC AP AT 4D 7T

🔁 Suite

Cinq cartes consécutives.
Classement basé sur la carte la plus élevée.
Exemple : 3P 4C 5P 6D 7T

🎨 Couleur

Cinq cartes de la même couleur.
Classement selon les règles de la carte haute.
Exemple : 3P 6P 9P DP RP

🃏 Full

Un brelan + une paire.
Classement basé sur la valeur des trois cartes.
Exemple : 9C 9D 9T 6P 6C

🟦 Carré

Quatre cartes de même valeur.
Classement basé sur la valeur du carré.
Exemple : 10P 10C 10D 10T 3C

🔥 Quinte flush

Suite + couleur.
Classement basé sur la carte la plus élevée.
Exemple : 7P 8P 9P 10P VP

🧪 Cas de test suggérés
Entrée d'exemple

Joueur 1 : 2P 3D 5T 9C RD

Joueur 2 : 2C 3P 4T 8C AP

Joueur 1 : 2P 4T 4C 2D 4P

Joueur 2 : 2T 8T AC RC 3T

Joueur 1 : 2P 3D 5T 9C RD

Joueur 2 : 2C 3P 4T 8C RT

Joueur 1 : 2P 3D 5T 9C RD

Joueur 2 : 2D 3P 5C 9T RT

Chaque ligne d'entrée représente une partie avec deux joueurs. Les cinq premières cartes appartiennent à Joueur 1, et les cinq suivantes à Joueur 2.

Sortie d'exemple

Joueur 2 gagne — avec une carte haute : As

Joueur 1 gagne — avec un full : 4 sur 2

Joueur 1 gagne — avec une carte haute : 9

Match nul