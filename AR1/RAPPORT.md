RAPPORT - Projet de programmation PI4 2021/2022  
**AR1 - Alignement de séquences**  
==============
<ins>Référente</ins> : Alice Roger  
<ins>Groupe J</ins> : Dounia BENYAKHLAF, Linéa DESARBRE, Louise LAM, Elvire TRAMA 

1. [Introduction](#partie1)  
2. [Cahier des charges](#partie2)  
3. [Modélisation](#partie3)  
4. [Décomposition du code : le modèle MVC](#partie4)  
5. [Organisation de l’implémentation](#partie5)
6. [Les problèmes rencontrés](#partie6)  
7. [Extensions](#partie7) 
8. [Conclusion](#partie8) 

## Introduction <a id="partie1"></a>
---
Dans le cadre de notre deuxième année en licence d’informatique à l’université de Paris Cité, nous avons implémenté une interface graphique fonctionnelle permettant l'alignement de deux courtes séquences d'ADN (ou protéique) selon l'algorithme d'alignement global de Needleman et Wunsch dans le langage Java. Ce projet, au croisement entre l'informatique et la biologie, a pour but d'identifier, comparer et/ou prédire la fonction de gènes/protéines.

## Cahier des charges <a id="partie2"></a>
---
Les fonctionnalités suivantes sont requises et ont été implantées :
- Possibilité d'aligner n'importe quelle paire de séquences de moins de 20 nucléotides.
- Possibilité de changer tous les scores
- Boutons "Compute Optimal Alignment" et "Clear Path" permettant de mettre en évidence ou non le chemin optimal sur la matrice.
- Affichage de la matrice
- Affichage de l'alignement

## Modélisation <a id="partie3"></a>
---
Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean odio turpis, malesuada a metus at, ullamcorper tincidunt lorem. Nullam gravida sodales risus feugiat ultricies. Praesent lacinia nisl consequat, pharetra nunc sed, tincidunt tellus. Ut tincidunt nulla sit amet erat laoreet suscipit. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Vivamus sed mollis arcu. Nam purus nibh, pulvinar id ornare semper, pharetra at risus. In hac habitasse platea dictumst. Nunc volutpat eget leo ut gravida. Curabitur et ligula at risus porttitor pulvinar. Aliquam non neque et tellus elementum porttitor. Phasellus velit lorem, consectetur at interdum nec, porttitor ac eros. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.

## Décomposition du code : le modèle MVC <a id="partie4"></a>
---
Nous avons décomposé notre code selon le modèle MVC en 3 packages : Modele, Vue, Controleur.

Le `Modele` est composé des différentes classes pour la modélisation de la matrice : la classe globale, la matrice et les cases pour la matrice ainsi que tous les calculs qui sont à effectuer pour remplir la matrice.

La `Vue` comporte toutes les classes et méthodes concernant l'interface graphique, que ce soit les différents boutons, l'alignement de séquence, le détail des calculs des cases ou encore la matrice. Ce package comporte aussi une classe _VueMatrice_  qui permet à l'utilisateur d'intéragir avec la matrice.

Le `Controleur` permet de faire le lien entre le Modele et la Vue. Il permet notamment de lancer le calcul de la matrice ou encore de mettre à jour les cases.

## Organisation de l'implémentation <a id="partie5"></a>
---
Nous avons créé deux classes, _Global_ et _GlobalProt_, qui sont les deux classes les centrales du projet. Elles sont essentielles car elles contiennent des attributs tels que la matrice ou les séquences alignées, mais aussi les méthodes les plus importantes de calculs que ce soit le score des différentes cases ou du chemin optimal. 

_GlobalProt_ héritant de _Global_ redéfinie certaines méthodes qui changent pour lors du passage en mode protéines, comme le calcul du score de chaque case ou du score de l'alignement de séquence.

## Les problèmes rencontrés <a id="partie6"></a>
---
Nous avons rencontré des problèmes pour la modélisation des matrices. En effet, de nombreuses questions se sont posées afin de les modéliser, telles que les différentes classes dont nous avions besoin, leur organisation. De plus, nous devions bien séparer la matrice d'ADN et celle des protéines. Certaines méthodes devaient être redéfinies pour les protéines, afin de ne pas compliquer les fonctions déjà écrites pour l'ADN.

Une première méthode a été écrite de manière récursive afin de calculer les valeurs dans les cases des matrices. Cependant, pour des séquences d'ADN trop longues, la méthode était trop longue et n'affichait donc pas les valeurs dans la matrice sur l'interface graphique. Il a donc fallu réécrire la méthode de manière récursive.

chemin optimal 

Lors de l'ajout du mode avec les protéine, nous avons rencontré quelques difficultés. En effet, comme nous intégrions une matrice de substitution, le modde de calcul des scores changait. Il a donc été utile de redéfinir correctement la méthode de calcul des scores pour les protéines. Cela nous a aussi obligé à rédéfinir la méthode pour le calcul du chemin optimal qui ne se faisait plus en l'absence de Match et misMatch.

fichier CSV

## Extensions <a id="partie7"></a>
---
- Bouton "Custom Path"
- Affichage expliquant comment le score de la case a été trouvé
- Second mode : alignements de protéines
- Concernant le mode ADN : possibilité de choisir l'affichage avec la matrice de substitution
- Scanner des matrices de substitution
---
Le Custom Path permet à l'utilisateur de créer son propre chemin de la fin de la matrice jusqu'au début. Le code calcule et affiche de manière dynamique le score du joueur obtenu avec le chemin dessiné.

Lorsque la souris interagit avec une case, le détail des calculs concernant les cases adjacentes s'affiche en haut à droite de la fenêtre.

Nous avons décidé de donner à l'utilisateur la possibilité de pouvoir aligner des séquences protéiques. Les protéines, représentées par leur code 1 lettre, peuvent s'aligner de la même manière que des nucléotides. Nous avons décider que les scores, pour les protéines, étaient caculés à partir d'une matrice de substitution.

Concernant l'alignement de séquences d'ADN, l'utilisateur a le choix entre deux modes distincts : un mode standard utilisant des scores de match, mismatch et gap préalablement précisés, ainsi qu'un mode utilisant une matrice de substitution.

Un scanner de matrices de substitution a été réalisé. Ce scanner permet de lire des fichiers .csv contenant les matrices de substitution.

## Conclusion <a id="partie8"></a>
---
Ainsi le projet a été réalisé dans son entièreté avec des extensions. Ces dernières ont une place importante dans le projet et la modélisation. En effet, elles nous ont obligées a bien réfléchir à notre code et à l'organiser de manière la plus adaptée afin de ne réécrire que le minimum de fonctions pour les protéines et les matrices de substitution. 

Nous avons réalisé ce projet progressive tout au long du semestre. Le travail a été réparti de manière égale entre chaque personne et tout le monde a pu faire à la fois de la modélisation et de l'interface graphique.