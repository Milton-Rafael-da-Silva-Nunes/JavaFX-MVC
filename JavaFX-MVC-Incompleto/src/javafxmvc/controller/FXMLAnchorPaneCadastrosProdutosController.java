package javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafxmvc.model.dao.CategoriaDAO;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Categoria;
import javafxmvc.model.domain.Produto;

/**
 *
 * @author Rafael Nunes
 */
public class FXMLAnchorPaneCadastrosProdutosController implements Initializable {

    @FXML
    private TableView tableViewProdutos;
    @FXML
    private TableColumn<Produto, Integer> tableColumnProdutoCodigo;
    @FXML
    private TableColumn<Produto, String> tableColumnProdutoNome;
    @FXML
    private TableColumn<Produto, Double> tableColumnProdutoPreco;
    @FXML
    private TableColumn<Produto, Double> tableColumnProdutoEstoque;
    @FXML
    private Label labelProdutoCodigo;
    @FXML
    private Label labelProdutoNome;
    @FXML
    private Label labelProdutoPreco;
    @FXML
    private Label labelProdutoEstoque;
    @FXML
    private Label labelProdutoCategoriaNome;
    @FXML
    private Button buttonInserir;
    @FXML
    private Button buttonAlterar;
    @FXML
    private Button buttonRemover;

    private List<Produto> listProdutos;
    private ObservableList<Produto> observableListProdutos;

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        produtoDAO.setConnection(connection);
        categoriaDAO.setConnection(connection);
        carregarTableViewProdutos();

        tableViewProdutos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewProduto((Produto) newValue));

    }

    private void carregarTableViewProdutos() {
        tableColumnProdutoCodigo.setCellValueFactory(new PropertyValueFactory<>("cdProduto"));
        tableColumnProdutoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnProdutoPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        tableColumnProdutoEstoque.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        listProdutos = produtoDAO.listar();
        observableListProdutos = FXCollections.observableArrayList(listProdutos);

        tableViewProdutos.setItems(observableListProdutos);
    }

    private void selecionarItemTableViewProduto(Produto produto) {
        if (produto != null) {
            labelProdutoCodigo.setText(String.valueOf(produto.getCdProduto()));
            labelProdutoNome.setText(produto.getNome());
            labelProdutoPreco.setText(String.valueOf(produto.getPreco()));
            labelProdutoEstoque.setText(String.valueOf(produto.getQuantidade()));
            labelProdutoCategoriaNome.setText(produto.getCategoria().getDescricao());
        } else {
            labelProdutoCodigo.setText("");
            labelProdutoNome.setText("");
            labelProdutoPreco.setText("");
            labelProdutoEstoque.setText("");
            labelProdutoCategoriaNome.setText("");
        }
    }

    @FXML
    public void hundleButtonInserir() throws IOException {
        Produto produto = new Produto();
        List<Categoria> categorias = categoriaDAO.listar();
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosProdutosDialog(produto, categorias);
        if (buttonConfirmarClicked) {
            produtoDAO.inserir(produto);
            carregarTableViewProdutos();
        }
    }

    @FXML
    public void hundleButtonAlterar() throws IOException {
        Produto produto = (Produto) tableViewProdutos.getSelectionModel().getSelectedItem();
        List<Categoria> categorias = categoriaDAO.listar();
        if (produto != null && categorias != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosProdutosDialog(produto, categorias);
            if (buttonConfirmarClicked) {
                produtoDAO.alterar(produto);
                carregarTableViewProdutos();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um Produto na Tabela!");
            alert.show();
        }
    }

    @FXML
    public void hundleButtonRemover() throws IOException {
        Produto produto = (Produto) tableViewProdutos.getSelectionModel().getSelectedItem();
        if (produto != null) {
            produtoDAO.remover(produto);
            carregarTableViewProdutos();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um produto na Tabela!");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastrosProdutosDialog(Produto produto, List<Categoria> categorias) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastrosClientesDialogController.class.getResource("/javafxmvc/view/FXMLAnchorPaneCadastrosProdutosDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Produtos");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Setando o cliente no controller
        FXMLAnchorPaneCadastrosProdutosDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setProduto(produto);
        controller.setCategorias(categorias);

        // Abre a tela de alteração e espera o usuario fechar.
        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();
    }
}
