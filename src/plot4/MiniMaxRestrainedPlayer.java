/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plot4;

/**
 *
 * @author José María Serrano
 * @version 1.7 Departamento de Informática. Universidad de Jáen 
 * Última revisión: 2023-03-30
 *
 * Inteligencia Artificial. 2º Curso. Grado en Ingeniería Informática
 *
 * Clase MiniMaxRestrainedPlayer para representar al jugador CPU que usa una
 * técnica de IA
 *
 * Esta clase es en la que tenemos que implementar y completar el algoritmo
 * MiniMax Restringido
 *
 */
public class MiniMaxRestrainedPlayer extends Player {

    int heuristica(Grid tablero, int jugador){
        int salida = 0,salida1 = 0, salida2 = 0;
        salida += numCadenas(jugador,tablero,1);
        salida1 += numCadenas(jugador,tablero,2) ;
        salida2 += numCadenas(jugador,tablero,3);

        return (salida*10 + salida1*100 +salida2*1000) * jugador;
    }

    int numCadenas(int turno, Grid tablero,int tam){
        int cadenasH = cadenasHorizontales(turno,tablero,tam);
        int cadenasV = cadenasVerticales(turno,tablero,tam);
        int cadenasDA = cadenasDiagonalesAscendentes(turno,tablero,tam);
        int cadenasDD = cadenasDiagonalesDescendentes(turno,tablero,tam);
        return cadenasDD + cadenasDA + cadenasH + cadenasV;
    }

    int cadenasHorizontales(int turno, Grid tablero, int tam){
        int[][] matriz = tablero.tablero;
        int resultado = 0;
        int seguidas = 0;
        for(int i = 0; i < tablero.filas; i++){
            for(int j = 0; j < tablero.columnas-tam; j++){
                if(matriz[i][j] == turno){
                    seguidas++;
                   for(int k = 0; k < tam; k++){
                       if(matriz[i][j+k] != turno){
                           break;
                       }else{
                           seguidas++;
                       }
                   }
                }
                if(seguidas == tam){
                    resultado++;
                }
            }
        }
        return resultado;
    }

    int cadenasVerticales(int turno, Grid tablero, int tam){
        int[][] matriz = tablero.tablero;
        int resultado = 0;
        int seguidas = 0;
        for(int i = 0; i <)
    }

    /**
     * @brief funcion que determina donde colocar la ficha este turno
     * @param tablero Tablero de juego
     * @param conecta Número de fichas consecutivas adyacentes necesarias para
     * ganar
     * @return columna donde dejar caer la ficha
     */
    @Override
    public int turno(Grid tablero, int conecta) {

        int posicion = getRandomColumn(tablero);
        // to do

        // ...
        return posicion;

    } // turno

} // MiniMaxRestrainedPlayer
