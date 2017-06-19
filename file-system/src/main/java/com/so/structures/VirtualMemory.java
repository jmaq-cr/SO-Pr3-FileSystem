package com.so.structures;

/**
 * Created by Jose Aguilar Quesada on 6/18/2017.
 */
public class VirtualMemory {
    public char valor;
    private String posicion;
    private String ruta;

    public VirtualMemory(char valor, String posicion, String ruta){ // Objeto que se Guarda en Matriz
        this.valor = valor; // Cualquier Caracter
        this.posicion = posicion; // Posicion En el String Inicial (BEST FIT)
        this.ruta = ruta; // Nombre del Archivo al cual responde como contenido
    }

    public static VirtualMemory[][] verificarSectores(String contenido, int lineas, int corte, VirtualMemory[][] matriz){
        int contadorEspacios = 0;

        int filaInicio = 0;
        int colInicio = 0;

        int filaFin = 0;
        int colFin = 0;

        int bandera = 0;


        for (int filas = 0; filas < lineas; filas++){ // Muestro la Matriz
            for(int columnas = 0; columnas < corte; columnas++){
                if (matriz[filas][columnas].valor == '-'){
                    if(bandera == 0){
                        filaInicio = filas;
                        colInicio = columnas;
                        bandera = 1;
                    }
                    contadorEspacios++;
                    if (contenido.length() == contadorEspacios){
                        filaFin = filas;
                        colFin = columnas;
                        return matriz = insertarMatriz(filaInicio, colInicio, filaFin, colFin, corte, contenido, matriz);
                    }
                }
                else if(matriz[filas][columnas].valor != '-') {
                    contadorEspacios = 0;
                    bandera = 0;
                }
            }
        }
        return matriz;
    }

    public static VirtualMemory[][] insertarMatriz(int filasInicio, int columnasInicio, int filasFin, int columnasFin, int corte, String contenido, VirtualMemory[][] matriz){
        int contador = 0;
        System.out.println(filasInicio);
        System.out.println(filasFin);
        System.out.println(columnasInicio);
        System.out.println(columnasFin);

        for (int filas = filasInicio; filas <= filasFin; filas++){ // Muestro la Matriz
            for(int columnas = columnasInicio; columnas < corte; columnas++){
                if (contador < contenido.length()){
                    matriz[filas][columnas].valor = contenido.charAt(contador);
                    contador++;
                }
            }
            if (columnasInicio != 0){
                columnasInicio = 0;
            }
        }
        return matriz;
    }
}
