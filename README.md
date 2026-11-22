\# Task Manager - Mini-projet DevSecOps



Application de gestion des tâches (API REST Spring Boot) utilisée comme support pour une chaîne CI/CD sécurisée : Jenkins, SonarQube, OWASP Dependency-Check, Docker, Trivy, Docker Hub, Kubernetes (Minikube) et OWASP ZAP.



\## Architecture



```

controller/   TaskController        -> endpoints REST /api/tasks

service/      TaskService           -> logique métier

repository/   TaskRepository        -> accès aux données (Spring Data JPA)

model/        Task, TaskStatus      -> entité JPA

exception/    GlobalExceptionHandler, TaskNotFoundException

```



Base de données : H2 en mémoire (les données sont perdues au redémarrage).



\## Prérequis



\- Java 17 ou plus

\- Maven 3.9+

\- Git



\## Installation et exécution



```bash

git clone https://github.com/moemenzarraa-coder/task-manager.git

cd task-manager

mvn clean package

java -jar target/task-manager.jar

```



L'application démarre sur `http://localhost:8080`.



\## Tests



```bash

mvn test

```



Le rapport de couverture JaCoCo est généré dans `target/site/jacoco/index.html`.



\## API



| Méthode | URL | Description |

|---|---|---|

| GET | `/api/tasks` | Liste des tâches (filtre optionnel `?status=TODO`) |

| GET | `/api/tasks/{id}` | Détail d'une tâche |

| POST | `/api/tasks` | Créer une tâche |

| PUT | `/api/tasks/{id}` | Modifier une tâche |

| PATCH | `/api/tasks/{id}/status?status=DONE` | Changer le statut |

| DELETE | `/api/tasks/{id}` | Supprimer une tâche |

| GET | `/actuator/health` | État de l'application |



Statuts possibles : `TODO`, `IN\_PROGRESS`, `DONE`.



\## Organisation des branches



\- `main` : version stable, déployée par le pipeline

\- `develop` : intégration des fonctionnalités

\- `feature/<nom>` : une branche par fonctionnalité, fusionnée dans `develop` via pull request



\## Docker



À compléter (phase 3).



\## Déploiement Kubernetes



À compléter (phase 4).



\## Pipeline CI/CD



À compléter (phases 5 et 6).

