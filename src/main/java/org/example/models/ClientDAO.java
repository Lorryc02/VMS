package org.example.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    // Récupérer tous les clients
    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT refclient, nomclient, email, adresse, tel FROM client ORDER BY nomclient";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Client client = new Client(
                        rs.getInt("refclient"),
                        rs.getString("nomclient"),
                        rs.getString("email"),
                        rs.getString("adresse"),
                        rs.getInt("tel")
                );
                clients.add(client);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des clients: " + e.getMessage());
        }
        return clients;
    }

    // Rechercher des clients par nom ou téléphone
    public List<Client> searchClients(String searchText) {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT refclient, nomclient, email, adresse, tel FROM client " +
                "WHERE LOWER(nomclient) LIKE LOWER(?) OR CAST(tel AS TEXT) LIKE ? " +
                "ORDER BY nomclient";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            String searchPattern = "%" + searchText + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Client client = new Client(
                        rs.getInt("refclient"),
                        rs.getString("nomclient"),
                        rs.getString("email"),
                        rs.getString("adresse"),
                        rs.getInt("tel")
                );
                clients.add(client);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche: " + e.getMessage());
        }
        return clients;
    }

    // Ajouter un nouveau client
    public boolean addClient(String nom, String email, String adresse, int tel) {
        String query = "INSERT INTO client (nomclient, email, adresse, tel) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nom);
            pstmt.setString(2, email);
            pstmt.setString(3, adresse);
            pstmt.setInt(4, tel);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du client: " + e.getMessage());
            return false;
        }
    }

    // Récupérer un client par son ID
    public Client getClientById(int refClient) {
        String query = "SELECT refclient, nomclient, email, adresse, tel FROM client WHERE refclient = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, refClient);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Client(
                        rs.getInt("refclient"),
                        rs.getString("nomclient"),
                        rs.getString("email"),
                        rs.getString("adresse"),
                        rs.getInt("tel")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du client: " + e.getMessage());
        }
        return null;
    }
}