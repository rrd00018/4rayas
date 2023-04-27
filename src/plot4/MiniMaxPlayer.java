/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plot4;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class MiniMaxPlayer extends Player {

    private ArrayList<ArrayList<Grid>> arbol;
    private  File fichero = null;
    private final String nombreArchivo = "4enrayas.txt";
    private Boolean arbolMostrado = false;

    private Integer jugadaFinal;
    private final Random aleatorio = new Random();

    /**
     * @brief Comprueba si el tablero esta lleno o no
     * @param tablero tablero a comprobar
     * @return true si esta lleno y false sino
     */
    boolean estaLleno(Grid tablero){
        for(int i=0; i< tablero.getColumnas(); i++){
            if(!tablero.fullColumn(i)){
                return false;
            }
        }
        return true;
    }

    /**
     * @brief Genera el árbol completo del juego
     * @param tablero tablero a partir del cual se va a generar el arbol
     * @param jugador jugador que tiene el turno
     * @param nivel nivel actual
     */
    void generaArbol(Grid tablero, int jugador, int nivel){
        if(arbol.size() == nivel){
            arbol.add(new ArrayList<>());
        }
        if(tablero.checkWin() == 1 || tablero.checkWin() == -1 || estaLleno(tablero))
            arbol.get(nivel).add(tablero);
        else {
            for (int i = 0; i < tablero.getColumnas(); i++) {
                if (!tablero.fullColumn(i)) {
                    Grid hijo = new Grid(tablero);
                    hijo.set(i, jugador);
                    arbol.get(nivel).add(hijo);
                    generaArbol(hijo, jugador * (-1), nivel + 1);
                }
            }
        }

    }

    int min(Grid tablero, int nivel){
        if(tablero.checkWin() == -1){
            return Integer.MAX_VALUE;
        }
        else if(estaLleno(tablero)){
            return 0;
        }else{
            int resultadoActual=Integer.MAX_VALUE;
            for(int i=0; i<tablero.getColumnas(); i++){
                if(!tablero.fullColumn(i)) {
                    Grid hijo=new Grid(tablero);
                    hijo.set(i, 1);
                    int resultadoHijo = max(hijo, nivel + 1);
                    if (resultadoHijo < resultadoActual) {
                        resultadoActual = resultadoHijo;
                    }
                }
            }
            return resultadoActual;
        }
    }

    int max(Grid tablero, int nivel){
        if(tablero.checkWin() == 1){
            return Integer.MIN_VALUE;
        }else if(estaLleno(tablero)){
            return 0;
        }else{
            int resultadoActual= Integer.MIN_VALUE;
            for(int i=0; i<tablero.getColumnas(); i++){
                if(!tablero.fullColumn(i)) {
                    Grid hijo=new Grid(tablero);
                    hijo.set(i, -1);
                    int resultadoHijo = min(hijo, nivel + 1);
                    if (resultadoHijo > resultadoActual) {
                        resultadoActual = resultadoHijo;
                        if (nivel == 1) {
                            jugadaFinal = i;
                        }
                    }
                }
            }
            return resultadoActual;
        }
    }

    /**
     * @brief Se ejecuta cada vez que es el turno de la IA
     * @param tablero Representación del tablero de juego
     * @param conecta Número de fichas consecutivas para ganar
     * @return Columna del tablero en la que se va a colocar la ficha
     */
    @Override
    public int turno(Grid tablero, int conecta) {
        if(!arbolMostrado) {
            crear_fichero();
            arbol = new ArrayList<>();
            ArrayList<Grid> nivel0 = new ArrayList<>();
            nivel0.add(tablero);
            arbol.add(nivel0);
            generaArbol(tablero, -1, 0);
            fichero();
            arbolMostrado = true;
        }

        jugadaFinal=-1;
        max(tablero,1);
        if(jugadaFinal == -1){
            int salida = aleatorio.nextInt(0,3);
            while(tablero.fullColumn(salida)){
                salida = (salida + 1) % tablero.columnas;
            }
            return salida;
        }
        return jugadaFinal;
    }

    /**
     * @brief Crea el fichero que va a contener la estructura
     */
    void crear_fichero(){
        fichero = new File (nombreArchivo);
        try {
            if (fichero.createNewFile())
                System.out.println("El fichero se ha creado correctamente");
            else
                System.out.println("No ha podido ser creado el fichero");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * @brief Escribe el tablero en el fichero
     * @param tablero tablero a escribir
     * @param nivel nivel del tablero a imprimir
     */
    void rellenar_fichero(Grid tablero,int nivel){
        int[][] mat=tablero.copyGrid();
        try {
            FileWriter writer = new FileWriter(nombreArchivo, true);
            writer.write("Nivel " + nivel + "\n");
            for(int i=0; i<tablero.getFilas();i++){
                for(int j=0; j<tablero.getColumnas(); j++){
                    if(mat[i][j]!=-1) writer.write(" ");
                    writer.write(String.valueOf(mat[i][j]));
                    writer.write(" ");
                }
                writer.write("\n");
            }
            writer.write("\n");
            writer.close();
            System.out.println("Valores agregados exitosamente al archivo " + nombreArchivo);
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo " + nombreArchivo);
            e.printStackTrace();
        }
    }

    /**
     * @brief Envia cada matriz de la estructura a escribirse en el fichero
     */
    void fichero(){
            for(int i = 0; i < arbol.size(); i++){
                for(int j = 0; j < arbol.get(i).size(); j++){
                    rellenar_fichero(arbol.get(i).get(j),i);
                }
            }
    }


} // MiniMaxPlayer

