package tds.gestiongastos.vista;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import tds.gestiongastos.main.Configuracion;
import tds.gestiongastos.modelo.TipoCuenta;
import tds.gestiongastos.modelo.impl.CuentaCompartidaImpl;

public abstract class MenuLateralControladorPlantilla {

    protected SceneManager sceneManager;

    public MenuLateralControladorPlantilla() {
        this.sceneManager = Configuracion.getInstancia().getSceneManager();
    }

    @FXML
    protected void botonDashboard() {
        TipoCuenta cuentaActiva=Configuracion.getInstancia().getGestionGastos().getCuentaActiva();
    	System.out.println("=================================================");
        System.out.println(">>> Dashboard  | Desde: " + this.getClass().getSimpleName()); 
        if(cuentaActiva instanceof CuentaCompartidaImpl) {
        	this.sceneManager.showCuentaCompartida();
        }
        else {
        	this.sceneManager.showCuentaPersonal();
        }
        
        System.out.println("=================================================");
    }
    
    @FXML
    protected void botonVolver() {
    	System.out.println("=================================================");
        System.out.println(">>> Ventana Principal  | Desde: " + this.getClass().getSimpleName()); 
        this.sceneManager.showVentanaPrincipal();
        System.out.println("=================================================");
    }
 

    @FXML
    protected void botonCalendario() {
        System.out.println("=================================================");
        System.out.println(">>> Calendario | Desde: " + this.getClass().getSimpleName());
        System.out.println("=================================================");
        this.sceneManager.showCalendario();
    }

    @FXML
    protected void botonGrafico() {
        System.out.println("=================================================");
        System.out.println(">>> Gráficos | Desde: " + this.getClass().getSimpleName());
        System.out.println("=================================================");
        this.sceneManager.showGrafico();
    }
    
    @FXML 
    public void botonVerBalance(ActionEvent event) {
    	System.out.println("=================================================");
        System.out.println(">>> Ver Balance | Desde: " + this.getClass().getSimpleName());
        System.out.println("=================================================");
        
        this.sceneManager.showVerBalance();
    }

    @FXML
    protected void botonVerLimites() {
        System.out.println(">>> Abriendo: Estado de Límites");
        this.sceneManager.showEstadoLimites();
    }

    @FXML
    protected void botonCategorias() {
        System.out.println(">>> Acción: Nueva Categoría");
        this.sceneManager.showNuevaCategoria();
    }

    @FXML
    protected void botonNuevaAlerta() {
        System.out.println(">>> Acción: Configutación Alertas");
        this.sceneManager.showNuevaAlerta();
    }
    
    @FXML public void botonHistorialNotificaciones(ActionEvent event) { 
        System.out.println(">>> Acción: Historial Notificaciones");
        this.sceneManager.showHistorialNotificaciones(); 
    }
   
}
