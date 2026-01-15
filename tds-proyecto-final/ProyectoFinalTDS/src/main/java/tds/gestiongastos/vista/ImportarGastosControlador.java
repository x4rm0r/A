package tds.gestiongastos.vista;

import java.io.File;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tds.gestiongastos.main.Configuracion;

public class ImportarGastosControlador {

    @FXML private TextField txtRutaArchivo;
    private File archivoSeleccionado;

    @FXML public void initialize() { }

    @FXML
    void botonSeleccionarArchivo(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Seleccionar archivo");
        fc.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Archivos soportados", "*.csv"),
            new FileChooser.ExtensionFilter("Todos", "*.*")
        );
        
        archivoSeleccionado = fc.showOpenDialog(txtRutaArchivo.getScene().getWindow());
        if (archivoSeleccionado != null) {
            txtRutaArchivo.setText(archivoSeleccionado.getAbsolutePath());
        }
    }

    @FXML
    void botonImportar(ActionEvent event) {
        if (archivoSeleccionado == null) {
            mostrarAlerta("Atención", "Selecciona un archivo primero.", AlertType.WARNING);
            return;
        }

        try {
            List<String> alertasGeneradas = Configuracion.getInstancia().getGestionGastos().importarDatos(archivoSeleccionado);

            if (alertasGeneradas == null) {
                mostrarAlerta("Error", "No se han podido leer datos o el formato es incorrecto.", AlertType.ERROR);
            } 
            
            else if (alertasGeneradas.isEmpty()) {
                mostrarAlerta("Éxito", "Importación completada correctamente. Sin incidencias.", AlertType.INFORMATION);
                cerrarVentana();
            } 
            
            else {
                StringBuilder mensajeBuilder = new StringBuilder();
                mensajeBuilder.append("Se han importado los gastos, pero revisa lo siguiente:\n\n");
                
                for (String aviso : alertasGeneradas) {
                    mensajeBuilder.append("• ").append(aviso).append("\n");
                }

                mostrarAlerta("Límites Superados", mensajeBuilder.toString(), AlertType.WARNING);
                
                cerrarVentana();
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Fallo crítico al importar: " + e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML void botonCancelar(ActionEvent event) { cerrarVentana(); }

    private void cerrarVentana() { 
        ((Stage) txtRutaArchivo.getScene().getWindow()).close(); 
    }

    private void mostrarAlerta(String titulo, String contenido, AlertType tipo) { 
        Alert alert = new Alert(tipo); 
        alert.setTitle(titulo); 
        alert.setHeaderText(null);

        alert.setResizable(true);

        if (contenido.contains("\n")) {
            TextArea textArea = new TextArea(contenido);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(textArea, 0, 0);

            alert.getDialogPane().setContent(expContent);
            alert.getDialogPane().setPrefSize(500, 300);
        } else {
            alert.setContentText(contenido);
        }

        alert.showAndWait(); 
    }
}