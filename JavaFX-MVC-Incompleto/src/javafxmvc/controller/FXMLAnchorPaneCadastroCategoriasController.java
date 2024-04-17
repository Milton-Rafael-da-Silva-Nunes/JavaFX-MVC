package javafxmvc.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    
    private List<Categoria> listCategorias;
    private ObservableList<Categoria> observableListCategorias;
    
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoriaDAO.setConnection(connection);
        carregarTableViewCategorias();
    }    
    
    public void carregarTableViewCategorias() {
        tableColumnCategoriaCodigo.setCellValueFactory(new PropertyValueFactory<>("cdCategoria"));
        tableColumnCategoriaNome.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        
        listCategorias = categoriaDAO.listar();
        
        observableListCategorias = FXCollections.observableArrayList(listCategorias);
        tableViewCategorias.setItems(observableListCategorias);
    } 
}
