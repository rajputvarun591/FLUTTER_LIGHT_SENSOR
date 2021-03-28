import 'dart:async';

import 'package:flutter/services.dart';

class FlutterLightSensor {
  //static const MethodChannel _methodChannel = const MethodChannel('light_method_channel');
  static const EventChannel _eventChannel = const EventChannel('light_event_channel');

  Stream<dynamic> getSensorData() async* {
    final sensorStream = _eventChannel.receiveBroadcastStream();
    await for (var stream in sensorStream) {
      print(stream);
      yield stream;
    }
  }
}
