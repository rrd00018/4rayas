/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plot4;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

public class MiniMaxPlayer extends Player {

    private ArrayList<ArrayList<Pair<Integer,Grid>>> tableroGenerado;
    private Integer nivelActual = 1;
    private  File fichero = null;
    private final String nombreArchivo = "4enrayas.txt";
    private Boolean arbolCreado = false;

    static class Pair<U, V> {

        public final U first;       // el primer campo de un par
        public final V second;      // el segundo campo de un par

        // Construye un nuevo par con valores especificados
        private Pair(U first, V second) {
            this.first = first;
            this.second = second;
        }

        @Override
        // Verifica que el objeto especificado sea "igual a" el objeto actual o no
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Pair<?, ?> pair = (Pair<?, ?>) o;

            // llamar al método `equals()` de los objetos subyacentes
            if (!first.equals(pair.first)) {
                return false;
            }
            return second.equals(pair.second);
        }

        @Override
        // Calcula el código hash de un objeto para admitir tablas hash
        public int hashCode() {
            // usa códigos hash de los objetos subyacentes
            return 31 * first.hashCode() + second.hashCode();
        }

        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }


    }

    void mostrar(Grid tablero){
        int[][] mat=tablero.copyGrid();
        for(int i=0; i<tablero.getFilas();i++){
            for(int j=0; j<tablero.getColumnas(); j++){
                if(mat[i][j]!=-1) System.out.print(" ");
                System.out.print(mat[i][j]);
                System.out.print(" ");
            }
            System.out.println("");
        }
        System.out.println("");
    }
    boolean estaLleno(Grid tablero){
        for(int i=0; i< tablero.getColumnas(); i++){
            if(!tablero.fullColumn(i)){
                return false;
            }
        }
        return true;
    }
    void expandirArbolCompleto(Integer padre, Grid tablero, int jugador, int nivel){
        //rellenar_fichero(tablero,nivel);
        if(tableroGenerado.size() == nivel){
            tableroGenerado.add(new ArrayList<>());
        }
        if(tablero.checkWin() == -1){
            tableroGenerado.get(nivel).add(new Pair<>(padre,tablero));
        }
        else if(tablero.checkWin() == 1){
            tableroGenerado.get(nivel).add(new Pair<>(padre,tablero));
            //Nodo final gana el humano
        } else if(estaLleno(tablero)){
            //Nodo final empate
            tableroGenerado.get(nivel).add(new Pair<>(padre,tablero));
        }else{
            if(tableroGenerado.size() == nivel){
                tableroGenerado.add(new ArrayList<>());
            }
            for(int i=0; i<tablero.getColumnas(); i++){
                if(!tablero.fullColumn(i)){
                    Grid hijo = new Grid(tablero);
                    hijo.set(i,jugador);
                    tableroGenerado.get(nivel).add(new Pair<>(padre,hijo));
                    //mostrar(hijo);
                    //System.out.println(tableroGenerado.get(nivel).size()-1);
                    expandirArbolCompleto(i,hijo,jugador*(-1),nivel+1);
                }
            }
        }
    }

    Grid encontrarPadre(Pair<Integer,Grid> actual,int nivel){
        if(nivel == 1){
            return actual.second;
        }else{
            return encontrarPadre(tableroGenerado.get(nivel-1).get(actual.first),nivel-1);
        }
    }
    @Override
    public int turno(Grid tablero, int conecta) {
        /*if(fichero == null) {
            crear_fichero();
        }*/
        tableroGenerado = new ArrayList<>();
        ArrayList<Pair<Integer,Grid>> nivel0 = new ArrayList<>();
        nivel0.add(new Pair<>(0,tablero));
        tableroGenerado.add(nivel0);
        int nivel=0;
        int jugador=-1;
        Grid jugada = null;
        expandirArbolCompleto(0, tablero, jugador, nivel);
        if(!arbolCreado) {
            //fichero();
            arbolCreado = true;
        }

        System.out.println("-----------------------------------");
        for(int i = 0; i < tableroGenerado.size(); i++) {
            for(int j = 0;  j < tableroGenerado.get(i).size(); j++){
                if((tableroGenerado.get(i).get(j).second).checkWin() == -1){
                    mostrar(tableroGenerado.get(i).get(j).second); //Tablero ganador
                    System.out.println("ENTRA AQUI");
                    jugada = encontrarPadre(tableroGenerado.get(i).get(j),i);
                    break;
                }
            }
            System.out.println("NIVEL " + i );
            if(jugada != null)
                break;
        }
        nivelActual = nivelActual + 2;
        for(int i = 0; i < tablero.filas; i++){
            for(int j = 0; j < tablero.columnas; j++){
                if(tablero.tablero[i][j] != jugada.tablero[i][j]){
                    return j;
                }
            }
        }
        return -1;
    }

    void crear_fichero(){
        fichero = new File (nombreArchivo);
        try {
            // A partir del objeto File creamos el fichero físicamente
            if (fichero.createNewFile())
                System.out.println("El fichero se ha creado correctamente");
            else
                System.out.println("No ha podido ser creado el fichero");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

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

    void fichero(){
        try{
            FileWriter writer = new FileWriter(nombreArchivo,true);
            int filas = tableroGenerado.get(0).get(0).second.filas;
            int columnas = tableroGenerado.get(0).get(0).second.columnas;
            System.out.println(tableroGenerado.size());
            for(int i = 0; i < tableroGenerado.size(); i++){ //Nivel
                for(int j = 0; j < tableroGenerado.get(i).size(); j++){
                    rellenar_fichero(tableroGenerado.get(i).get(j).second,i);
                }
            }

        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo " + nombreArchivo);
        }
    }


} // MiniMaxPlayer

