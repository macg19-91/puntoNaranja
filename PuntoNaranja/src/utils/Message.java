/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
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
    }
    
    public Message() {
        this.parent = "isomsg";
        this.map = new HashMap<String, String>();
        identificador = getIdentificador();
        this.codigosMap = pupulateCodigosMap();
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
        map.put("0","0100");
        map.put("3","200200");
        map.put("11",getSecuencia());
        map.put("41",identificador);
        map.put("61",identificador);
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
        NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
        String recarga = operador + ","+producto+","+celular+","+usuario+","+dispersion(clave)+","+tienda;
        String extraInfo = "H2H"+"|"+InetAddress.getLocalHost()+"|"+ni.getHardwareAddress()+"|NA|Hardware id client"+System.getProperty("os.name").toLowerCase()+"|NA";
        map = new HashMap<String, String>();
        map.put("0","0200");
        map.put("3",proceso);
        map.put("4",monto);
        map.put("11",getSecuencia());
        map.put("12",getHora());
        map.put("13",getFecha());
        map.put("37",getReferencia());
        map.put("41",identificador);
        map.put("60",recarga);
        map.put("62",extraInfo);
    }  
    
    public void ventaPines(String monto,String operador,String producto) throws UnknownHostException, SocketException{
        NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
        String recarga = operador + ","+producto+","+usuario+","+dispersion(clave)+","+tienda;
        String extraInfo = "H2H"+"|"+InetAddress.getLocalHost()+"|"+ni.getHardwareAddress()+"|NA|Hardware id client"+System.getProperty("os.name").toLowerCase()+"|NA";
        map = new HashMap<String, String>();
        map.put("0","0200");
        map.put("3","000500");
        map.put("4",addCero(monto,12));
        map.put("11",getSecuencia());
        map.put("12",getHora());
        map.put("13",getFecha());
        map.put("37",getReferencia());
        map.put("41",identificador);
        map.put("60",recarga);
        map.put("61",extraInfo);
    }  
    
    public void consultaServiciosPublicos(String empresa,String numeroReferencia,String zonaSoloCabletica,String tipoSoloCabletica) throws UnknownHostException, SocketException{
        NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
        String datos = usuario + "," + dispersion(clave) + ","+empresa+","+numeroReferencia;
        if(empresa.equals("021003")){
            datos += ","+zonaSoloCabletica+","+tipoSoloCabletica;
        }
        datos += ",NA,NA,NA,NA";
        String extraInfo = "H2H"+"|"+InetAddress.getLocalHost()+"|"+ni.getHardwareAddress()+"|NA|Hardware id client"+System.getProperty("os.name").toLowerCase()+"|NA";
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
    
    public void pagarServiciosPublicos(String monto,String consecutivoRecibo,String numeroReferencia,String zonaSoloCabletica) throws UnknownHostException, SocketException{
        NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
        String datos = usuario + "," + dispersion(clave) + ","+consecutivoRecibo+","+monto+"00";
        if(!zonaSoloCabletica.equals("")){
            datos += ","+zonaSoloCabletica;
        }
        
        String extraInfo = "H2H"+"|"+InetAddress.getLocalHost()+"|"+ni.getHardwareAddress()+"|NA|Hardware id client"+System.getProperty("os.name").toLowerCase()+"|NA";
        map = new HashMap<String, String>();
        map.put("0","0200");
        map.put("3","000006");
        map.put("3",monto);
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
        return str;
    }
      

    private String getSecuencia() {
        return "11";
    }

    private String getIdentificador() {
        return "11";
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
        String values = "<"+parent+">";
        for ( String key : map.keySet() ) {
            values += "<field id=\""+key+"\" value=\""+ map.get(key) +"\"/>";
        }
        values += "</"+parent+">";
        return values;
    }
    
    public Map<String, String> getFromXML(Document doc){
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
        return map;
    }

    private String getHora() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        return Integer.toString(cal.get(Calendar.HOUR_OF_DAY)) + Integer.toString(cal.get(Calendar.MINUTE)) + Integer.toString(cal.get(Calendar.SECOND));
    }

    private String getFecha() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        return Integer.toString(cal.get(Calendar.MONTH)) + Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) + Integer.toString(cal.get(Calendar.YEAR));
    }

    private String getReferencia() {
        return "000000000001";
    }

    private String dispersion(String clave) {
        String enc = "";
        try {
            enc = sha1(clave);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
        return enc;
    }
    
    static String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
         
        return sb.toString();
    }

    private String addCero(String monto, int i) {
        return String.format("%0"+i+"d", Integer.parseInt(monto));
    };

    private Map<String, String> pupulateCodigosMap() {
        codigosMap.put("00","Transacción aprobada en forma exitosa");
        codigosMap.put("04","Producto no asignado");
        codigosMap.put("1D","Clave invalida");
        codigosMap.put("25","No existe ultima transacción");
        codigosMap.put("27","Saldo insuficiente");
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
        codigosMap.put("77","Error interno de plataforma. Falta secuenciador de operador.");
        
        return codigosMap;
    }

    public String getMsgResponse() {
        return codigosMap.get(map.get("39"));
    }
    
    public String getMsgAprovacion() {
        return map.get("38");
    }
    
}
