package tds.gestiongastos.modelo.impl;

import tds.gestiongastos.modelo.ImportadorGastos;

public class FactoriaImportadoresImpl {
	public static ImportadorGastos crearImportador(String tipo) {
        if (tipo == null) return null;

        switch (tipo.toLowerCase()) {
            case "csv":
                return new CSVImportadorGastos();
                
            default:
                return null;
        }
	}
}

