package Util;

import android.app.Application;
import android.util.Log;

import com.example.yueli.myapplication.bean.myMessage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by yueli on 2018/4/27.
 */

public class ApplicationUtil extends Application {
    private Socket socket=null;
    private DataOutputStream out = null;
    private DataInputStream in = null;
    private PrintWriter pw=null;
    private BufferedReader br=null;
    private String user=null;
    public static String serverIP="10.0.2.2";
    private List<myMessage> myMessageList;
    private ArrayList<String>Friends;
    private ArrayList<String>Groups;
    private ObjectInputStream ois;

    public void init() throws IOException, Exception{
        this.socket = new Socket(serverIP,8000);//
        Log.v("arki","remote ip"+socket.getInetAddress());
        Log.v("arki","my ip"+socket.getLocalAddress());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.in = new DataInputStream(socket.getInputStream());
        this.pw=new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
                ,true);
        this.br=new BufferedReader(new InputStreamReader(socket.getInputStream()));

        myMessageList =new ArrayList<>();

    }

    public void setMyMessageList(List<myMessage> myMessageList){this.myMessageList = myMessageList;}
    public void addToMsgList(myMessage m){synchronized (myMessageList){this.myMessageList.add(m);}}
    public List<myMessage> getMyMessageList(){return this.myMessageList;}
    public void setUser(String user){this.user=user;}
    public String getUser(){return user;}
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public DataOutputStream getOut() {
        return out;
    }
    public PrintWriter getPw(){
        return pw;
    }
    public void setOut(DataOutputStream out) {
        this.out = out;
    }
    public DataInputStream getIn() {
        return in;
    }
    public BufferedReader getBr(){
        return br;
    }

    public void setIn(DataInputStream in) {
        this.in = in;
    }

    public void receiveData() throws Exception{
        ois=new ObjectInputStream(socket.getInputStream());
        Object Friends=ois.readObject();
        Object Groups=ois.readObject();
        this.Friends=(ArrayList<String>)Friends;
        this.Groups=(ArrayList<String>)Groups;
    }
    public List<String>getFriends(){return Friends;}
    public List<String>getGroups(){return Groups;}
    public static String getIp(){
        String localip=null;
        String netip=null;
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            boolean finded=false;
            while(netInterfaces.hasMoreElements() && !finded){
                NetworkInterface ni=netInterfaces.nextElement();
                Enumeration<InetAddress> address=ni.getInetAddresses();
                while(address.hasMoreElements()){
                    ip=address.nextElement();
                    if( !ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":")==-1){
                        netip=ip.getHostAddress();
                        finded=true;
                        break;
                    }else if(ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":")==-1){
                        localip=ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if(netip!=null && !"".equals(netip)){
            return netip;
        }else{
            return localip;
        }
    }
}
