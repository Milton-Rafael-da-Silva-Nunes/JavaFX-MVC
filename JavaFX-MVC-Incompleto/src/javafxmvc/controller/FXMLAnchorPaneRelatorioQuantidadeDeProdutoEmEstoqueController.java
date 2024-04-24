package javafxmvc.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Produto;

/**
 *
 * @author Rafael Nunes
 */
public class FXMLAnchorPaneRelatorioQuantidadeDeProdutoEmEstoqueController implements Initializable {

    @FXML
    private TableView<Produto> tableViewProdutos;
    @FXML
    private TableColumn<Produto, String> tableViewColumnProduto;
    @FXML
    private TableColumn<Produto, Integer> tableViewColumnProdutoSaldo;
    @FXML
    private GridPane gridPaneResumo;
    @FXML
    private Label labelQtdeProdutos;
    @FXML
    private Label labelQtdeEstoque;
    @FXML
    private Label labelValorEstoque;

    private List<Produto> listProdutos;
    private ObservableList observableListProdutos;

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        produtoDAO.setConnection(connection);
        CarregarTableViewProdutos();
    }

    private void CarregarTableViewProdutos() {
        listProdutos = produtoDAO.listar();

        List<Produto> produtosEmEstoque = new ArrayList<>();
        int sumQtdeProdutos = 0;
        int sumQtdeEstoques = 0;
        double sumValorEstques = 0;

        for (Produto produto : listProdutos) {
            if (produto.getQuantidade() > 0) {
                sumQtdeProdutos += 1;
                sumQtdeEstoques += produto.getQuantidade();
                sumValorEstques += (produto.getPreco() * produto.getQuantidade());
                produtosEmEstoque.add(produto);
            }
        }
        
        labelQtdeProdutos.setText(String.valueOf(sumQtdeProdutos));
        labelQtdeEstoque.setText(String.valueOf(sumQtdeEstoques));
        labelValorEstoque.setText(String.format("%.2f", sumValorEstques));

        if (!produtosEmEstoque.isEmpty()) {
            tableViewColumnProduto.setCellValueFactory(new PropertyValueFactory<>("nome"));
            tableViewColumnProdutoSaldo.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

            observableListProdutos = FXCollections.observableArrayList(listProdutos);
            tableViewProdutos.setItems(observableListProdutos);
        }
    }
}
