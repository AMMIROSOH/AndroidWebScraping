package com.example.webscraping;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JSON_PARSE {
    static JSONObject auto_rec_acc = new JSONObject();
    static JSONObject auto_rec_mag = new JSONObject();
    static JSONObject auto_rec_gyro = new JSONObject();

    public final String MAINJSONNAME = "data.json";
    public final String AUTOACCJSONNAME = "auto_rec_acc.json";
    public final String AUTOMAGJSONNAME = "auto_rec_mag.json";
    public final String AUTOGYROJSONNAME = "auto_rec_gyro.json";
    public final  String SENSORTYPEACC = "accelerometer";
    public final  String SENSORTYPEMAG = "magnetometer";
    public final  String SENSORTYPEGYRO = "gyroscope";

    JSON_PARSE(Context context){

    }
    public void empty_auto(Context context){
        auto_rec_acc = new JSONObject();
        auto_rec_mag = new JSONObject();
        auto_rec_gyro = new JSONObject();
        Toast.makeText(context, "Data Was Not Saved!",Toast.LENGTH_SHORT).show();
    }
    public void put_auto_acc(Context context){
        try {
            File file = new File(context.getFilesDir(), AUTOACCJSONNAME);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            String responce = stringBuilder.toString();
            //converting json-string to json and adding item
            JSONObject main  = new JSONObject(responce);
            for (int i = 1;i<= auto_rec_acc.length();i++){
                main.put((main.length() + 1) +"", auto_rec_acc.get(i + "") );
            }
            String main_string = main.toString();
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(main_string);
            bufferedWriter.close();
            auto_rec_acc = new JSONObject();
        }
        catch (IOException | JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception", e);
        }
    }
    public void put_auto_mag(Context context){
        try {
            File file = new File(context.getFilesDir(), AUTOMAGJSONNAME);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            String responce = stringBuilder.toString();
            //converting json-string to json and adding item
            JSONObject main  = new JSONObject(responce);
            for (int i = 1;i<= auto_rec_mag.length();i++){
                main.put((main.length() + 1) +"", auto_rec_mag.get(i + "") );
            }
            String main_string = main.toString();
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(main_string);
            bufferedWriter.close();
            auto_rec_mag = new JSONObject();
        }
        catch (IOException | JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception", e);
        }
    }
    public void put_auto_gyro(Context context){
        try {
            File file = new File(context.getFilesDir(), AUTOGYROJSONNAME);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            String responce = stringBuilder.toString();
            //converting json-string to json and adding item
            JSONObject main  = new JSONObject(responce);
            for (int i = 1;i<= auto_rec_gyro.length();i++){
                main.put((main.length() + 1) +"", auto_rec_gyro.get(i + "") );
            }
            String main_string = main.toString();
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(main_string);
            bufferedWriter.close();
            Toast.makeText(context, "Recorded Successfully!",Toast.LENGTH_LONG).show();
            auto_rec_gyro = new JSONObject();
        }
        catch (IOException | JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception", e);
        }
    }
    public void add_auto_input(JSONArray acc_mag_gyro, String type, double time, Context context){
        try {
            JSONObject item = new JSONObject();
            item.put("date", time);
            item.put(type,  acc_mag_gyro);
            item.put("label", "None");
            if(type == SENSORTYPEACC){
                auto_rec_acc.put("" + (auto_rec_acc.length() + 1),item);
            }
            else if(type==SENSORTYPEMAG){
                auto_rec_mag.put("" + (auto_rec_mag.length() + 1),item);
            }
            else if(type==SENSORTYPEGYRO){
                auto_rec_gyro.put("" + (auto_rec_gyro.length() + 1),item);
            }
        }
        catch (JSONException /*| IOException*/ e) {
            Log.e("MYAPP", "unexpected JSON exception", e);
        }
    }
    public void create_json_files(Context context){
        try {
            JSONObject Main = new JSONObject();
            String userString = Main.toString();
            File file = new File(context.getFilesDir(),"auto_rec_acc.json");
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(userString);
            bufferedWriter.close();
            file = new File(context.getFilesDir(),"auto_rec_mag.json");
            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(userString);
            bufferedWriter.close();
            file = new File(context.getFilesDir(),"auto_rec_gyro.json");
            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(userString);
            bufferedWriter.close();
        }
        catch ( IOException e) {
            Log.e("MYAPP", "unexpected JSON exception", e);
        }
    }
}
