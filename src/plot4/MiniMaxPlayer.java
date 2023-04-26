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

    boolean estaLleno(Grid tablero){
        for(int i=0; i< tablero.getColumnas(); i++){
            if(!tablero.fullColumn(i)){
                return false;
            }
        }
        return true;
    }
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
        if(tablero.checkWin() == -1){//Viene de jugar max, compruebo si es tablero ganador de la IA
            return 100000;
        }
        else if(estaLleno(tablero)){//Si no es ganador, compruebo si está lleno
            return 0;
        }else{//Si no está lleno, coloco mi ficha en sus sitios posibles
            int resultadoActual=100000;
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
        if(tablero.checkWin() == 1){//Viene de jugar min, compruebo si es tablero ganador suyo, del HUMANO
            return -100000;
        }else if(estaLleno(tablero)){//En caso de no serlo, compruebo si está lleno
            return 0;
        }else{//Si no está lleno, coloco mis posibles jugadas y expando
            int resultadoActual=-100000;
            for(int i=0; i<tablero.getColumnas(); i++){
                if(!tablero.fullColumn(i)) {
                    Grid hijo=new Grid(tablero);
                    hijo.set(i, -1);
                    int resultadoHijo = min(hijo, nivel + 1);
                    if (resultadoHijo > resultadoActual) {//Si la posible jugada "hijo" mejora, guardo el resultado
                        resultadoActual = resultadoHijo;
                        if (nivel == 1) {//Si estamos a nivel 1, guardo la jugada
                            jugadaFinal = i;
                        }
                    }
                }
            }
            return resultadoActual;
        }
    }

    @Override
    public int turno(Grid tablero, int conecta) {
        if(!arbolMostrado) {
            //crear_fichero();
            arbol = new ArrayList<>();
            ArrayList<Grid> nivel0 = new ArrayList<>();
            nivel0.add(tablero);
            arbol.add(nivel0);
            generaArbol(tablero, -1, 0);
            //fichero();
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
            for(int i = 0; i < arbol.size(); i++){ //Nivel
                for(int j = 0; j < arbol.get(i).size(); j++){
                    rellenar_fichero(arbol.get(i).get(j),i);
                }
            }
    }


} // MiniMaxPlayer

