package tds.gestiongastos.vista;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import tds.gestiongastos.main.Configuracion;
import tds.gestiongastos.modelo.Alerta;
import tds.gestiongastos.modelo.Categoria;
import tds.gestiongastos.modelo.CuentaCompartida;
import tds.gestiongastos.modelo.EstrategiaTemporal;
import tds.gestiongastos.modelo.Gasto;
import tds.gestiongastos.modelo.TipoCuenta;
import tds.gestiongastos.modelo.impl.EstrategiaMensual;
import tds.gestiongastos.modelo.impl.EstrategiaSemanal;

public class LimitesVistaControlador extends MenuLateralControladorPlantilla {

    @FXML private ListView<String> listaProgreso;
    @FXML private ComboBox<String> comboFiltroPeriodo;
    @FXML private ComboBox<Categoria> comboFiltroCategoria;
    @FXML private Button btnBalance;
    
    private static final String SEPARADOR = "--------------------------------------------------";

    @FXML
    public void initialize() { 
        
        var gestion = Configuracion.getInstancia().getGestionGastos();
        var cuenta = gestion.getCuentaActiva();

        if (btnBalance != null) {
            boolean esCompartida = cuenta instanceof CuentaCompartida;
            btnBalance.setVisible(esCompartida);
            btnBalance.setManaged(esCompartida);
        }
    	
    	comboFiltroPeriodo.setItems(FXCollections.observableArrayList("Todas", "Mensual", "Semanal"));
        comboFiltroPeriodo.setValue("Todas");

        cargarCategoriasEnCombo();
        configurarRenderizadoLista();
        
        comboFiltroPeriodo.valueProperty().addListener((obs, viejo, nuevo) -> {
            cargarEstadoLimites();
        });

        comboFiltroCategoria.valueProperty().addListener((obs, viejo, nuevo) -> {
            cargarEstadoLimites();
        });
        
        
        cargarEstadoLimites();
    }

    private void cargarCategoriasEnCombo() {
        List<Categoria> categorias = Configuracion.getInstancia().getGestionGastos().getTodasCategorias();
        List<Categoria> listaConTodas = new ArrayList<>();
        listaConTodas.add(null); 
        listaConTodas.addAll(categorias);
        
        comboFiltroCategoria.setItems(FXCollections.observableArrayList(listaConTodas));

        comboFiltroCategoria.setButtonCell(crearCeldaCategoria());
        comboFiltroCategoria.setCellFactory(lv -> crearCeldaCategoria());

        comboFiltroCategoria.setConverter(new StringConverter<Categoria>() {
            @Override
            public String toString(Categoria c) {
                if (c == null) return "Todas";
                return limpiarPrefijo(c.getNombre());
            }
            @Override
            public Categoria fromString(String string) { return null; }
        });
        
        comboFiltroCategoria.getSelectionModel().selectFirst();
    }

    private ListCell<Categoria> crearCeldaCategoria() {
        return new ListCell<Categoria>() {
            @Override
            protected void updateItem(Categoria item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Todas");
                } else {
                    setText(limpiarPrefijo(item.getNombre()));
                }
            }
        };
    }

    private void configurarRenderizadoLista() {
        listaProgreso.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else if (item.startsWith("No se")) {
                    setText(item);
                    setGraphic(null);
                } else {
                    VBox contenedor = new VBox(5);
                    Label lblTexto = new Label(item);
                    lblTexto.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

                    double progreso = extraerProgreso(item);
                    ProgressBar pb = new ProgressBar(progreso);
                    pb.setMaxWidth(Double.MAX_VALUE);

                    if (progreso >= 1.0) pb.setStyle("-fx-accent: #e74c3c;");
                    else if (progreso > 0.6) pb.setStyle("-fx-accent: #f39c12;");
                    else pb.setStyle("-fx-accent: #2ecc71;");

                    contenedor.getChildren().addAll(lblTexto, pb);
                    setGraphic(contenedor);
                    setText(null);
                }
            }
        });
    }

    private double extraerProgreso(String texto) {
        try {
            if (texto.contains("EXCEDIDO")) return 1.0;
            int inicio = texto.lastIndexOf("(") + 1;
            int fin = texto.lastIndexOf("%");
            return Double.parseDouble(texto.substring(inicio, fin)) / 100.0;
        } catch (Exception e) { return 0; }
    }

    private void cargarEstadoLimites() {
        TipoCuenta cuenta = Configuracion.getInstancia().getGestionGastos().getCuentaActiva();

        List<Alerta> alertas = Configuracion.getInstancia().getGestionGastos().getAlertas();
        List<Gasto> todosLosGastos = cuenta.getGastos();
        
        String filtroPeriodo = comboFiltroPeriodo.getValue();
        Categoria filtroCat = comboFiltroCategoria.getValue();

        List<String> items = new ArrayList<>();
        LocalDate hoy = LocalDate.now();

        for (Alerta alerta : alertas) {
            if (filtroPeriodo != null && !filtroPeriodo.equals("Todas")) {
                if (!alerta.getTipo().equalsIgnoreCase(filtroPeriodo)) continue;
            }


            if (filtroCat != null) {
                if (alerta.getCategoria() == null || 
                    !alerta.getCategoria().getNombre().equals(filtroCat.getNombre())) {
                    continue;
                }
            }

            EstrategiaTemporal estrategia = alerta.getTipo().equalsIgnoreCase("Semanal") 
                                            ? new EstrategiaSemanal() 
                                            : new EstrategiaMensual();

            double gastado = todosLosGastos.stream()
                    .filter(g -> (alerta.getCategoria() == null || 
                                 g.getCategoria().getNombre().equals(alerta.getCategoria().getNombre())))
                    .filter(g -> estrategia.esMismoPeriodo(g.getFecha(), hoy))
                    .mapToDouble(Gasto::getCantidad).sum();


            String nombreCat = (alerta.getCategoria() != null) ? limpiarPrefijo(alerta.getCategoria().getNombre()) : "General";
            String estado = String.format("[%s] %s: %.2f € / %.2f €", 
                            alerta.getTipo(), nombreCat, gastado, alerta.getLimite());
            
            if (gastado > alerta.getLimite()) {
                estado += " EXCEDIDO";
            } else {
                double porcentaje = (alerta.getLimite() > 0) ? (gastado / alerta.getLimite()) * 100 : 0;
                estado += String.format(" (%.0f%%)", porcentaje);
            }
            items.add(estado);
        }

        if (items.isEmpty()) {
            items.add("No se encontraron alertas para los filtros seleccionados.");
        }
        
        listaProgreso.setItems(FXCollections.observableArrayList(items));
    }

    private String limpiarPrefijo(String nombre) {
        int index = nombre.indexOf("_");
        return (index != -1) ? nombre.substring(index + 1) : nombre;
    }
    
    @FXML
    public void botonLimpiarFiltros(ActionEvent event) {
        comboFiltroPeriodo.setValue("Todas");
        comboFiltroCategoria.getSelectionModel().selectFirst();
    }
    
    @FXML public void botonImportarDatos(ActionEvent event) {
        System.out.println(">>> Acción: Importar Gastos");
        
        this.sceneManager.showImportarGastos();

        var gestion = Configuracion.getInstancia().getGestionGastos();
        var cuentaActual = gestion.getCuentaActiva();
        
        var cuentaRefrescada = gestion.getCuentasDisponibles().stream()
                .filter(c -> c.getNombre().equals(cuentaActual.getNombre()))
                .findFirst()
                .orElse(cuentaActual);
        
        gestion.setCuentaActiva(cuentaRefrescada);

        cargarEstadoLimites();
        cargarCategoriasEnCombo(); 
        System.out.println(SEPARADOR);
    }
    
    @Override
    @FXML
    public void botonNuevaAlerta() {
        System.out.println(">>> Abriendo Nueva Alerta desde Estado de Límites...");
        this.sceneManager.showNuevaAlerta();
        cargarEstadoLimites();
    }
    
    @FXML public void botonCategorias(ActionEvent event) {
        System.out.println(">>> Acción: Categorías");
        this.sceneManager.showNuevaCategoria(); 
        cargarCategoriasEnCombo(); 
        System.out.println(SEPARADOR);
    }

}