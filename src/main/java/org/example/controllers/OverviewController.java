package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.models.DatabaseConnection;
import org.example.models.User;
import org.example.models.UserSession;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class OverviewController {

    @FXML
    private Label demandesCount;

    @FXML
    private Label clientsCount;

    @FXML
    private Label vouchersCount;

    @FXML
    private Label enAttentePaiement;

    @FXML
    private Label enAttenteApprobation;

    @FXML
    private Label vouchersExpires;

    @FXML
    private Label welcomeMessage;

    @FXML
    public void initialize() {
        // Message de bienvenue personnalisé
        User currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser != null) {
            welcomeMessage.setText("👋 Bienvenue " + currentUser.getPrenom() + " " + currentUser.getNom() + " !");
        }

        // Charger les statistiques
        loadStatistics();
    }

    private void loadStatistics() {
        try (Connection conn = DatabaseConnection.getConnection()) {

            // Compter le nombre de demandes (à adapter selon ta table)
            // Remplace "demandes" par le vrai nom de ta table
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as total FROM demandes");
                if (rs.next()) {
                    demandesCount.setText(String.valueOf(rs.getInt("total")));
                }
            } catch (Exception e) {
                // Si la table n'existe pas encore, on met 0
                demandesCount.setText("0");
            }

            // Compter le nombre de clients
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as total FROM client");
                if (rs.next()) {
                    clientsCount.setText(String.valueOf(rs.getInt("total")));
                }
            } catch (Exception e) {
                clientsCount.setText("0");
            }

            // Nombre de vouchers (pour l'instant 0 comme tu as dit)
            vouchersCount.setText("0");

            // Stats supplémentaires (à adapter plus tard)
            enAttentePaiement.setText("0");
            enAttenteApprobation.setText("0");
            vouchersExpires.setText("0");

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des statistiques : " + e.getMessage());
            // Mettre des valeurs par défaut en cas d'erreur
            demandesCount.setText("0");
            clientsCount.setText("0");
            vouchersCount.setText("0");
        }
    }
}
