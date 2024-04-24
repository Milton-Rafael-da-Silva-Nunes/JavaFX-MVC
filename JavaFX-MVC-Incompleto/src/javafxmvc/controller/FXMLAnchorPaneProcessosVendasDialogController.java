package javafxmvc.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafxmvc.model.dao.ClienteDAO;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Cliente;
import javafxmvc.model.domain.ItemDeVenda;
import javafxmvc.model.domain.Produto;
import javafxmvc.model.domain.Venda;

/**
 *
 * @author Rafael Nunes
 */
public class FXMLAnchorPaneProcessosVendasDialogController implements Initializable {

    @FXML
    private ComboBox comboBoxVendaCliente;
    @FXML
    private ComboBox comboBoxVendaProdutos;
    @FXML
    private DatePicker datePickerVendaData;
    @FXML
    private CheckBox checkBoxVendaPago;
    @FXML
    private TextField textFieldItemDeVendaQuantidade;
    @FXML
    private Button buttonAdicionar;
    @FXML
    private TableView<ItemDeVenda> tableViewItensDeVenda;
    @FXML
    private TableColumn<ItemDeVenda, Produto> tableColumnItemDeVendaProduto;
    @FXML
    private TableColumn<ItemDeVenda, Integer> tableColumnItemDeVendaQuantidade;
    @FXML
    private TableColumn<ItemDeVenda, Double> tableColumnItemDeVendaValor;
    @FXML
    private TextField textFieldVendaTotal;
    @FXML
    private Button buttonConfirmar;
    @FXML
    private Button buttonCancelar;

    private List<Cliente> listClientes;
    private List<Produto> listProdutos;
    private ObservableList<Cliente> observableListClientes;
    private ObservableList<Produto> observableListProdutos;
    private ObservableList<ItemDeVenda> observableListItensDeVenda;

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Venda venda;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteDAO.setConnection(connection);
        produtoDAO.setConnection(connection);
        carregarComboBoxClientes();
        carregarComboBoxProdutos();
        tableColumnItemDeVendaProduto.setCellValueFactory(new PropertyValueFactory<>("produto"));
        tableColumnItemDeVendaQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        tableColumnItemDeVendaValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
    }

    @FXML
    public void hundleButtonAdicionarProduto() {
        Produto produto;
        ItemDeVenda itemDeVenda = new ItemDeVenda();
        if (comboBoxVendaProdutos.getSelectionModel().getSelectedItem() != null) {
            produto = (Produto) comboBoxVendaProdutos.getSelectionModel().getSelectedItem();
            if (produto.getQuantidade() >= Integer.parseInt(textFieldItemDeVendaQuantidade.getText())) {
                itemDeVenda.setProduto((Produto) comboBoxVendaProdutos.getSelectionModel().getSelectedItem());
                itemDeVenda.setQuantidade(Integer.parseInt(textFieldItemDeVendaQuantidade.getText()));
                itemDeVenda.setValor(itemDeVenda.getProduto().getPreco() * itemDeVenda.getQuantidade());
                venda.getItensDeVenda().add(itemDeVenda);
                venda.setValor(venda.getValor() + itemDeVenda.getValor());
                observableListItensDeVenda = FXCollections.observableArrayList(venda.getItensDeVenda());
                tableViewItensDeVenda.setItems(observableListItensDeVenda);
                textFieldVendaTotal.setText(String.format("%.2f", venda.getValor()));
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Problema na escolha do Produto!");
                alert.setContentText("Não axiste a quantidade de produtos disponivel em estoque.");
                alert.show();
            }
        }
    }

    @FXML
    public void hundleButtonConfirmar() {
        if(validarEntradaDeDadosVenda()) {
            venda.setCliente((Cliente) comboBoxVendaCliente.getSelectionModel().getSelectedItem());
            venda.setPago(checkBoxVendaPago.isSelected());
            venda.setData(datePickerVendaData.getValue());
            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    public void hundleButtonCancelar() {
        dialogStage.close();
    }

    private void carregarComboBoxClientes() {
        listClientes = clienteDAO.listar();
        observableListClientes = FXCollections.observableArrayList(listClientes);
        comboBoxVendaCliente.setItems(observableListClientes);
    }

    private void carregarComboBoxProdutos() {
        listProdutos = produtoDAO.listar();
        observableListProdutos = FXCollections.observableArrayList(listProdutos);
        comboBoxVendaProdutos.setItems(observableListProdutos);
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

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }
    
    private boolean validarEntradaDeDadosVenda() {
        String errorMessage = "";
        
        if (comboBoxVendaCliente.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Cliente inválido!\n";
        }
        if (datePickerVendaData.getValue() == null) {
            errorMessage += "Data da venda inválida!\n";
        }
        if (observableListItensDeVenda == null) {
            errorMessage += "Itens de Venda inválidos!\n";
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
