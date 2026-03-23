package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.models.Client;
import org.example.models.ClientDAO;

import java.util.List;

public class InitierController {

    @FXML
    private TextField clientNameField;

    @FXML
    private TextField voucherValueField;

    @FXML
    private TextField voucherCountField;

    @FXML
    private DatePicker validityDatePicker;

    @FXML
    private Button submitButton;

    @FXML
    private Button dropdownButton;

    @FXML
    private Button addClientButton;

    @FXML
    private Button searchButton;

    private ClientDAO clientDAO;
    private Client selectedClient;

    @FXML
    public void initialize() {
        clientDAO = new ClientDAO();

        submitButton.setOnAction(event -> createVoucherRequest());
        dropdownButton.setOnAction(event -> showClientDropdown());
        addClientButton.setOnAction(event -> showAddClientModal());
        searchButton.setOnAction(event -> showSearchModal());
    }

    // Afficher le dropdown avec tous les clients
    private void showClientDropdown() {
        List<Client> clients = clientDAO.getAllClients();

        if (clients.isEmpty()) {
            showAlert("Aucun client", "Aucun client trouvé dans la base de données.");
            return;
        }

        Stage dropdownStage = new Stage();
        dropdownStage.initModality(Modality.APPLICATION_MODAL);
        dropdownStage.setTitle("Sélectionner un client");

        ListView<String> listView = new ListView<>();
        ObservableList<String> clientItems = FXCollections.observableArrayList();

        for (Client c : clients) {
            clientItems.add(c.getNomClient() + " - " + c.getTel());
        }
        listView.setItems(clientItems);

        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                int index = listView.getSelectionModel().getSelectedIndex();
                if (index >= 0) {
                    selectedClient = clients.get(index);
                    clientNameField.setText(selectedClient.getNomClient());
                    dropdownStage.close();
                }
            }
        });

        VBox vbox = new VBox(10, new Label("Double-cliquez pour sélectionner:"), listView);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 400, 300);
        dropdownStage.setScene(scene);
        dropdownStage.showAndWait();
    }

    // Modal pour ajouter un nouveau client
    private void showAddClientModal() {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Ajouter un nouveau client");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nomField = new TextField();
        TextField emailField = new TextField();
        TextField adresseField = new TextField();
        TextField telField = new TextField();

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Adresse:"), 0, 2);
        grid.add(adresseField, 1, 2);
        grid.add(new Label("Téléphone:"), 0, 3);
        grid.add(telField, 1, 3);

        Button saveButton = new Button("Enregistrer");
        Button cancelButton = new Button("Annuler");

        saveButton.setOnAction(event -> {
            try {
                String nom = nomField.getText().trim();
                String email = emailField.getText().trim();
                String adresse = adresseField.getText().trim();
                int tel = Integer.parseInt(telField.getText().trim());

                if (nom.isEmpty() || email.isEmpty()) {
                    showAlert("Champs requis", "Le nom et l'email sont obligatoires.");
                    return;
                }

                boolean success = clientDAO.addClient(nom, email, adresse, tel);

                if (success) {
                    showAlert("Succès", "Client ajouté avec succès!");
                    modalStage.close();
                } else {
                    showAlert("Erreur", "Impossible d'ajouter le client.");
                }
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Le numéro de téléphone doit être un nombre valide.");
            }
        });

        cancelButton.setOnAction(event -> modalStage.close());

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);
        grid.add(buttonBox, 0, 4, 2, 1);

        Scene scene = new Scene(grid, 400, 250);
        modalStage.setScene(scene);
        modalStage.showAndWait();
    }

    // Modal de recherche avec filtre
    private void showSearchModal() {
        Stage searchStage = new Stage();
        searchStage.initModality(Modality.APPLICATION_MODAL);
        searchStage.setTitle("Rechercher un client");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        TextField searchField = new TextField();
        searchField.setPromptText("Entrez un nom ou numéro de téléphone...");

        ListView<String> resultListView = new ListView<>();
        Label resultLabel = new Label("Tapez pour rechercher");

        // Liste pour stocker les clients trouvés
        final List<Client>[] searchResults = new List[]{null};

        searchField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.trim().isEmpty()) {
                resultListView.getItems().clear();
                resultLabel.setText("Tapez pour rechercher");
                return;
            }

            searchResults[0] = clientDAO.searchClients(newText);
            ObservableList<String> items = FXCollections.observableArrayList();

            for (Client c : searchResults[0]) {
                items.add(c.getNomClient() + " - Tél: " + c.getTel());
            }

            resultListView.setItems(items);
            resultLabel.setText(searchResults[0].size() + " résultat(s) trouvé(s)");
        });

        resultListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && searchResults[0] != null) {
                int index = resultListView.getSelectionModel().getSelectedIndex();
                if (index >= 0) {
                    selectedClient = searchResults[0].get(index);
                    clientNameField.setText(selectedClient.getNomClient());
                    searchStage.close();
                }
            }
        });

        vbox.getChildren().addAll(
                new Label("Rechercher:"),
                searchField,
                resultLabel,
                resultListView
        );

        Scene scene = new Scene(vbox, 450, 350);
        searchStage.setScene(scene);
        searchStage.showAndWait();
    }

    private void createVoucherRequest() {
        String client = clientNameField.getText();

        if (client.isEmpty() || voucherValueField.getText().isEmpty() ||
                voucherCountField.getText().isEmpty() || validityDatePicker.getValue() == null) {
            showAlert("Champs requis", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            int value = Integer.parseInt(voucherValueField.getText());
            int count = Integer.parseInt(voucherCountField.getText());
            var date = validityDatePicker.getValue();

            // TODO : envoyer en base de données
            System.out.println("Client : " + client);
            System.out.println("Valeur : " + value);
            System.out.println("Nombre : " + count);
            System.out.println("Validité : " + date);

            showAlert("Succès", "Demande créée avec succès!");

        } catch (NumberFormatException e) {
            showAlert("Erreur", "La valeur et le nombre doivent être des nombres valides.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}