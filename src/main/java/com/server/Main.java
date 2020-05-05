package com.server;

import com.model.Admin;
import com.model.Sensor;
import com.service.AdminServiceProvider;
import com.service.IAdminService;
import com.service.ISensorService;
import com.service.SensorServiceProvider;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Timer;
import java.util.TimerTask;


public class Main  {

    public static void main(String[] args) {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        System.setProperty("java.security.policy", "file:./allowall.policy");

        try {
            if(System.getSecurityManager() == null ){
                System.setSecurityManager( new RMISecurityManager() );
            }

            final ISensorService sensorServiceProvider = new SensorServiceProvider();
            final IAdminService adminServiceProvicer = new AdminServiceProvider();

            Registry registry = LocateRegistry.createRegistry(4500);

            registry.rebind("SensorService", sensorServiceProvider);
            registry.rebind("AdminService", adminServiceProvicer );

            System.out.println("Server Started..");

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        sensorServiceProvider.updateValues();
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            }, 0, 5000);




        } catch (Exception e ) {
            System.out.println( "r " + e);
        }

    }
}
