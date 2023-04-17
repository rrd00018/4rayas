/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plot4;

import java.util.ArrayList;

public class MiniMaxPlayer extends Player {
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
    void expandirArbolCompleto(Grid tablero, int jugador, int nivel){
        if(tablero.checkWin() == -1){
            System.out.println("Encontrado estado final, gana la IA, nivel " + nivel);
            mostrar(tablero);
        }
        else if(tablero.checkWin() == 1){
            System.out.println("Encontrado estado final, gana el HUMANO, nivel " + nivel);
            mostrar(tablero);
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
                    expandirArbolCompleto(hijo,jugador*(-1),nivel+1);
                }
            }
        }
    }
    @Override
    public int turno(Grid tablero, int conecta) {
        int nivel=0;
        int jugador=-1;
        expandirArbolCompleto(tablero,jugador,nivel);
        return getRandomColumn(tablero);
    }
    
} // MiniMaxPlayer
