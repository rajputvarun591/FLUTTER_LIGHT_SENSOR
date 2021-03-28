import 'dart:async';

import 'package:flutter_light_sensor_example/streams/light_stream.dart';

class LightInterpolationStream {
  Map<String, StreamSubscription> _subscriptions;
  LightStream _lightStream;

  LightInterpolationStream() {
    _subscriptions = Map<String, StreamSubscription>();
    _lightStream = LightStream();
  }

  Stream<dynamic> getLightData() async* {
    await for (var data in _lightStream.stream) {
      yield data;
    }
  }

  void cancel() {
    _subscriptions?.forEach((key, value) => value?.cancel());
    _lightStream?.dispose();
  }
}
