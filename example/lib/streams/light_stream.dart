import 'dart:async';

class LightStream {
  final _controller = StreamController<dynamic>();

  Stream<dynamic> get stream => _controller.stream;

  void addData(var data) => _controller.sink.add(data);

  dispose() {
    _controller.close();
  }
}
