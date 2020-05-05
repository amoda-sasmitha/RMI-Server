package com.service;

import com.google.gson.*;
import com.model.Sensor;
import com.model.SensorLog;
import com.util.HttpController;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

public class SensorServiceProvider extends UnicastRemoteObject implements ISensorService{

    private  ArrayList<Sensor> sensorsdata ;
    private LinkedList<SensorLog> sensorslog;

    public SensorServiceProvider() throws RemoteException {
        this.sensorsdata = new ArrayList<Sensor>();
        this.sensorslog = new LinkedList<SensorLog>();
    }

    public ArrayList<Sensor> getAllSensorsCurrentData() throws RemoteException {
        return sensorsdata;
    }

    public LinkedList<SensorLog> getAllSensorsLog() throws RemoteException {
        return sensorslog;
    }

    public Sensor getSensorCurrentData(int id) throws RemoteException {
        for (Sensor sensor : sensorsdata) {
            if(sensor.getId() == id ){
                return sensor;
            }
        }
        return null;
    }

    public LinkedList<SensorLog> getSensorLog(int id) throws RemoteException {
        System.out.println("called");
        LinkedList<SensorLog> single_sensorslog = new LinkedList<SensorLog>();

        Gson gson = new Gson();
        DateFormat time = new SimpleDateFormat("HH:mm:ss");

        String response = null;
        try {
            response = HttpController.Get( "sensors/getall/"+ id +"/1" );
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonObject result = new JsonParser().parse(response).getAsJsonObject();
        JsonObject data = result.getAsJsonObject("data");
        JsonArray log_array = data.getAsJsonArray("log");

        for (JsonElement  item : log_array) {
            SensorLog log = gson.fromJson( item.toString() , SensorLog.class );

            Calendar cal = javax.xml.bind.DatatypeConverter.parseDateTime(log.getDatetime());
            String timex  =  time.format( cal.getTime() );
            log.setDatetime(timex);

            single_sensorslog.add(log);
        }

        if(single_sensorslog.size() > 16){
            single_sensorslog =  new LinkedList<SensorLog>(single_sensorslog.subList( single_sensorslog.size() - 10 , single_sensorslog.size() ));

        }
        return single_sensorslog;
    }

    public String test(String msg) throws RemoteException {
        return "Server : " + msg;
    }

    public void insertSensor(Sensor sensor) throws RemoteException{
        String response = null;
        Gson gson = new Gson();

        try {
            response = HttpController.Post( "sensors/insert" , gson.toJson(sensor) );
            updateValues();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response);
    }

    public void editSensor(Sensor sensor) throws RemoteException{
        String response = null;
        Gson gson = new Gson();

        try {
            response = HttpController.Post( "sensors/update" , gson.toJson(sensor) );
            updateValues();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response);

    }

    public void deleteSensor(int id) throws RemoteException{
        String response = null;
        Gson gson = new Gson();

        try {
            response = HttpController.Post( "sensors/delete/"+ id , "" );
            updateValues();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void updateValues() throws IOException {

        Gson gson = new Gson();

        DateFormat time = new SimpleDateFormat("HH:mm:ss");

        String response =  HttpController.Get( "sensors/getall/1" );
        JsonObject result = new JsonParser().parse(response).getAsJsonObject();

        JsonArray current_array = result.getAsJsonArray("current");
        JsonArray log_array = result.getAsJsonArray("log");

        sensorsdata.clear();
        sensorslog.clear();
        System.out.println("------------------------------");
        for (JsonElement  item : log_array) {
            SensorLog log = gson.fromJson( item.toString() , SensorLog.class );

            Calendar cal = javax.xml.bind.DatatypeConverter.parseDateTime(log.getDatetime());
            String timex  =  time.format( cal.getTime() );

            log.setDatetime(timex);
            sensorslog.add(log);
        }


        if(sensorslog.size() > 16){
            sensorslog =  new LinkedList<SensorLog>(sensorslog.subList( sensorslog.size() - 10 , sensorslog.size() ));

        }

        for (JsonElement  item : current_array) {
            Sensor sensor = gson.fromJson( item.toString() , Sensor.class );
            sensor.setStatus( returnStatus(sensor.getCo2_level() , sensor.getSmoke_level() ));
            sensorsdata.add(sensor);
        }

    }

    private String returnStatus(int c02 , int smoke){
        float avg = (float) c02 + smoke / 2;
        if(avg >= 0 && avg <= 2 ){
            return "Normal";
        }else if( avg >= 3 && avg <= 4 ){
            return "Average";
        }else if( avg >= 5 && avg <= 10 ){
            return "Danger";
        }
        return "None";
    }
}
