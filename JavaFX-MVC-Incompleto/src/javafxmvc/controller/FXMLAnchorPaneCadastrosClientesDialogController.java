package javafxmvc.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxmvc.model.domain.Cliente;

/**
 *
 * @author Rafael Nunes
 */
public class FXMLAnchorPaneCadastrosClientesDialogController implements Initializable {

    @FXML
    private Label labelClienteNome;
    @FXML
    private Label labelClienteCPF;
    @FXML
    private Label labelClienteTelefone;
    @FXML
    private TextField textFieldClienteNome;
    @FXML
    private TextField textFieldClienteCPF;
    @FXML
    private TextField textFieldClienteTelefone;
    @FXML
    private Button buttonConfirmar;
    @FXML
    private Button buttonCancelar;

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Cliente cliente;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        this.textFieldClienteNome.setText(cliente.getNome());
        this.textFieldClienteCPF.setText(cliente.getCpf());
        this.textFieldClienteTelefone.setText(cliente.getTelefone());
    }

    @FXML
    public void handleButtonConfirmar() {
        if (validarEntradaDeDadosCliente()) {
            
            cliente.setNome(textFieldClienteNome.getText());
            cliente.setCpf(textFieldClienteCPF.getText());
            cliente.setTelefone(textFieldClienteTelefone.getText());

            buttonConfirmarClicked = true;

            dialogStage.close();
        }
    }

    @FXML
    public void handleButtonCancelar() {
        dialogStage.close();
    }

    private boolean validarEntradaDeDadosCliente() {
        String errorMessage = "";
        String nome = textFieldClienteNome.getText();
        String cpf = textFieldClienteCPF.getText();
        String telefone = textFieldClienteTelefone.getText();

        if (nome == null || nome.length() == 0) {
            errorMessage += "Digite o nome do cliente!\n";
        }
        if (cpf == null || cpf.length() == 0) {
            errorMessage += "Digite o CPF do cliente!\n";
        }
        if (telefone == null || telefone.length() == 0) {
            errorMessage += "Digite um Telefone para contato!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setContentText("Campos invalidos, por favor, corrija-os...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
}
