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
import javafxmvc.model.domain.Categoria;

/**
 * FXML Controller class
 *
 * @author Rafael Nunes
 */
public class FXMLAnchorPaneCadastrosCategoriasDialogController implements Initializable {

    @FXML
    private Label gridLabelCategoriaCodigo;
    @FXML
    private TextField gridTextFieldCategoriaDescricao;
    @FXML
    private Button buttonConfirmar;
    @FXML
    private Button buttonCancelar;

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Categoria categoria;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
        if (categoria.getCdCategoria() == 0) {
            this.gridLabelCategoriaCodigo.setText("");
        } else {
            this.gridLabelCategoriaCodigo.setText(String.valueOf(categoria.getCdCategoria()));
            this.gridTextFieldCategoriaDescricao.setText(categoria.getDescricao());
        }
    }

    @FXML
    public void hundleButtonConfirmar() {
        if (validarDadosNovaCategoria()) {
            categoria.setDescricao(gridTextFieldCategoriaDescricao.getText());

            buttonConfirmarClicked = true;

            dialogStage.close();
        }
    }

    @FXML
    public void hundleButtonCancelar() {
        dialogStage.close();
    }

    private boolean validarDadosNovaCategoria() {
        String errorMessage = "";
        String nomeNovaCategoria = gridTextFieldCategoriaDescricao.getText();

        if (nomeNovaCategoria == null || nomeNovaCategoria.length() == 0) {
            errorMessage += "Digite o nome da Categoria!\n";
        }

        if (errorMessage.isEmpty()) {
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
