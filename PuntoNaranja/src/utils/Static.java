/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author rlobo
 */
public class Static {
    static String usuario, password, terminal, saldo;
    static Boolean pass;
    public Static() {
        if(Static.isWindows()){
            usuario=new auth().leerArchivo("Sesion\\archivoUser.txt");
            password=new auth().leerArchivo("Sesion\\archivoPassword.txt");
            terminal=new auth().leerArchivo("Sesion\\archivoId.txt");
            pass=Boolean.parseBoolean(new auth().leerArchivo("Sesion\\archivoPidePassword.txt"));
        }else{
            usuario=new auth().leerArchivo("Sesion/archivoUser.txt");
            password=new auth().leerArchivo("Sesion/archivoPassword.txt");
            terminal=new auth().leerArchivo("Sesion/archivoId.txt");
            pass=Boolean.parseBoolean(new auth().leerArchivo("Sesion/archivoPidePassword.txt"));
        }
        saldo="";
    }

    public static String getUsuario() {
        return usuario;
    }

    public static void setUsuario(String usuario) {
        Static.usuario = usuario;
    }

    public static String getPassword() {
        return password;
    }
    public static boolean isWindows() {
        String OS = System.getProperty("os.name").toLowerCase();
        return (OS.contains("win"));
    }
    public static void setPassword(String password) {
        Static.password = password;
    }

    public static String getTerminal() {
        return terminal;
    }

    public static void setTerminal(String terminal) {
        Static.terminal = terminal;
    }

    public static String getSaldo() {
        return saldo;
    }

    public static void setSaldo(String saldo) {
        Static.saldo = saldo;
    }

    public static Boolean getPass() {
        return pass;
    }

    public static void setPass(Boolean pass) {
        Static.pass = pass;
    }  
    
    
}
