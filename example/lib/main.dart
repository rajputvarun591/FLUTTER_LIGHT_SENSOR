import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_light_sensor/flutter_light_sensor.dart';
import 'package:flutter_light_sensor_example/light_interpolation.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _lightData = "";

  FlutterLightSensor _lightStream = FlutterLightSensor();
  StreamSubscription<dynamic> _streamSubscription;

  @override
  void initState() {
    super.initState();
    _streamSubscription = _lightStream.getSensorData().listen(_listen);
  }

  @override
  void dispose() {
    super.dispose();
    _streamSubscription.cancel();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text('Running on: $_lightData'),
        ),
      ),
    );
  }

  void _listen(event) {
    setState(() {
      _lightData = event.toString();
    });
  }
}
