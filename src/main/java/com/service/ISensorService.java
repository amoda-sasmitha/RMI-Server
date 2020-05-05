package com.service;

import com.model.Sensor;
import com.model.SensorLog;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;

public interface ISensorService extends Remote {

    public ArrayList<Sensor> getAllSensorsCurrentData() throws RemoteException;

    public LinkedList<SensorLog> getAllSensorsLog() throws RemoteException;

    public Sensor getSensorCurrentData(int id) throws RemoteException;

    public LinkedList<SensorLog> getSensorLog(int id) throws RemoteException;

    public String test(String msg) throws RemoteException;

    public void insertSensor(Sensor sensor) throws RemoteException;

    public void editSensor(Sensor sensor) throws RemoteException;

    public void deleteSensor(int id) throws RemoteException;

    public void updateValues() throws IOException;
}
