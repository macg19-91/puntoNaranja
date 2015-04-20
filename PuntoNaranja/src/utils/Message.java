/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import static utils.Static.terminal;

/**
 *
 * @author rlobo
 */
public class Message {
    private String parent;
    private Map<String, String> map;
    private Map<String, String> codigosMap;
    private String identificador;
    private Document document;
    private String usuario;
    private String clave;
    private String tienda;
    private JSONObject serviciosMap;
    private Static st;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public JSONObject getServiciosMap() {
        return serviciosMap;
    }

    public void setServiciosMap(JSONObject serviciosMap) {
        this.serviciosMap = serviciosMap;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Message(String parent, Map<String, String> map, String identificador) {
        this.parent = parent;
        this.map = map;
        this.identificador = identificador;
        this.clave = Static.getPassword();
        this.usuario = Static.getUsuario();
    }
    
    public Message() {
        this.parent = "isomsg";
        this.map = new HashMap<String, String>();
        codigosMap = new HashMap<String, String>();
        identificador = Static.getTerminal();
        pupulateCodigosMap();
        this.clave = st.getPassword();
        this.usuario = Static.getUsuario();
        tienda="00000";
        
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
    
    public void addField(String id,String value){
        map.put(id, value);
    }
    
    public String getField(String id){
        String str = map.get(id);
        return str;
    }
    
    public void setEchoTest(){
        map = new HashMap<String, String>();
        map.put("0","0800");
        map.put("3","990000");
        map.put("11",getSecuencia());
        map.put("41",identificador);
    }   
    
    public void consultaSaldo(){
        map = new HashMap<String, String>();
        map.put("3","200200");
        map.put("0","0100");
        map.put("11",getSecuencia());
        map.put("41",identificador);
        map.put("61",usuario);
    } 
    
    public void consultaEstadoTransaccionRecarga(String operador,String idTransaccion){
        map = new HashMap<String, String>();
        map.put("0","0100");
        map.put("3","200400");
        map.put("11",getSecuencia());
        map.put("41",identificador);
        map.put("61",operador);
        map.put("62",idTransaccion);
    }  
    
    public void recargaTiempoAire(String monto,String operador,String producto,String proceso,String celular) throws UnknownHostException, SocketException{
        String mac = "";
        String ip = "";
        NetworkInterface ni = null;
        try{
            ip = InetAddress.getLocalHost().getHostAddress();
        }
        catch(Exception e){
            ip = "127.0.0.1";
        }
        try{
            ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            mac = ni.getHardwareAddress().toString();
        }
        catch(Exception e){
            mac = "02-50-F2-CE-82-01";
        }
        String recarga = operador + ","+producto+","+celular+","+usuario+","+dispersion(usuario,clave)+","+tienda;
        String extraInfo = "PC"+"|"+ip+"|"+mac+"|NA|Hardware id client "+System.getProperty("os.name").toLowerCase()+"|NA";
        map = new HashMap<String, String>();
        map.put("0","0200");
        map.put("3",proceso);
        map.put("4",addCero(Integer.toString(Integer.parseInt(monto)*100),12));
        map.put("11",getSecuencia());
        map.put("12",getHora());
        map.put("13",getFecha());
        map.put("37",getReferencia());
        map.put("41",identificador);
        map.put("60",recarga);
        map.put("61",extraInfo);
    }  
    
    public void ventaPines(String monto,String operador,String producto) throws UnknownHostException, SocketException{
        NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
        String recarga = operador + ","+producto+","+usuario+","+dispersion(usuario,clave)+","+tienda;
        String extraInfo = "PC"+"|"+InetAddress.getLocalHost()+"|"+ni.getHardwareAddress()+"|NA|Hardware id client"+System.getProperty("os.name").toLowerCase()+"|NA";
        map = new HashMap<String, String>();
        map.put("0","0200");
        map.put("3","000500");
        map.put("4",addCero(Integer.toString(Integer.parseInt(monto)*100),12));
        map.put("11",getSecuencia());
        map.put("12",getHora());
        map.put("13",getFecha());
        map.put("37",getReferencia());
        map.put("41",identificador);
        map.put("60",recarga);
        map.put("61",extraInfo);
    }  
    
    public void consultaServiciosPublicos(String empresa,String numeroReferencia,String zonaSoloCabletica,String tipoSoloCabletica) throws UnknownHostException, SocketException{
        //NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
        String mac = "";
        String ip = "";
        NetworkInterface ni = null;
        try{
            ip = InetAddress.getLocalHost().getHostAddress();
        }
        catch(Exception e){
            ip = "127.0.0.1";
        }
        try{
            ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            mac = ni.getHardwareAddress().toString();
        }
        catch(Exception e){
            mac = "02-50-F2-CE-82-01";
        }
        String datos = usuario + "," + dispersion(usuario,clave) + ","+empresa+","+numeroReferencia;
        if(empresa.equals("021003")){
            datos += ","+zonaSoloCabletica+","+tipoSoloCabletica;
        }
        datos += ",NA,NA,NA,NA";
        String extraInfo = "PC"+"|"+ip+"|"+mac+"|NA||"+System.getProperty("os.name").toLowerCase()+"|NA";
        map = new HashMap<String, String>();
        map.put("0","0100");
        map.put("3","000004");
        map.put("11",getSecuencia());
        map.put("12",getHora());
        map.put("13",getFecha());
        map.put("37",getReferencia());
        map.put("41",identificador);
        map.put("60",datos);
        map.put("61",extraInfo);
    }  
    
    public void pagarServiciosPublicos(String monto,String consecutivoRecibo,String zonaSoloCabletica) throws UnknownHostException, SocketException{
        //NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
        String mac = "";
        String ip = "";
        NetworkInterface ni = null;
        try{
            ip = InetAddress.getLocalHost().getHostAddress();
        }
        catch(Exception e){
            ip = "127.0.0.1";
        }
        try{
            ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            mac = ni.getHardwareAddress().toString();
        }
        catch(Exception e){
            mac = "02-50-F2-CE-82-01";
        }
        String datos = usuario + "," + dispersion(usuario,clave) + ","+consecutivoRecibo+","+monto+"00";
        if(!zonaSoloCabletica.equals("")){
            datos += ","+zonaSoloCabletica;
        }
        
        String extraInfo = "PC"+"|"+ip+"|"+mac+"|NA|Hardware id client|"+System.getProperty("os.name").toLowerCase()+"|NA";
        map = new HashMap<String, String>();
        map.put("0","0200");
        map.put("3","000006");
        map.put("4",addCero(Integer.toString(Integer.parseInt(monto)*100),12));
        map.put("11",getSecuencia());
        map.put("12",getHora());
        map.put("13",getFecha());
        map.put("37",getReferencia());
        map.put("41",identificador);
        map.put("60",datos);
        map.put("61",extraInfo);
    }  
    
     
    
    public String getEchoResp(){
        String str = map.get("39");
        String response = codigosMap.get(str);
        return response;
    }
      

    private String getSecuencia() {
        auth file = new auth();
        String secuencia;
        if(Static.isWindows())secuencia = file.leerArchivo("Sesion\\archivoSecuencia.txt");
        else secuencia = file.leerArchivo("Sesion/archivoSecuencia.txt");
        int sec = Integer.parseInt(secuencia) + 1;
        if(sec> 999999)
            sec = 0;
        String secuen = String.format("%06d", sec);
        try {
            file.escribeFichero(secuen,"Sesion/archivoSecuencia.txt");
        } catch (IOException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (String.format("%06d",sec));
    }
    
    public Document buildXML() throws ParserConfigurationException, SAXException, IOException{
        String values = buildString();
        //String values = "<isomsg><field id=\"0\" value=\"200\" /><field id=\"0\" value=\"200\" /><field id=\"0\" value=\"200\" /></isomsg>";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        document = db.parse(new InputSource(new StringReader(values)));
        return document;
    }
    
    public String buildString() throws ParserConfigurationException, SAXException, IOException{
        String values = "<"+parent+">\n\r";
        ArrayList<Integer> ordenada= new ArrayList<>();
        for ( String key : map.keySet() ) {
            ordenada.add(Integer.parseInt(key));                
        }
        Collections.sort(ordenada);
        for (Integer ordenada1 : ordenada) {
            values += "<field id=\"" + Integer.toString(ordenada1) + "\" value=\"" + map.get(Integer.toString(ordenada1)) + "\"/>\n\r";
        }
        values += "</"+parent+">\n\r";
        return values;
    }
    
    public void getFromXML(Document doc){
        String value;
        String id;
        Element elem;
        map = new HashMap<String, String>();
        for(int i=0,j=doc.getChildNodes().getLength();i<j;i++){
            elem = (Element)doc.getChildNodes().item(i);
            value = elem.getAttribute("value");
            id = elem.getAttribute("id");
            map.put(id, value);
        }
    }
    
    public void getFromXML2(Document doc){
        String value;
        String id;
        Element elem;
        map = new HashMap<String, String>();
        for(int i=0,j=doc.getChildNodes().getLength();i<j;i++){
            elem = (Element)doc.getChildNodes().item(i);
            value = elem.getAttribute("value");
            id = elem.getAttribute("id");
            map.put(id, value);
        }
    }

    private String getHora() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        return addCero(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)),2) + addCero(Integer.toString(cal.get(Calendar.MINUTE)),2) + addCero(Integer.toString(cal.get(Calendar.SECOND)),2);
    }

    private String getFecha() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        return addCero(Integer.toString(cal.get(Calendar.MONTH)+1),2) + addCero(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)),2) +  Integer.toString(cal.get(Calendar.YEAR));
    }

    private String getReferencia() {
        auth file = new auth();
        String secuencia = "";
        if(Static.isWindows()){            
         secuencia = file.leerArchivo("Sesion\\archivoReferencia.txt");
        }
        else {
            secuencia = file.leerArchivo("Sesion/archivoReferencia.txt");
        }
        
        long sec = Long.parseLong(secuencia) + 1;
        long max = Long.parseLong("999999999999");
        if(sec > max)
            sec = 0;
        String secuen = String.format("%012d", sec);
        try {
            file.escribeFichero(secuen,"Sesion/archivoReferencia.txt");
        } catch (IOException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (String.format("%012d",sec));
    }

    private String dispersion(String usuario,String clave) {
        String enc = "";
        Hash hs = new Hash();
        enc = hs.get(usuario, clave);
        String enc2 = "4BD4E771034D14382B9F276733CEFBB00C2F412A215BB47DA8FAC3342B858AC3DB09B033C6C46E0B";
        return enc;//enc.toUpperCase();
    }
    
    static byte[] sha1(String input) throws NoSuchAlgorithmException {
        byte[] sh = null;
        try {
            sh = AeSimpleSHA1.SHA1(input);  
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sh;
    }
    

    private String addCero(String monto, int i) {
        return String.format("%0"+i+"d", Integer.parseInt(monto));
    };

    private void pupulateCodigosMap() {
        codigosMap = new HashMap<String, String>();
        codigosMap.put("00","Transacción aprobada en forma exitosa");
        codigosMap.put("04","Producto no asignado");
        codigosMap.put("1D","Clave invalida");
        codigosMap.put("25","No existe ultima transacción");
        codigosMap.put("27","Saldo insuficiente");
        codigosMap.put("28","Producto no asignado a cliente");
        codigosMap.put("2F","Error en la base de datos");
        codigosMap.put("31","Usuario no existe");
        codigosMap.put("32","Usuario inactivo");
        codigosMap.put("33","Datafono inactivo");
        codigosMap.put("34","Datafono no existe");
        codigosMap.put("35","Asociación usuario-datafono inactiva");
        codigosMap.put("36","Asociación usuario-datafono no existe");
        codigosMap.put("37","Error en el formato del número celular");
        codigosMap.put("38","Error en el formato de id de tienda");
        codigosMap.put("39","Falta porcentaje de ganancia asignado al producto");
        codigosMap.put("40","Producto inactivo");
        codigosMap.put("41","Error en el formato del terminal id");
        codigosMap.put("42","Error en el formato del monto");
        codigosMap.put("43","Error en el formato del número de referencia externo");
        codigosMap.put("44","Usuario sin permiso para la venta de recargas");
        codigosMap.put("46","Cambio de clave obligatoria");
        codigosMap.put("47","Error en el procedimiento de cambiar clave");
        codigosMap.put("55","Error en seguridad");
        codigosMap.put("61","Error en base ganancia");
        codigosMap.put("64","Dirección IP invalida");
        codigosMap.put("65","Dirección MAC invalida");
        codigosMap.put("66","Hardware Id invalido");
        codigosMap.put("67","Tipo de datafono invalido");
        codigosMap.put("68","Numero origen de celular invalido (aplica para SMS)");
        codigosMap.put("90","Reverso: No existe la transacción original");
        codigosMap.put("91","Reverso: Estado de la transacción original POSIBLE_VENTA");
        codigosMap.put("92","Reverso: Estado de la transacción original REVERSADA.");
        codigosMap.put("93","Reverso: Estado de la transacción original ANULADA.");
        codigosMap.put("70","No existen PINES en el inventario");
        codigosMap.put("71","Número de referencia inválido");
        codigosMap.put("77","Error interno de plataforma. Falta secuenciador de operador.");
    }

    public String getMsgResponse() {
        return codigosMap.get(map.get("39"));
    }
    
    public String getMsgAprovacion() {
        String resp = "";
        if(map.get("39").equals("00")){
            resp = map.get("38");
        }
        else{
            resp = map.get("39");
        }
        
        return resp;
    }
    
    public String getMsgMonto() {
        String resp = "";
        if(map.get("39").equals("00")){
            resp = map.get("4");
        }
        else{
            resp = map.get("39");
        }
        
        return resp;
    }
    
    public String getMsgPin() {
        String resp = "";
        if(map.get("39").equals("00")){
            String[] datosPin=map.get("62").split(",");
            resp = datosPin[1]+", Clave: "+datosPin[2];
        }
        else{
            resp = map.get("39");
        }
        
        return resp;
    }
    
    public void pupulateServiciosMap() {
        
        try {
            serviciosMap = new httpCall().call("Servicios");
        } catch (IOException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
