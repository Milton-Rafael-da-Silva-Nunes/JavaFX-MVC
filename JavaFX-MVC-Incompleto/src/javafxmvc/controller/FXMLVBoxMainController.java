package javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Rafael Nunes
 */
public class FXMLVBoxMainController implements Initializable {

    @FXML
    private MenuItem menuItemCadastrosClientes; 
    @FXML
    private MenuItem menuItemprocessoVendas; 
    @FXML
    private MenuItem menuItemGraficosVendasPorMes; 
    @FXML
    private MenuItem menuItemRelatoriosQuantidadeProdutoEstoque; 
    @FXML
    private AnchorPane anchorPane; 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    } 
    
    @FXML
    public void handleMenuIntemCadastrosClientes() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLAnchorPaneCadastrosClientes.fxml"));
        anchorPane.getChildren().setAll(a);
    } 
    
    @FXML
    public void handleMenuIntemCadastrosCategorias() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLAnchorPaneCadastrosCategorias.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuIntemCadastrosProdutos() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLAnchorPaneCadastrosProdutos.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemProcessosVendas() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLAnchorPaneProcessosVendas.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemGraficosVendasPorMes() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLAnchorPaneGraficosVendasPorMes.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemRelatoriosQuantidadeProdutoEmEstoque() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLAnchorPaneRelatorioQuantidadeDeProdutoEmEstoque.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
}
