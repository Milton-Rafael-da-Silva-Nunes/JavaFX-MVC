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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
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
    }

    public void carregarTableViewCategorias() {
        tableColumnCategoriaCodigo.setCellValueFactory(new PropertyValueFactory<>("cdCategoria"));
        tableColumnCategoriaNome.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        listCategorias = categoriaDAO.listar();

        observableListCategorias = FXCollections.observableArrayList(listCategorias);
        tableViewCategorias.setItems(observableListCategorias);
    }

    @FXML
    public void hundleButtonInserir() throws IOException {
        Categoria categoria = new Categoria();
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosClientesDialog(categoria);
        if(buttonConfirmarClicked) {
            categoriaDAO.inserir(categoria);
            carregarTableViewCategorias();
        }
    }

    @FXML
    public void hundleButtonAlerar() throws IOException {
        Categoria categoria = tableViewCategorias.getSelectionModel().getSelectedItem();
        if(categoria != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosClientesDialog(categoria);
            if(buttonConfirmarClicked) {
                categoriaDAO.alterar(categoria);
                carregarTableViewCategorias();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha uma categoria na Tabela!");
            alert.show();
        }
    }

    @FXML
    public void hundleButtonRemover() {
        Categoria categoria = tableViewCategorias.getSelectionModel().getSelectedItem();
        if (categoria != null) {
            categoriaDAO.remover(categoria);
            carregarTableViewCategorias();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha uma categoria na Tabela!");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastrosClientesDialog(Categoria categoria) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroCategoriasController.class.getResource("/javafxmvc/view/FXMLAnchorPaneCadastrosCategoriasDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Categorias");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        // Setando o cliente no controller
        FXMLAnchorPaneCadastrosCategoriasDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCategoria(categoria);
        
        // Abre a tela de alteração e espera o usuario fechar.
        dialogStage.showAndWait();
        
        return controller.isButtonConfirmarClicked();
    }    
}
