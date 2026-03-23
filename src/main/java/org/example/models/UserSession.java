package org.example.models;

/**
 * Classe Singleton pour stocker l'utilisateur connecté durant toute la session
 */
public class UserSession {

    private static UserSession instance;
    private User currentUser;

    // Constructeur privé (Singleton)
    private UserSession() {}

    // Récupérer l'instance unique
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // Stocker l'utilisateur connecté
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    // Récupérer l'utilisateur connecté
    public User getCurrentUser() {
        return currentUser;
    }

    // Vérifier si quelqu'un est connecté
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    // Se déconnecter (effacer la session)
    public void clearSession() {
        this.currentUser = null;
    }
}