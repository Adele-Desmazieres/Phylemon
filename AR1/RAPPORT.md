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
Dans le cadre de notre deuxième année en licence d’informatique à l’Université de Paris Cité, nous avons implémenté une interface graphique fonctionnelle permettant l'alignement de deux courtes séquences d'ADN (ou protéines) selon l'algorithme d'alignement global de Needleman et Wunsch dans le langage Java. Ce projet, au croisement entre l'informatique et la biologie, a pour but d'identifier, comparer et/ou prédire la fonction de gènes/protéines.

## Cahier des charges <a id="partie2"></a>
---
Les fonctionnalités suivantes sont requises et ont été implémentées :
- Possibilité d'aligner n'importe quelle paire de séquences de moins de 20 nucléotides.
- Possibilité de changer tous les scores
- Boutons "Compute Optimal Alignment" et "Clear Path" permettant de mettre en évidence ou non le chemin optimal sur la matrice.
- Affichage de la matrice
- Affichage de l'alignement

## Modélisation <a id="partie3"></a>
---
Nous avons décomposé notre codes en trois packages selon le modèle MVC afin de le structurer. 

(insertion du petit diagramme de classes ici ? ou lien vers annexe ?)

## Décomposition du code : le modèle MVC <a id="partie4"></a>
---
Nous avons décomposé notre code selon le modèle MVC en 3 packages : Modele, Vue, Controleur.

Le `Modele` est composé des différentes classes pour la modélisation de la matrice : la classe globale, la matrice et les cases pour la matrice ainsi que tous les calculs qui sont à effectuer pour remplir la matrice.

La `Vue` comporte toutes les classes et méthodes concernant l'interface graphique, que ce soit les différents boutons, l'alignement de séquence, le détail des calculs des cases ou encore la matrice. Ce package comporte aussi une classe _VueMatrice_  qui permet à l'utilisateur d'intéragir avec la matrice.

Le `Controleur` permet de faire le lien entre le Modele et la Vue. Il permet notamment de lancer le calcul de la matrice ou encore de mettre à jour les cases.

## Organisation de l'implémentation <a id="partie5"></a>
---
Nous avons créé deux classes, _Global_ et _GlobalProt_, qui sont les deux classes centrales du projet. Elles sont essentielles car elles contiennent des attributs tels que la matrice ou les séquences alignées, mais aussi les méthodes les plus importantes de calculs que ce soit le score des différentes cases ou du chemin optimal. 

_GlobalProt_ héritant de _Global_ redéfinie certaines méthodes qui changent lors du passage en mode protéines, comme le calcul du score de chaque case ou celui du score de l'alignement de séquence.

L'alignement optimal des deux séquences (que ce soit pour les ADN ou les protéines) a été réalisé au moyen d'une méthode récursive `cheminOptimalRec()`. Pour chaque CaseMatrice, cette fonction récupère les trois cases adjacentes qui ont été utilisées dans le calcul de la case en question. Sont ensuite testées les différentes possibilités d'alignement que sont les gap (vertical ou horizontal) et l'alignement dit "classique". Prenons une caseMatrice que nous nommons a et ses trois cases dites "triangles" b, c et d. Si la valeur de l'une des cases précédentes sommée avec la valeur de match, mismatch ou de gap (définies ou modifiées par l'utilisateur) correspond à la valeur dans la case a, alors cette case a est ajoutée à une liste de CaseMatrice et l'appel est ensuite réalisé avec la case triangle dont la valeur coincidait. Lorsque la case en paramètre de la fonction correspond à l'une des cases en bordure, la liste de Case est copiée dans l'attribut _chemin_ de la classe Globale. L'appel en cours est terminé et selon la liste des appels effectués, d'autres possibilités vont ensuite être testées. Le chemin finalement retenu n'est pas forcément unique mais ce sera le dernier chemin testé et réussi. Nous nous sommes donc basées sur la méthode du _backtracking_. 

## Les problèmes rencontrés <a id="partie6"></a>
---
Nous avons rencontré des problèmes pour la modélisation des matrices. En effet, de nombreuses questions se sont posées afin de les modéliser, telles que les différentes classes dont nous avions besoin, leur organisation. De plus, nous devions bien séparer la matrice d'ADN et celle des protéines. Certaines méthodes devaient être redéfinies pour les protéines, afin de ne pas compliquer les fonctions déjà écrites pour l'ADN.

Une première méthode a été écrite de manière récursive afin de calculer les valeurs dans les cases des matrices. Cependant, pour de longues séquences d'ADN, la méthode était trop longue et n'affichait donc pas les valeurs dans la matrice sur l'interface graphique. Il a donc fallu réécrire la méthode de manière récursive.

Lors de l'ajout du mode avec les protéine, nous avons rencontré quelques difficultés. En effet, comme nous intégrions une matrice de substitution, le modde de calcul des scores changeait. Il a donc été utile de redéfinir correctement la méthode de calcul des scores pour les protéines. Cela nous a aussi obligé à rédéfinir la méthode pour le calcul du chemin optimal qui ne se faisait plus en l'absence de Match et misMatch.

La recherche du chemin optimal a nécessité la création de diverses méthodes telles que `estUnGap` ou encore `renvoieTriangle` qui renvoie un tableau de trois caseMatrice ayant servi au calcul d'une caseMatrice prise en paramètres. Par ailleurs, cette méthode nécessitait de parcourir toutes les possibilités de chemins possibles ce qui nous a amenées à envisager le _backtracking_ comme solution potentielle.

Afin de réaliser l'extension du _Custom path_, il nous a fallu d'abord songer à la manière dont nous pouvions relier le code "brut", c'est à dire la classe _Globale_ et ses nombreuses méthodes, à un affichage dynamique. Ainsi, la réalisation de l'affichage customisé est réalisé par le _Controleur_ qui va construire les séquences selon les cases sélectionnées par l'utilisateur. Pour ce faire, nous avons donc utilisé un attribut boolean _colore_ permettant de connaître l'état d'une case. Le principe est relativement proche de la recherche de l'alignement optimal puisque sont comparées les valeurs des cases ainsi que leur position géographique les unes par rapport aux autres. A chaque nouveau clic, l'ancien panneau est retiré et le calcul est de nouveau effectué avant affichage. Nous avons rencontré quelques problèmes de superposition de JPanel, réglés par la mise à jour du JPanel général de l'interface.

fichier CSV

## Extensions <a id="partie7"></a>
---
- Bouton "Custom Path"
- Affichage expliquant comment le score de la case a été trouvé
- Second mode : alignements de protéines
- Concernant le mode ADN : possibilité de choisir l'affichage avec la matrice de substitution
- Scanner des matrices de substitution
- Fusion avec le projet AR2 (Planet 622)
---
Le Custom Path permet à l'utilisateur de créer son propre chemin à n'importe quel endroit de la matrice sous certaines conditions : il ne peut "avancer" qu'en diagonale gauche, en haut ou sur le côté gauche de la case sélectionnée. Une fois sélectionnée, une case peut être désélectionnée si le joueur considère que son alignement ne convient pas. Cette subtilité permet d'éviter à l'utilisateur de devoir recommencer l'intégralité de son chemin. Le code calcule et affiche de manière dynamique le score du joueur obtenu avec le chemin dessiné. Cette fonctionnalité a été implémentée pour les deux modes.

Lorsque la souris interagit avec une case, le détail des calculs concernant les cases adjacentes s'affiche en haut à droite de la fenêtre. Cette information est visible à chaque instant sous le mode _ADN_.

Nous avons décidé de donner à l'utilisateur la possibilité de pouvoir aligner des séquences protéiques. Les protéines, représentées par leur code 1 lettre, peuvent s'aligner de la même manière que des nucléotides. Nous avons décidé que les scores, pour les protéines, étaient caculés à partir d'une matrice de substitution.

Concernant l'alignement de séquences d'ADN, l'utilisateur a le choix entre deux modes distincts : un mode standard utilisant des scores de match, mismatch et gap préalablement précisés(, ainsi qu'un mode utilisant une matrice de substitution). Nous avons choisi d'utiliser la matrice BLOSUM.

Un scanner de matrices de substitution a été réalisé. Ce scanner permet de lire des fichiers .csv contenant les matrices de substitution.

Nous avons eu l'opportunité de fusionner notre projet avec le projet AR2 (Planet 622).

## Conclusion <a id="partie8"></a>
---
Ainsi le projet a été réalisé dans son entièreté avec des extensions. Ces dernières ont une place importante dans le projet et la modélisation. En effet, elles nous ont obligées a bien réfléchir à notre code et à l'organiser de la manière la plus adaptée possible afin de ne réécrire que le minimum de fonctions pour les protéines et les matrices de substitution. 

Nous avons réalisé ce projet progressivement tout au long du semestre. Le travail a été réparti de manière égale entre chaque membre du groupe et tout le monde a pu faire à la fois de la modélisation et de l'interface graphique.

Nous avons eu la possibilité de fusionner notre projet avec le projet AR2 (Planet 622).