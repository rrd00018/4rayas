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
                    tableroGenerado.get(nivel).add(new Pair<>(padre,tablero));
                    //mostrar(hijo);
                    //System.out.println(tableroGenerado.get(nivel).size()-1);
                    expandirArbolCompleto(i,hijo,jugador*(-1),nivel+1);
                }
            }
        }
    }

    Grid encontrarPadre(Pair<Integer,Grid> actual,int nivel){
        if(nivel == nivelActual){
            return actual.second;
        }else{
            return encontrarPadre(tableroGenerado.get(nivel-1).get(actual.first),nivel-1);
        }
    }
    @Override
    public int turno(Grid tablero, int conecta) {
        crear_fichero();
        tableroGenerado = new ArrayList<>();
        ArrayList<Pair<Integer,Grid>> nivel0 = new ArrayList<>();
        nivel0.add(new Pair<>(0,tablero));
        tableroGenerado.add(nivel0);
        int nivel=0;
        int jugador=-1;
        Grid jugada = null;
        int nivel = 0;
        expandirArbolCompleto(0,tablero,jugador,nivel);
        System.out.println("-----------------------------------");
        for(int i = 0; i < tableroGenerado.size(); i++) {
            for(int j = 0;  j < tableroGenerado.get(i).size(); j++){
                if((tableroGenerado.get(i).get(j).second).checkWin() == -1){
                    jugada = encontrarPadre(tableroGenerado.get(i).get(j),i);
                    break;
                }
            }
            if(jugada != null)
                break;
        }
        mostrar(jugada);
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
        File fichero = new File ("C:\\Users\\josea\\Desktop\\IA\4enrayas.txt");


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
} // MiniMaxPlayer
