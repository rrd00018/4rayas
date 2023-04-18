/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plot4;

import java.util.ArrayList;
import java.util.Optional;

public class MiniMaxPlayer extends Player {

    private final ArrayList<ArrayList<Pair<Integer,Grid>>> tableroGenerado = new ArrayList<>();

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
        if(tablero.checkWin() == -1){
            System.out.println("Encontrado estado final, gana la IA, nivel " + nivel);
            mostrar(tablero);
            //System.out.println(tablero);
            tableroGenerado.get(nivel).add(new Pair<>(padre,tablero));
        }
        else if(tablero.checkWin() == 1){
            System.out.println("Encontrado estado final, gana el HUMANO, nivel " + nivel);
            //System.out.println(tablero);
            mostrar(tablero);
            tableroGenerado.get(nivel).add(new Pair<>(padre,tablero));
        } else if(estaLleno(tablero)){
            System.out.println(("Encontrado estado final, lleno sin ganador"));
        }else{
            for(int i=0; i<tablero.getColumnas(); i++){
                if(!tablero.fullColumn(i)){
                    Grid hijo = new Grid(tablero);
                    hijo.set(i,jugador);
                    System.out.println("EXPANDIENDO...NIVEL-->" + nivel);
                    if(jugador == 1){
                        System.out.println("HUMANO VA A COLOCAR EN COLUMNA  " + i);
                    }else{
                        System.out.println("IA VA A COLOCAR EN COLUMNA " + i);
                    }
                    mostrar(tablero);
                    //System.out.println(tablero);
                    tableroGenerado.get(nivel).add(new Pair<>(padre,tablero));
                    System.out.println(tableroGenerado.get(nivel).size()-1);
                    expandirArbolCompleto(tableroGenerado.get(nivel).size(),hijo,jugador*(-1),nivel+1);
                }
            }
        }
    }
    @Override
    public int turno(Grid tablero, int conecta) {
        int nivel=0;
        int jugador=-1;
        expandirArbolCompleto(0,tablero,jugador,nivel);
       /* for(int i = 0; i < tableroGenerado.size(); i++) {
            for(int j = 0;  j < tableroGenerado.get(i).size(); j++){
                System.out.println(tableroGenerado.get(i).get(j).second);
            }
        }*/
        return getRandomColumn(tablero);
    }
    
} // MiniMaxPlayer
