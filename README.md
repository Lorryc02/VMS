# 🎟️ VMS — Voucher Management System

**VMS (Voucher Management System)** est une application de bureau lourde et autonome développée en JavaFX. Elle est dédiée à la gestion, la vérification, l'approbation et le suivi sécurisé du cycle de vie de bons de cadeaux. 

L'application intègre un contrôle strict des privilèges utilisateurs, une persistance robuste sur base de données relationnelle et une architecture respectant le modèle MVC.

---

## 📥 Téléchargement & Démo Rapide (Recommandé)

Pour tester l'application instantanément sans avoir à configurer d'environnement de développement (Java/IntelliJ) :

1. Rendez-vous sur la page principale du portfolio et cliquez sur le bouton **Démo**.
2. Téléchargez et extrayez le dossier compressé `VMS.zip`.
3. Double-cliquez sur l'exécutable **`VMS.exe`** pour lancer l'application.

> 💡 *Note : L'application est directement reliée à la base de données de test en ligne. Vous pouvez utiliser les identifiants par défaut ci-dessous pour explorer l'interface.*

### 🔑 Compte SuperUser par défaut
* **Email :** `admin@vms.com`
* **Mot de passe :** `Admin@1234`

---

## 👥 Rôles et Droits d'Accès

Le système implémente une matrice de droits d'accès stricte selon le rôle de l'utilisateur connecté :

| Rôle | Initier | Valider | Paiement | Modifier Voucher | Gestion Users | Gestion Clients |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| **SuperUser** | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| **Superviseur** | ❌ | ❌ | ❌ | ✅ | ❌ | ✅ |
| **Initiateur** | ✅ | ❌ | ❌ | ❌ | ❌ | ❌ |
| **Approbateur**| ❌ | ✅ | ❌ | ❌ | ❌ | ❌ |
| **Comptable** | ❌ | ❌ | ✅ | ❌ | ❌ | ❌ |

---

## 🔄 Flux de Traitement d'une Demande

Le cycle de vie d'un bon cadeau (Voucher) suit un workflow métier précis et sécurisé :
```text
[ Initiateur ] ──( Crée la demande )──> Statut: INITIÉ
│
▼
[ Approbateur ] ──( Analyse & Valide )──> Statut: APPROUVÉ ou REJETÉ
│
▼
[ Comptable ]   ──( Effectue le versement )──> Statut: PAYÉ
```
📂 Structure du Projet (Modèle MVC)
Le code source respecte une architecture claire séparant l'interface graphique (views), la logique métier (controllers), l'accès aux données (models/DAO) et la sécurité (service).

```text
src/
└── main/
    ├── java/
    │   └── org/example/
    │       ├── Main.java
    │       ├── controllers/
    │       │   ├── LoginController.java
    │       │   ├── DashboardController.java
    │       │   ├── OverviewController.java
    │       │   ├── InitierController.java
    │       │   ├── ValiderController.java
    │       │   ├── PaiementController.java
    │       │   ├── ModifierVoucherController.java
    │       │   ├── MessagesController.java
    │       │   ├── AjoutClientController.java
    │       │   ├── AjoutUserController.java
    │       │   ├── ModifierSupprimerClientsController.java
    │       │   └── ModifierSupprimerUserController.java
    │       ├── models/
    │       │   ├── User.java
    │       │   ├── Client.java
    │       │   ├── Demandes.java
    │       │   ├── Voucher.java
    │       │   ├── DatabaseConnection.java
    │       │   ├── UserDAO.java
    │       │   ├── ClientDAO.java
    │       │   ├── DemandeDAO.java
    │       │   ├── VoucherDAO.java
    │       │   └── UserSession.java
    │       ├── service/
    │       │   └── AuthService.java
    │       └── utils/
    │           └── Session.java
    └── resources/
        ├── views/
        │   ├── Login.fxml
        │   ├── Dashboard.fxml
        │   ├── overview.fxml
        │   ├── Initier.fxml
        │   ├── Valider.fxml
        │   ├── voucher_paiement.fxml
        │   ├── voucher_modifier.fxml
        │   ├── messages.fxml
        │   ├── AjoutClient.fxml
        │   ├── AjoutUser.fxml
        │   ├── modifierSupprimerClients.fxml
        │   └── modifierSupprimerUser.fxml
        └── styles/
            ├── login.css
            └── dashboard.css
            
Dévéloppé avec rigueur dans un objectif de performance, de traçabilité et de sécurité des transactions.
