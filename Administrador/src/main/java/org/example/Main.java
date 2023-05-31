package org.example;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        String archivoOriginal = "src/main/resources/Libros.txt";
        String archivoTemporal = "LibroTemp.txt";
        try (ZContext context = new ZContext()) {
            Socket subscriber = context.createSocket(SocketType.SUB);
            subscriber.connect("tcp://localhost:5563");
            subscriber.subscribe("".getBytes(ZMQ.CHARSET));

            while (!Thread.currentThread().isInterrupted()) {

                // Read message contents
                String contents = subscriber.recvStr();
                System.out.println(contents);
                String[] elementosEntrada = contents.split(":");//DEVOLVER:id
                try {
                    BufferedReader br = new BufferedReader(new FileReader(archivoOriginal));
                    BufferedWriter bw = new BufferedWriter(new FileWriter(archivoTemporal));

                    if (elementosEntrada[0].equals("DEVOLVER")) {
                        String linea;
                        while ((linea = br.readLine()) != null) {
                            String[] campos = linea.split("\\\\");
                            if (campos[0].equals(elementosEntrada[1]) && campos[2].equals("1")) {
                                bw.write(campos[0] + "\\" + campos[1] + "\\" + String.valueOf((Integer.valueOf(campos[2]) - 1)));
                            } else {
                                bw.write(linea);
                            }
                            bw.newLine();
                        }
                    }else if(elementosEntrada[0].equals("RENOVAR")){
                        //Fecha
                        Date fechaNueva = new Date();
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.WEEK_OF_YEAR, 1);
                        fechaNueva= cal.getTime();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        String fechaFormateada = dateFormat.format(fechaNueva);
                        String linea;
                        while ((linea = br.readLine()) != null) {
                            String[] campos = linea.split("\\\\");
                            if (campos[0].equals(elementosEntrada[1]) && campos[2].equals("1")) {
                                bw.write(campos[0] + "\\" + fechaFormateada + "\\" + campos[2]);
                            } else {
                                bw.write(linea);
                            }
                            bw.newLine();
                        }
                    }


                    br.close();
                    bw.close();

                    // Reemplazar el archivo original con el archivo modificado
                    if (reemplazarArchivo(archivoOriginal, archivoTemporal)) {
                        System.out.println("Archivo modificado exitosamente.");
                    } else {
                        System.out.println("Error al reemplazar el archivo.");
                    }
                } catch (IOException e) {
                    System.out.println("Error al modificar el archivo: " + e.getMessage());
                }


            }
        }

        /*while(!Thread.currentThread().isInterrupted()){
            System.out.println("SUB: " + subPrestamo.recvStr());
        }

        subPrestamo.close();
        subPrestamo.close();
        ctx.close();

         */
    }

    private static boolean reemplazarArchivo(String archivoOriginal, String archivoTemporal) {
        // Eliminar el archivo original
        boolean eliminado = new File(archivoOriginal).delete();

        if (eliminado) {
            // Renombrar el archivo temporal al nombre del archivo original
            File archivoTemp = new File(archivoTemporal);
            return archivoTemp.renameTo(new File(archivoOriginal));
        } else {
            return false;
        }
    }
}