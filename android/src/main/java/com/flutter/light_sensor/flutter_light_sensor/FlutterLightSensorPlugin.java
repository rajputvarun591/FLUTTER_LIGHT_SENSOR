package com.flutter.light_sensor.flutter_light_sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.annotation.NonNull;


import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FlutterLightSensorPlugin */
public class FlutterLightSensorPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware, SensorEventListener, EventChannel.StreamHandler{
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  /// The MethodChannel that will the communication between Flutter and native Android
  private MethodChannel methodChannel;

  /// The EventChannel that will the communication between Flutter and native Android specially with the Streams
  private EventChannel eventChannel;

  /// The SensorManager lets you access the device's sensors.
  private SensorManager sensorManager;

  private EventChannel.EventSink eventSink;
  private boolean isListenerRegistered = false;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    methodChannel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "light_method_channel");
    methodChannel.setMethodCallHandler(this);
    eventChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), "light_event_channel");
    eventChannel.setStreamHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if(call.method.equals("getAllAvailableSensors") && sensorManager != null) {
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        if(sensorList.isEmpty()) {
          result.success("Can't find any Sensor on this Device");
        } else {
          final List<Object> list = new ArrayList<>(sensorList.size());
          for (Sensor sensor : sensorList) {
            list.add("{" +
                    "\"name\" : " + "\"" + sensor.getName()+ "\", " +
                    "\"vendor\" : " + "\"" + sensor.getVendor()+ "\", " +
                    "\"version\" : " + "\"" + sensor.getVersion()+ "\", " +
                    "\"type\" : " + "\"" + sensor.getType()+ "\", " +
                    "\"maxRange\" : " + "\"" + sensor.getMaximumRange()+ "\", " +
                    "\"resolution\" : " + "\"" + sensor.getResolution()+ "\", " +
                    "\"power\" : " + "\"" + sensor.getPower()+ "\", " +
                    "\"minDelay\" : " + "\"" + sensor.getMinDelay()+ "\"" +
                    "}"); // Map<String, Object> entries
          }
          result.success(list);
        }
    }else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    methodChannel.setMethodCallHandler(null);
    eventChannel.setStreamHandler(null);
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    sensorManager = (SensorManager) binding.getActivity().getSystemService(Context.SENSOR_SERVICE);
  }

  @Override
  public void onDetachedFromActivity() {
    sensorManager.unregisterListener(this);
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    //TODO has to be done
    //throw UnknownError;
  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    //TODO has to be done
    //throw UnknownError;
  }


  @Override
  public void onSensorChanged(SensorEvent event) {
    final List<Float> list = new ArrayList<>(event.values.length);
    for (float data : event.values) {
      list.add(data); // Map<String, Object> entries
    }
    eventSink.success(list);
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
    //TODO has to be done
    //throw UnknownError;
  }

  @Override
  public void onListen(Object arguments, EventChannel.EventSink eventSink) {
    if(eventSink != null){
      this.eventSink = eventSink;
    }
    if(!isListenerRegistered) {
      Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
      isListenerRegistered = sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
  }

  @Override
  public void onCancel(Object arguments) {
    //TODO has to be done
    //throw UnknownError;
  }
}
