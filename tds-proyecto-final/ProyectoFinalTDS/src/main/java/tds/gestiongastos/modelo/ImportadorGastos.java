package tds.gestiongastos.modelo;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface ImportadorGastos {
	Map<String, List<Gasto>> importarGastos(File fichero);
}
