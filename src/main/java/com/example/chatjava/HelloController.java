package com.example.chatjava;

import com.example.chatjava.logic.CommunicationManager;
import com.example.chatjava.model.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.Socket;

public class HelloController {
    @FXML
    private Button enviarBtn;

    @FXML
    private Button enviarContr;

    @FXML
    private TextField portIn;

    @FXML
    private TextField ipIn;

    @FXML
    private TextField nameIn;

    @FXML
    private TextField textoIn;

    @FXML
    private VBox cajaV;

    @FXML
    private VBox boxmensa;
    @FXML
    private ScrollPane containMessag;

    private String name;
    private CommunicationManager communicationManager;



    @FXML
    protected void onConnectButtonClick() {
        try {
            String IP = ipIn.getText();
            System.out.println(IP);
            int port = Integer.parseInt(portIn.getText());
            name = nameIn.getText();

            Socket socket = new Socket(IP, port);
            communicationManager = new CommunicationManager(socket);
            Thread thread = new Thread(communicationManager);
            thread.setDaemon(true);
            thread.start();

            ipIn.setEditable(false);
            portIn.setEditable(false);
            nameIn.setEditable(false);
            enviarBtn.setDisable(false);
        } catch (IOException e) {
            // Manejar la excepción de manera adecuada, por ejemplo, mostrar un mensaje de error al usuario
            e.printStackTrace();
        }
    }
    @FXML
    protected void setSendButtonClick() {
        if (communicationManager != null) {
            String messageText = textoIn.getText().trim();

            if (!messageText.isEmpty()) {
                communicationManager.send(new Message(name, messageText));

                // Actualizar la interfaz de usuario
                Platform.runLater(() -> {
                    Label label = new Label(name + ": " + messageText);
                    VBox messageContainer = new VBox(label);
                    messageContainer.setAlignment(Pos.CENTER_RIGHT);

                    // Agregar el nuevo mensaje a boxmensa
                    boxmensa.getChildren().add(messageContainer);

                    textoIn.setText("");
                });
            }
        }
    }


    public void receive(Message message) {
        Platform.runLater(() -> {
            Label label = new Label(message.toString());
            HBox hBox = new HBox(label);
            hBox.setAlignment(Pos.CENTER_LEFT);
            cajaV.getChildren().add(hBox);
        });
    }

    public void remove() {
        communicationManager = null;
        ipIn.setEditable(true);
        portIn.setEditable(true);
        nameIn.setEditable(true);
        enviarBtn.setDisable(true);
    }
}