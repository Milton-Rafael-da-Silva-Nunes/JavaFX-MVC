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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxmvc.model.dao.CategoriaDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Categoria;

/**
 *
 * @author Rafael Nunes
 */
public class FXMLAnchorPaneCadastroCategoriasController implements Initializable {

    @FXML
    private TableView<Categoria> tableViewCategorias;
    @FXML
    private TableColumn<Categoria, String> tableColumnCategoriaCodigo;
    @FXML
    private TableColumn<Categoria, String> tableColumnCategoriaNome;
    @FXML
    private TextField textFielCategoriaNome;
    @FXML
    private Button buttonInserir;
    @FXML
    private Button buttonAlterar;
    @FXML
    private Button buttonRemover;

    private List<Categoria> listCategorias;
    private ObservableList<Categoria> observableListCategorias;

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoriaDAO.setConnection(connection);
        carregarTableViewCategorias();
        gridPaneCategoria.setVisible(false);
    }

    public void carregarTableViewCategorias() {
        tableColumnCategoriaCodigo.setCellValueFactory(new PropertyValueFactory<>("cdCategoria"));
        tableColumnCategoriaNome.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        listCategorias = categoriaDAO.listar();

        observableListCategorias = FXCollections.observableArrayList(listCategorias);
        tableViewCategorias.setItems(observableListCategorias);
    }

    @FXML
    public void hundleButtonInserir() {

    }

    @FXML
    public void hundleButtonAlerar() {
        
    }

    @FXML
    public void hundleButtonRemover() {
        Categoria categoria = tableViewCategorias.getSelectionModel().getSelectedItem();
        if (categoria != null) {
            categoriaDAO.remover(categoria);
            carregarTableViewCategorias();
        }
    }

    

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private boolean validarDadosNovaCategoria() {
        String errorMessage = "";
        String nomeNovaCategoria = textFielCategoriaNome.getText();

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
