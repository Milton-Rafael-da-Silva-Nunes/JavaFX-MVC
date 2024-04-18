package javafxmvc.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxmvc.model.domain.Produto;

/**
 *
 * @author Rafael Nunes
 */
public class FXMLAnchorPaneCadastrosProdutosDialogController implements Initializable {

    @FXML
    private Button buttonConfirmar;
    @FXML
    private Button buttonCancelar;
    @FXML
    private TextField textFielProdutoNome;
    @FXML
    private TextField textFielProdutoPreco;
    @FXML
    private TextField textFielProdutoEstoque;
    @FXML
    private TextField textFielProdutoCategoriaNome;
    
    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Produto produto;    
    
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

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
    
    @FXML
    public void hundleButtonConfirmar() {
        produto.setNome(textFielProdutoNome.getText());
        produto.setPreco(textFielProdutoPreco.getTranslateX());
        produto.setQuantidade(Integer.valueOf(textFielProdutoEstoque.getText()));
        
        
    }
    
    @FXML
    public void hundleButtonCancelar() {
        dialogStage.close();
    }
    
}
