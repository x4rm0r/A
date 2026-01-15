package tds.gestiongastos.vista;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import tds.gestiongastos.main.Configuracion;
import tds.gestiongastos.modelo.CuentaCompartida;
import tds.gestiongastos.modelo.ParticipanteCuenta;
import tds.gestiongastos.modelo.TipoCuenta;

public class BalanceVistaControlador extends MenuLateralControladorPlantilla {

    private static final String SEPARADOR = "--------------------------------------------------";
    
    @FXML private TableView<ParticipanteCuenta> tablaBalance;
    @FXML private TableColumn<ParticipanteCuenta, String> colParticipante;
    @FXML private TableColumn<ParticipanteCuenta, Double> colPorcentaje;
    @FXML private TableColumn<ParticipanteCuenta, Double> colSaldo;

    @FXML
    public void initialize() {
        
        colParticipante.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPorcentaje.setCellValueFactory(new PropertyValueFactory<>("porcentajeAsumido"));
        colSaldo.setCellValueFactory(new PropertyValueFactory<>("saldo"));

        configurarColoresSaldo();

        cargarDatos();
    }

    private void cargarDatos() {
        TipoCuenta cuenta = Configuracion.getInstancia().getGestionGastos().getCuentaActiva();
    
            tablaBalance.setItems(FXCollections.observableArrayList(
                ((CuentaCompartida) cuenta).getParticipantes()
            ));
     }

    private void configurarColoresSaldo() {
        colSaldo.setCellFactory(column -> new TableCell<ParticipanteCuenta, Double>() {
            @Override
            protected void updateItem(Double saldo, boolean empty) {
                super.updateItem(saldo, empty);
                if (empty || saldo == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.format("%.2f €", saldo));
                    if (saldo >= 0) {
                        setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    }
                }
            }
        });
    }
    
    @FXML public void botonImportarDatos(ActionEvent event) {
        System.out.println(">>> Acción: Importar Gastos");
        System.out.println(SEPARADOR);
        this.sceneManager.showImportarGastos();
        cargarDatos();
    }
}
