package javafxmvc.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxmvc.model.dao.CategoriaDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Categoria;
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
    private TextField textFieldProdutoNome;
    @FXML
    private TextField textFieldProdutoPreco;
    @FXML
    private TextField textFieldProdutoEstoque;
    @FXML
    private ComboBox<Categoria> comboBoxCategoriaNome;

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Produto produto;
    private List<Categoria> listCategorias;
    
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoriaDAO.setConnection(connection);
        carregarCategoriasComboBox();
    }

    private void carregarCategoriasComboBox() {
        listCategorias = categoriaDAO.listar();
        comboBoxCategoriaNome.setItems(FXCollections.observableArrayList(listCategorias));
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
        this.textFieldProdutoNome.setText(produto.getNome());
        this.textFieldProdutoPreco.setText(String.valueOf(produto.getPreco()));
        this.textFieldProdutoEstoque.setText(String.valueOf(produto.getQuantidade()));
        comboBoxCategoriaNome.getSelectionModel().select(produto.getCategoria());
    }

    @FXML
    public void hundleButtonConfirmar() {
        if (validarEntradaDeDadosProdutos()) {
            
            produto.setNome(textFieldProdutoNome.getText());
            produto.setPreco(Double.valueOf(textFieldProdutoPreco.getText()));
            produto.setQuantidade(Integer.valueOf(textFieldProdutoEstoque.getText()));
            produto.setCategoria(comboBoxCategoriaNome.getValue());

            buttonConfirmarClicked = true;

            dialogStage.close();
        }
    }

    @FXML
    public void hundleButtonCancelar() {
        dialogStage.close();
    }

    private boolean validarEntradaDeDadosProdutos() {
        String errorMessage = "";
        String nome = textFieldProdutoNome.getText();
        String preco = textFieldProdutoPreco.getText();
        String estoque = textFieldProdutoEstoque.getText();
        Categoria categoriaSelecionada = comboBoxCategoriaNome.getSelectionModel().getSelectedItem();

        if (nome == null || nome.length() == 0) {
            errorMessage += "Digite o Nome do produto!\n";
        }
        if (preco.isEmpty() || preco.equals("0.0")) {
            errorMessage += "Digite o Preço do produto!\n";
        }
        if (estoque == null || estoque.length() == 0) {
            errorMessage += "Adicione o estoque do produto!\n";
        }
        if(categoriaSelecionada == null) {
            errorMessage += "Selecione uma Categoria para o produto!\n";
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
