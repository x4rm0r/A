package tds.gestiongastos.vista;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.page.MonthPage;
import com.calendarfx.view.page.WeekPage;
import com.calendarfx.view.page.YearPage;

import tds.gestiongastos.main.Configuracion;
import tds.gestiongastos.modelo.CuentaCompartida;
import tds.gestiongastos.modelo.Gasto;

public class CalendarioControlador extends MenuLateralControladorPlantilla {

    private static final String SEPARADOR = "--------------------------------------------------";
	
	@FXML
    private StackPane contenedorCalendario;
    private CalendarView vistaPrincipal;


    @FXML 
    private Label lblGastoTotal; 
    
    @FXML
    private Button btnBalance;

    @FXML
    public void initialize() {
        System.out.println(">>> Iniciando CalendarioControlador...");
        var gestion = Configuracion.getInstancia().getGestionGastos();
        var cuenta = gestion.getCuentaActiva();
        
        if (btnBalance != null) {
            if (cuenta instanceof CuentaCompartida) {
                btnBalance.setVisible(true);
                btnBalance.setManaged(true);
            } else {
                btnBalance.setVisible(false);
                btnBalance.setManaged(false);
            }
        }
        
        vistaPrincipal = new CalendarView();
        vistaPrincipal.setShowPrintButton(false);          
        vistaPrincipal.setShowAddCalendarButton(false);
        vistaPrincipal.setShowPageToolBarControls(false);
        vistaPrincipal.setShowSearchField(false);

        cargarEstilos();
        cargarDatos();
        
        vistaPrincipal.selectedPageProperty().addListener((obs, oldPage, newPage) -> {
            actualizarTotalDia();
        });

        vistaPrincipal.getDayPage().dateProperty().addListener(obs -> actualizarTotalDia());
        vistaPrincipal.getWeekPage().dateProperty().addListener(obs -> actualizarTotalDia());
        vistaPrincipal.getMonthPage().dateProperty().addListener(obs -> actualizarTotalDia());
        vistaPrincipal.getYearPage().dateProperty().addListener(obs -> actualizarTotalDia());

        contenedorCalendario.getChildren().setAll(vistaPrincipal);
        vistaPrincipal.showDayPage();
        

        actualizarTotalDia();
    }


    private void actualizarTotalDia() {
            var cuenta = Configuracion.getInstancia().getGestionGastos().getCuentaActiva();

            var paginaActual = vistaPrincipal.getSelectedPage();
            LocalDate fechaRef = paginaActual.getDate();
            LocalDate inicio = fechaRef;
            LocalDate fin = fechaRef;

     
            if (paginaActual instanceof WeekPage) {
                inicio = fechaRef.with(DayOfWeek.MONDAY);
                fin = fechaRef.with(DayOfWeek.SUNDAY);
            } 
            else if (paginaActual instanceof MonthPage) {
                inicio = fechaRef.withDayOfMonth(1);
                fin = fechaRef.withDayOfMonth(fechaRef.lengthOfMonth());
            } 
            else if (paginaActual instanceof YearPage) {
                inicio = fechaRef.withDayOfYear(1);
                fin = fechaRef.withDayOfYear(fechaRef.lengthOfYear());
            }

            final LocalDate fInicio = inicio;
            final LocalDate fFin = fin;

            double totalPeriodo = cuenta.getGastos().stream()
                .filter(g -> g.getFecha() != null)
                .filter(g -> {
                    LocalDate fGasto = g.getFecha();
                    return (fGasto.isEqual(fInicio) || fGasto.isAfter(fInicio)) && 
                           (fGasto.isEqual(fFin) || fGasto.isBefore(fFin));
                })
                .mapToDouble(Gasto::getCantidad)
                .sum();

            lblGastoTotal.setText(String.format("%.2f €", totalPeriodo));
       
    }

    private void cargarDatos() {
        System.out.println(">>> Cargando datos...");
        var cuenta = Configuracion.getInstancia().getGestionGastos().getCuentaActiva();
        CalendarSource fuenteDatos = new CalendarSource("Gastos");
        Map<String, Calendar> mapasCategorias = new HashMap<>();

        Calendar.Style[] estilos = {
                Calendar.Style.STYLE1, Calendar.Style.STYLE2, Calendar.Style.STYLE3,
                Calendar.Style.STYLE4, Calendar.Style.STYLE5, Calendar.Style.STYLE6
        };

        for (Gasto g : cuenta.getGastos()) {

            String nombreCategoriaLimpio = limpiarNombre(g.getCategoria().getNombre());
            Calendar calendario = mapasCategorias.computeIfAbsent(nombreCategoriaLimpio, k -> {
                Calendar c = new Calendar(k);
                c.setStyle(estilos[mapasCategorias.size() % estilos.length]);
                c.setReadOnly(true);
                return c;
            });

            String precio = (g.getCantidad() % 1 == 0)
                    ? String.format("%.0f €", g.getCantidad())
                    : String.format("%.2f €", g.getCantidad());


            String tituloEntrada = String.format("%s: %s (%s)", nombreCategoriaLimpio, g.getDescripcion(), precio);

            Entry<Gasto> entrada = new Entry<>(tituloEntrada);
            if (g.getFecha() != null) entrada.setInterval(g.getFecha());
            
            entrada.setFullDay(true);
            calendario.addEntry(entrada);
        }   
        fuenteDatos.getCalendars().setAll(mapasCategorias.values());
        vistaPrincipal.getCalendarSources().setAll(fuenteDatos);
    }
    
    private void cargarEstilos() {
        var css = getClass().getResource("/estilos_calendario.css");
        if (css != null) {
            vistaPrincipal.getStylesheets().clear();
            vistaPrincipal.getStylesheets().add(css.toExternalForm());
        }
    }
    
    
    private String limpiarNombre(String nombre) {
        if (nombre == null) return "Sin Cat.";
        int index = nombre.indexOf("_");
        return (index != -1) ? nombre.substring(index + 1) : nombre;
    }
    
    @FXML public void botonImportarDatos(ActionEvent event) {
        System.out.println(">>> Acción: Importar Gastos");
        System.out.println(SEPARADOR);
        this.sceneManager.showImportarGastos();
        cargarDatos();
        actualizarTotalDia();
    }
}