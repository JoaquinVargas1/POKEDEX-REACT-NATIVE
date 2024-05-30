

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;

public class Programa {
    
    static String palabrasReservadas[] = {"SELECT","FROM","WHERE","IN","AND","OR","CREATE","TABLE","CHAR","NUMERIC","NOT","NULL","CONSTRAINT","KEY","PRIMARY","FOREIGN","REFERENCES","INSERT","INTO","VALUES"};
    static JTextArea txtArea ;
    static String datos[][]= new String[36][3];
    
    static DefaultTableModel model;
    static JTable tabla;
    
    static DefaultTableModel modelIdentificador;
    static JTable tablaIdentificador ;
    static boolean esReservada = true;
    static int valorIdentificador=401;
    static int filaTablaIdentificador=0;
    static String datosTablaIdentificador[][] = new String[300][3];
    
    static DefaultTableModel modelConstante;
    static JTable tablaConstante ;
    public static int valorConstante=600;
    static int numeroConstante=19;
    static int type;
    static int code;
    public static void main(String[] args) {
        iniciar();
    }
    
    public static void iniciar(){
       JFrame frame = new JFrame("Rotating Square");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 800);
        frame.setVisible(true);
        frame.setLayout(null);
        frame.setResizable(false);
        
       JPanel Panel =new JPanel();
       Panel.setSize(1800, 1500);
       Panel.setLocation(0, 0);
       Panel.setBackground(Color.decode("#D1D6DA"));
       Panel.setLayout(null);
       frame.add(Panel);

       JLabel lb1= new JLabel("Scanner de Texto");
       lb1.setLocation(600,10);
       lb1.setSize(200, 20);
       lb1.setFont(new Font("Arial", Font.BOLD, 18));
       Panel.add(lb1);
       

       
        txtArea = new JTextArea();
        Panel.add(txtArea);
        
        JScrollPane scrollTxtArea = new JScrollPane(txtArea);
        scrollTxtArea.setSize(700, 200); 
        scrollTxtArea.setLocation(10, 40); 
        Panel.add(scrollTxtArea);

        JLabel lbTablaIdentificador= new JLabel("IDENTIFICADORES");
        lbTablaIdentificador.setLocation(600,270);
        lbTablaIdentificador.setSize(200, 20);
        lbTablaIdentificador.setFont(new Font("Arial", Font.BOLD, 18));
        Panel.add(lbTablaIdentificador);
        
        JLabel lbTablaConstantes= new JLabel("CONSTANTES");
        lbTablaConstantes.setLocation(1050,270);
        lbTablaConstantes.setSize(200, 20);
        lbTablaConstantes.setFont(new Font("Arial", Font.BOLD, 18));
        Panel.add(lbTablaConstantes);
        
        model = new DefaultTableModel(new Object[]{"No.", "Línea", "Token", "Tipo", "Código"}, 0);
        tabla= new JTable(model);
        JScrollPane scrollTabla = new JScrollPane(tabla);
       
       scrollTabla.setSize(400,300);
       scrollTabla.setLocation(50, 300);
       Panel.add(scrollTabla);
       
       modelIdentificador = new DefaultTableModel(new Object[]{"Identificador", "Valor", "Línea"}, 0);
       tablaIdentificador = new JTable(modelIdentificador);
       JScrollPane scrollTablaIdentificador = new JScrollPane(tablaIdentificador);
      
       scrollTablaIdentificador.setSize(400,300);
       scrollTablaIdentificador.setLocation(470, 300);
      Panel.add(scrollTablaIdentificador);
      
      
      modelConstante = new DefaultTableModel(new Object[]{"No", "Constante", "Tipo","Valor"}, 0);
      tablaConstante= new JTable(modelConstante);
      JScrollPane scrollTablaConstante = new JScrollPane(tablaConstante);
     
      scrollTablaConstante.setSize(400,300);
      scrollTablaConstante.setLocation(900, 300);
     Panel.add(scrollTablaConstante);


       JButton btnReinicio = new JButton("Reiniciar");
       btnReinicio.setSize(100,60);
       btnReinicio.setLocation(800, 650);
       btnReinicio.setBackground(Color.decode("#DC403B"));
       Panel.add(btnReinicio);
  
       btnReinicio.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               
               txtArea.setText("");
               model.setRowCount(0);
               modelIdentificador.setRowCount(0);
               modelConstante.setRowCount(0);
               valorConstante =600;
               numeroConstante=19;
               vaciarMatrizTablaIdentificadores();
               valorIdentificador=401;;
               filaTablaIdentificador=1;
         
           }
       });
        
        JButton ss = new JButton("Buscar");
        ss.setSize(100,60);
        ss.setLocation(600, 650);
        ss.setBackground(Color.decode("#5ADBA5"));
        Panel.add(ss);
        ss.addActionListener(new ActionListener() {
            

            @Override
            public void actionPerformed(ActionEvent e) {
                
                analizarTexto(0, "\\b(?<![‘])\\w+|[,().=><]+|‘[^’]+’(?![’])\\b|'[^']+'"); // para la primera tabla
                
            	analizarTexto(1,"\\b(?<![‘])\\w+(?![’])\\b"); // para la tabla identificadores
                
                analizarTexto(2,"\"([^\"]*)\"|'([^']*)'|’([^’]*)’|(?:[><=]\\s*)(\\d*\\.?\\d+)|‘[^’]+’|\\b\\d+(\\.\\d+)?\\b"); // para la tabla constantes                      
            }              
       });
       
        frame.revalidate();
        frame.repaint();
    }
    public static void analizarTexto(int tablaLlenar,String exresionRegularr) {
        String regex = exresionRegularr;

           Pattern pattern = Pattern.compile(regex);
            javax.swing.text.Document texto = txtArea.getDocument();
             
            vaciarMatrizTablaIdentificadores();
            
           try {
                
                int totalLineas = txtArea.getLineCount();

                for (int i = 0; i < totalLineas; i++) {
                    
                    int inicioLinea = txtArea.getLineStartOffset(i);
                    int finLinea = (i == totalLineas - 1) ? texto.getLength() : txtArea.getLineEndOffset(i);
                    String linea = texto.getText(inicioLinea, finLinea - inicioLinea);
                    Matcher matcher = pattern.matcher(linea);
                    while(matcher.find()) {
                    
                        switch(tablaLlenar) {
                        	case 0:
                                if(obtenerValorPalabraReservada(matcher.group())!=0){
                                    code=obtenerValorPalabraReservada(matcher.group());
                                }else if(obtenerValorDelimitador(matcher.group())!=0){
                                    code=obtenerValorDelimitador(matcher.group());
                                }else if(obtenerValorOperador(matcher.group())!=0){
                                    code=obtenerValorOperador(matcher.group());
                                }else if(obtenerValorRelacional(matcher.group())!=0){
                                    code=obtenerValorRelacional(matcher.group());
                                }else if(obtenerValorConstante(matcher.group())!=0){
                                    code=obtenerValorConstante(matcher.group());
                                }else if(obtenerValorIndentificador(matcher.group())!=0){
                                    code=obtenerValorIndentificador(matcher.group());
                                }else{
                                    code=valorConstante;
                                    valorConstante=valorConstante+1;
                                }


                                if (code >= 10 && code <= 29){
                                    type=1;

                                }else if(code >=50 && code <=54){
                                    type=5;
                                }else if(code >=61 && code <=62){
                                    type=6;
                                }else if(code >=70 && code <=73){
                                    type=7;
                                }else if(code >=81 && code <=85){
                                    type=8;
                                }else if(code >=401 && code <=410){
                                    type=4;
                                }else if(code >=600){
                                    type=6;
                                }else{
                                    type=0;
                                }


                                agregarToken(i + 1, matcher.group(), type , code);
                        	break;
                            case 1:
    	                		buscarIdentificador(matcher.group(),(i+1));
                            break;
                            case 2:
                                buscarConstante(matcher.group());
                            break;
                        }
                    }
                }
                //ingresamos los datos en la tabla IDENTIFICADORES una vez ya cargados
	            IngresarDatosTablaIdentificadores();
	            valorConstante=600;
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
    }
    

    private static int obtenerValorPalabraReservada(String palabra) {
        switch (palabra) {
            case "SELECT":
                return 10;
            case "FROM":
                return 11;
            case "WHERE":
                return 12;
            case "IN":
                return 13;
            case "AND":
                return 14;
            case "OR":
                return 15;
            case "CREATE":
                return 16;
            case "TABLE":
                return 17;
            case "CHAR":
                return 18;
            case "NUMERIC":
                return 19;
            case "NOT":
                return 20;
            case "NULL":
                return 21;
            default:
                return 0;
        }
    }
    
    private static int obtenerValorDelimitador(String delimitador) {
        switch (delimitador) {
            case ",":
                return 50;
            case ".":
                return 51;
            case "(":
                return 52;
            case ")":
                return 53;
            case "‘":
                return 54;
            default:
                return 0;
        }
    }
    
    private static int obtenerValorConstante(String constante) {
        switch (constante) {
            case "CONSTRAINT":
                return 22;
            case "KEY":
                return 23;
            case "PRIMARY":
                return 24;
            case "FOREIGN":
                return 25;
            case "REFERENCES":
                return 26;
            case "INSERT":
                return 27;
            case "INTO":
                return 28;
            case "VALUES":
                return 29;
            default:
                return 0;
        }
    }
    
    private static int obtenerValorOperador(String operador) {
        switch (operador) {
            case "+":
                return 70;
            case "-":
                return 71;
            case "*":
                return 72;
            case "/":
                return 73;
            default:
                return 0;
        }
    }
    
    private static int obtenerValorRelacional(String relacional) {
        switch (relacional) {
            case ">":
                return 81;
            case "<":
                return 82;
            case "=":
                return 83;
            case ">=":
                return 84;
            case "<=":
                return 85;
            default:
                return 0;
        }
    }


    public static int obtenerValorIndentificador(String palabra) {
        // Utilizar un switch para asignar el valor según la palabra
        switch (palabra) {
            case "ANOMBRE":
                return 401;
            case "CALIFICACION":
                return 402;
            case "TURNO":
                return 403;
            case "ALUMNOS":
                return 404;
            case "INSCRITOS":
                return 405;
            case "MATERIAS":
                return 406;
            case "CARRERAS":
                return 407;
            case "MNOMBRE":
                return 408;
            case "CNOMBRE":
                return 409;
            case "SEMESTRE":
                return 410;
            default:
                return 0; // Devolver 0 si la palabra no tiene un valor asociado
        }
    }
    
    public static void agregarToken(int linea, String token, int tipo, int codigo) {
        // Agrega el token a la tabla model con los valores apropiados
        if (tipo == 6) {
            token="CONSTANTE";
            model.addRow(new Object[]{model.getRowCount() + 1, linea, token, tipo, codigo});
        } else {
            // Agrega el token a la tabla model de manera habitual
            model.addRow(new Object[]{model.getRowCount() + 1, linea, token, tipo, codigo});
        }
    }


    public static boolean comprobarNumero(String cadena) {
        
        if(cadena.matches("-?\\s*\\d+(\\.\\d+)?")) {
            return true;
        }else {
            return false;
        }
    }
    
    //---------------------------------------------		INICIO FUNCIONES PARA LA TABLA CONSTANTES

    public static void buscarConstante(String cadena) {

        cadena = cadena.replaceAll("[><=]+", "");
        String cadenaLimpia = cadena.replaceAll("(?:['\"’])(\\w+)(?:['\"’])|['\"’]|‘|’", "$1");

        //validamos si la cadena es un numero o no
        String tipo = (comprobarNumero(cadena)) ? "61" : "62";
        
        modelConstante.addRow(new Object[]{numeroConstante, cadenaLimpia, tipo, valorConstante});
        numeroConstante+=6;
        valorConstante++;
    }
    
    // ----------------------------------------------------- FIN FUNCIONES TABLA CONSTANTES

    
    //---------------------------------------------		INICIO FUNCIONES PARA LA TABLA IDENTIFICADORES
    
    public static void vaciarMatrizTablaIdentificadores() {
        for(int fila=0;fila<300;fila++) {
             datosTablaIdentificador[fila][0]="disp onible";
        }
    }
    
    public static boolean comprobarRepeticion(String cadena,int linea) {
    	boolean seRepite = false;
    	for(int fila=0;fila<300 ;fila++) {
        	if(datosTablaIdentificador[fila][0].equals(cadena)) {
        		datosTablaIdentificador[fila][2] += " , "+linea; 
        		seRepite=true;
        		break;
        	}else {
        		seRepite=false;
        	}	
    	}
    	return (seRepite==true) ? true : false;
    }
    
    public static void buscarIdentificador(String cadena,int linea) {
    	
    	for(String palabra : palabrasReservadas) {
    		if(palabra.equals(cadena)) {
    			esReservada = true;
    			break;
    		}else {
    			esReservada=false;
    		}
    	}
    	
    	if(esReservada==false) {
    		
    		if(comprobarRepeticion(cadena,linea)==false) {
    			datosTablaIdentificador[filaTablaIdentificador][0]=cadena;
        		datosTablaIdentificador[filaTablaIdentificador][1]=String.valueOf(valorIdentificador);
        		datosTablaIdentificador[filaTablaIdentificador][2]=String.valueOf(linea);
        		valorIdentificador++;
        		filaTablaIdentificador++;
    		}
    	}
    }
    
    public static void IngresarDatosTablaIdentificadores() { 
    	for(int fila=0;fila<300;fila++) {
    		//validamos si la posicion de la matriz no esta datosTablaIdentificador y si la cadena no es un numero
             if(datosTablaIdentificador[fila][0]!="disp onible" && comprobarNumero(datosTablaIdentificador[fila][0])==false ) {
            	 //insertamos la fila a la tabla
            	 modelIdentificador.addRow(new Object[]{datosTablaIdentificador[fila][0],datosTablaIdentificador[fila][1],datosTablaIdentificador[fila][2]});
             }
    	
    	}
    }
    
    // ----------------------------------------------------- FIN FUNCIONES TABLA IDENTIFICADORES
}
