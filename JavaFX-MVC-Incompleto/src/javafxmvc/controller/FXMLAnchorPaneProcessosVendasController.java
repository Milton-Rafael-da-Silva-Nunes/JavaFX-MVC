package javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafxmvc.model.dao.ClienteDAO;
import javafxmvc.model.dao.ItemDeVendaDAO;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.dao.VendaDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.ItemDeVenda;
import javafxmvc.model.domain.Produto;
import javafxmvc.model.domain.Venda;

/**
 *
 * @author Rafael Nunes
 */
public class FXMLAnchorPaneProcessosVendasController implements Initializable {

    @FXML
    private TableView<Venda> tableViewVendas;
    @FXML
    private TableColumn<Venda, Integer> tableColumnVendaCodigo;
    @FXML
    private TableColumn<Venda, LocalDate> tableColumnVendaData;
    @FXML
    private TableColumn<Venda, Venda> tableColumnVendaCliente;
    @FXML
    private Label labelVendaCodigo;
    @FXML
    private Label labelVendaData;
    @FXML
    private Label labelVendaValor;
    @FXML
    private Label labelVendaPago;
    @FXML
    private Label labelVendaCliente;
    @FXML
    private Button buttonInserir;
    @FXML
    private Button buttonAlterar;
    @FXML
    private Button buttonRemover;

    private List<Venda> listVendas;
    private ObservableList observableListVendas;

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final VendaDAO vendaDAO = new VendaDAO();
    private final ItemDeVendaDAO itemDeVendaDAO = new ItemDeVendaDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteDAO.setConnection(connection);
        vendaDAO.setConnection(connection);
        carregarTableViewVendas();

        tableViewVendas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewProduto(newValue));
    }

    @FXML
    public void hundleButtonInserir() throws IOException {
        Venda venda = new Venda();
        List<ItemDeVenda> listItensDeVenda = new ArrayList<>();
        venda.setItensDeVenda(listItensDeVenda);

        boolean buttonConfirmarClicked = showFXMLAnchorPaneProcessosVendasDialog(venda);
        if (buttonConfirmarClicked) {

            try {
                connection.setAutoCommit(false);
                vendaDAO.setConnection(connection);
                vendaDAO.inserir(venda);
                itemDeVendaDAO.setConnection(connection);
                produtoDAO.setConnection(connection);

                for (ItemDeVenda itemDeVenda : venda.getItensDeVenda()) {
                    Produto produto = itemDeVenda.getProduto();
                    itemDeVenda.setVenda(vendaDAO.buscarUltimaVenda());
                    itemDeVendaDAO.inserir(itemDeVenda);
                    produto.setQuantidade(produto.getQuantidade() - itemDeVenda.getQuantidade());
                    produtoDAO.alterar(produto);
                }

                connection.commit();
                carregarTableViewVendas();

            } catch (SQLException ex) {
                try {
                    connection.rollback();
                } catch (SQLException ex1) {
                    Logger.getLogger(FXMLAnchorPaneProcessosVendasController.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(FXMLAnchorPaneProcessosVendasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    public void hundleButtonRemover() {
        Venda venda = tableViewVendas.getSelectionModel().getSelectedItem();
        if (venda != null) {
            try {
                connection.setAutoCommit(false);
                vendaDAO.setConnection(connection);
                itemDeVendaDAO.setConnection(connection);
                produtoDAO.setConnection(connection);

                for (ItemDeVenda itemDeVenda : venda.getItensDeVenda()) {
                    Produto produto = itemDeVenda.getProduto();
                    produto.setQuantidade(produto.getQuantidade() + itemDeVenda.getQuantidade());
                    produtoDAO.alterar(produto);
                    itemDeVendaDAO.remover(itemDeVenda);
                }

                vendaDAO.remover(venda);
                connection.commit();
                carregarTableViewVendas();

            } catch (SQLException ex) {
                Logger.getLogger(FXMLAnchorPaneProcessosVendasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha uma venda na tabela ao lado!");
            alert.show();
        }
    }

    private void carregarTableViewVendas() {
        tableColumnVendaCodigo.setCellValueFactory(new PropertyValueFactory<>("cdVenda"));
        tableColumnVendaData.setCellValueFactory(new PropertyValueFactory<>("data"));
        tableColumnVendaCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));

        listVendas = vendaDAO.listar();

        observableListVendas = FXCollections.observableArrayList(listVendas);
        tableViewVendas.setItems(observableListVendas);
    }

    private void selecionarItemTableViewProduto(Venda venda) {
        if (venda != null) {
            labelVendaCodigo.setText(String.valueOf(venda.getCdVenda()));
            labelVendaData.setText(String.valueOf(venda.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            labelVendaPago.setText(getStatusVenda(venda.getPago()));
            labelVendaValor.setText(String.format("%.2f", venda.getValor()));
            labelVendaCliente.setText(venda.getCliente().getNome());
        } else {
            labelVendaCodigo.setText("");
            labelVendaData.setText("");
            labelVendaPago.setText("");
            labelVendaValor.setText("");
            labelVendaCliente.setText("");
        }
    }

    private String getStatusVenda(boolean status) {
        if (status) {
            return "Sim";
        } else {
            return "Não";
        }
    }

    private boolean showFXMLAnchorPaneProcessosVendasDialog(Venda venda) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneProcessosVendasController.class.getResource("/javafxmvc/view/FXMLAnchorPaneProcessosVendasDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Registro de Vendas");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Setando a venda no controller
        FXMLAnchorPaneProcessosVendasDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setVenda(venda);

        // Abre a tela de alteração e espera o usuario fechar.
        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();
    }
}
