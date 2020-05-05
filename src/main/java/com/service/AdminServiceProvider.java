package com.service;

import com.google.gson.*;
import com.model.Admin;
import com.model.SensorLog;
import com.util.HttpController;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedList;

public class AdminServiceProvider extends UnicastRemoteObject implements IAdminService{

    public AdminServiceProvider() throws RemoteException {
    }

    public ArrayList<Admin> getAllAdmins() throws RemoteException {
        Gson gson = new Gson();
        ArrayList<Admin> admin_list = new ArrayList<Admin>();
        String response = null;
        try {
            response = HttpController.Get( "users/a/all" );
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonObject result = new JsonParser().parse(response).getAsJsonObject();
        JsonArray array = result.getAsJsonArray("result");

        for (JsonElement item : array) {
            Admin admin = gson.fromJson(item.toString(), Admin.class);
            admin.setPassword("");
            admin_list.add(admin);
        }
        return admin_list;
    }

    public void insertAdmin(Admin admin) throws RemoteException {
        String response = null;
        Gson gson = new Gson();
        JsonObject adminJson = new JsonObject();
        adminJson.addProperty("uName", admin.getName() );
        adminJson.addProperty("uPass", "default123" );
        adminJson.addProperty("uEmail", admin.getEmail() );
        adminJson.addProperty("uCn", admin.getPhone() );

        JsonObject finaladmin = new JsonObject();
        finaladmin.add("admin", adminJson );

        try {
            response = HttpController.Post( "users/register" , finaladmin.toString() );
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response);

    }

    public void editAdmin(Admin admin) throws RemoteException {

    }

    public void deleteAdmin(int id, String password) throws RemoteException {
        String response = null;

        JsonObject adminJson = new JsonObject();
        adminJson.addProperty("id", id );
        adminJson.addProperty("password", password);

        try {
            response = HttpController.Post( "users/a/r" , adminJson.toString() );
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response);
    }

    public boolean loginUser(String email, String password) throws RemoteException {
        String response = null ;
        JsonObject adminJson = new JsonObject();
        adminJson.addProperty("uEmail", email );
        adminJson.addProperty("uPass", password);

        JsonObject finaladmin = new JsonObject();
        finaladmin.add("admin", adminJson );

        try {
            response = HttpController.Post( "users/login" , finaladmin.toString() );
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonObject result = new JsonParser().parse(response).getAsJsonObject();
        JsonPrimitive message = result.getAsJsonPrimitive("message");

        System.out.println(message.getAsString() );
        if(message.getAsString().equalsIgnoreCase("Login Sucess") ){
            return true;
        }else{
            return false;
        }
    }
}
