package com.service;

import com.model.Admin;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IAdminService extends Remote {
    public ArrayList<Admin> getAllAdmins() throws RemoteException;
    public void insertAdmin(Admin admin) throws RemoteException;
    public void editAdmin(Admin admin) throws RemoteException;
    public void deleteAdmin(int id, String password) throws RemoteException;
    public boolean loginUser(String email ,String password) throws RemoteException;
}
