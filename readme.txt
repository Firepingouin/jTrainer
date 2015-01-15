Binôme :
	- PARIMEROS Nicolas
	- LESBROS MAXIME

Liste des tâches :
	- Nicolas :
		- Principalement développement Backend.
			- Développement des Servlets 
				- JTrain (gestion des plans d'entrainement, ajout)
				- JTrainingTask (Queue pour ajout de Servlet asynchrone)
				- JTrainer (page d'accueil avec message)
				- JSearch (affichage des domaines, recherche)
				- JFixtures (initialisation des données de la base) --> A LANCER AU MOINS UNE FOIS AU DEMARRAGE DE JTRAINER
			- Fetch des news à partir d'un flux rss

	- Maxime
		- Développement Front End Javascript / JQuery
			- Page d'accueil (index.html / home.js)
				- Récupération du message dans le memcache sinon dans le datastore
			- Ajout TrainingPlan (ha-addTraining.html / add-training.js)
				- Ajout des exercices dans un tableau
				- Mise à jour en temps réel de la durée totale du plan d'entrainement en haut à droite en fonction du temps des exercices et de leurs répétitions
				- Envoi du trainingPlan en post en AJAX
			- Page de recherche (search-page.js / ha-search-screen.html)
				- Récupère tous les domaines présent
				- Domaines cliquables, renvoit vers les résultats selon le domaine
			- Header search (header.js)
				- Barre de recherche qui renvoit vers ha-result-screen.html avec le paramètre passé en get
				- Connexion OpenID (Google, Yahoo et Open), affiche le nom d'utilisateur quand connecté
			- Page de résultats (results-screen.js / ha-result-screen.html)
				- Recherche en AJAX les résultats selon le paramètre (seachKeyword quand c'est la recherche ou domainId quand on a cliqué sur un domaine)
				- Affiche les trainingPlan et exercices correspondant + les news
		- Connexion avec Open Id (Servlet JLogin)
		- Mise en place de la page index
		- Déploiement de l'application sur Google App Engine

Liste des questions réalisées :
	- Page d'accueil
	- Page de recherche
	- Ajout de plan d'entrainement
		- Page web
		- Fonctionnalité d'ajout
	- Résultats de recherche
		- Page web
		- Search function
		- Fetch News
Liste des questions non réalisées:
	- Page de travail
		- Impossible de faire une requête dans le datastore avec l'id du Training Plan.
	- Visualisation des données personnelles
	- Communication avec un coach

URL de l'application :
	> http://personal-trainer-lesbpari.appspot.com/
	
Github public : 
	> https://github.com/Firepingouin/jTrainer
	
NB : en local ne pas oublier d'appeler /fixtures pour populer le datastore
